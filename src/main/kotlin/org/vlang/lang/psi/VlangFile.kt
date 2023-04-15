package org.vlang.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.util.ArrayFactory
import org.vlang.configurations.VlangConfiguration
import org.vlang.configurations.VlangProjectStructureState.Companion.projectStructure
import org.vlang.ide.codeInsight.VlangAttributesUtil
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.run.VlangRunConfigurationProducer
import org.vlang.ide.ui.VIcons
import org.vlang.lang.VlangFileType
import org.vlang.lang.VlangLanguage
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.impl.ResolveUtil
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.types.VlangEnumDeclarationStubElementType
import org.vlang.lang.stubs.types.VlangFunctionDeclarationStubElementType
import org.vlang.lang.stubs.types.VlangInterfaceDeclarationStubElementType
import org.vlang.lang.stubs.types.VlangStructDeclarationStubElementType

open class VlangFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, VlangLanguage) {
    override fun getFileType() = VlangFileType

    override fun toString() = "V Language file"

    override fun getIcon(flags: Int) = if (isShellScript()) VIcons.Vsh else VIcons.V

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement,
    ): Boolean {
        return ResolveUtil.processChildren(this, processor, state, lastParent, place)
    }

    fun isShellScript(): Boolean = name.endsWith(".vsh")

    fun isTestFile(): Boolean = name.split(".").first().endsWith("_test")

    fun isCFile(): Boolean = name.endsWith(".c.v")

    fun isJSFile(): Boolean = name.endsWith(".js.v")

    fun isTranslatedFile(): Boolean {
        return getFileAttributes().any {
            VlangAttributesUtil.isTranslated(it)
        }
    }

    fun fromModules(): Boolean {
        val modulesDir = VlangConfiguration.getInstance(project).modulesLocation?.path ?: return false
        val path = virtualFile?.path ?: return false
        return path.startsWith(modulesDir)
    }

    fun fromTests(): Boolean {
        val path = virtualFile?.path?.normalizeSlashes() ?: return false
        return path.contains("/tests/")
    }

    fun isPlatformSpecificFile(): Boolean {
        val name = name.substringBefore(".", name)
        val platform = name.substringAfterLast("_", name)
        return platform in VlangRunConfigurationProducer.KNOWN_PLATFORMS
    }

    fun getFileAttributes(): List<VlangAttribute> {
        val moduleStatement = this.getModule() ?: return emptyList()
        return moduleStatement.attributes?.attributeList ?: emptyList()
    }

    fun addImport(path: String, alias: String?): VlangImportSpec {
        if (getImportedModulesMap().containsKey(path)) {
            return getImportedModulesMap()[path]!!
        }

        return VlangPsiImplUtil.addImport(this, getImportList(), path, alias)
    }

    fun getImportedModulesMap(): Map<String, VlangImportSpec> {
        return CachedValuesManager.getCachedValue(this) {
            val map = mutableMapOf<String, VlangImportSpec>()
            for (spec in getImports()) {
                val path = spec.importPath.qualifiedName
                map[path] = spec
            }
            CachedValueProvider.Result.create(map, this)
        }
    }

    fun getImportList(): VlangImportList? {
        return findChildByClass(VlangImportList::class.java)
    }

    fun getModule(): VlangModuleClause? {
        return VlangPsiTreeUtil.getStubChildOfType(this, VlangModuleClause::class.java)
    }

    fun getShebang(): VlangShebangClause? {
        return findChildByClass(VlangShebangClause::class.java)
    }

    fun getModuleName(): String? {
        val stub = stub as? VlangFileStub
        if (stub != null) {
            return stub.getModuleName()
        }

        return getModule()?.name
    }

    fun getModuleQualifiedName(): String {
        return CachedValuesManager.getCachedValue(this) {
            CachedValueProvider.Result.create(
                moduleQualifiedNameInner(), PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    private fun moduleQualifiedNameInner(): String {
        var moduleName = getModuleName()
        // when no `module name` in file
        if (moduleName.isNullOrEmpty() && !isTestFile()) {
            return "unnamed"
        }

        if (moduleName == "main") {
            return moduleName
        }

        val virtualFile = originalFile.virtualFile
        var dir = virtualFile?.parent

        val projectDir = project.guessProjectDir() ?: return ""
        val stdlib = VlangConfiguration.getInstance(project).stdlibLocation
        val modules = VlangConfiguration.getInstance(project).modulesLocation
        val src = VlangConfiguration.getInstance(project).srcLocation
        val localModules = VlangConfiguration.getInstance(project).localModulesLocation
        val stubs = VlangConfiguration.getInstance(project).stubsLocation

        val insideLocalModules = modules != null && virtualFile != null && virtualFile.path.startsWith(modules.path)

        val vmodDir = if (insideLocalModules) null else findTopMostVmodFile(dir)
        val rootDirs = listOfNotNull(projectDir, stdlib, modules, src, localModules, stubs, vmodDir)

        val moduleNames = mutableListOf<String>()
        val insideTopLevelDir = dir == projectDir
        val dirName = dir?.name ?: ""
        var depth = 0
        while (dir != null && dir !in rootDirs && dir.name != "/" && depth < 10) {
            moduleNames.add(dir.name)

            dir = dir.parent
            depth++
        }

        if (moduleName == null) {
            moduleName = if (insideTopLevelDir) "main" else dirName
        }

        if (moduleNames.firstOrNull() == dirName) {
            moduleNames.removeAt(0)
        }

        // See vsl project, it contains file with `module vsl` in project root.
        val needAddProjectDir = project.projectStructure.libraryWithTopModule
        val inProjectSource = virtualFile != null && virtualFile.path.normalizeSlashes()
            .contains(projectDir.path.normalizeSlashes() + "/")

        if (needAddProjectDir && virtualFile != null && inProjectSource) {
            moduleNames.add(projectDir.name)
        }

        val qualifierNames = moduleNames.reversed().toMutableList()

        if (insideLocalModules) {
            if (qualifierNames.isNotEmpty() && qualifierNames[0] == moduleName) {
                // iui.iui.* -> iui.*
                qualifierNames.removeAt(0)
            }
            if (qualifierNames.lastOrNull() == "src") {
                // iui.src.* -> iui.*
                qualifierNames.removeLast()
            }
        }

        val qualifier = qualifierNames
            .joinToString(".")
            .removePrefix("${VlangCodeInsightUtil.BUILTIN_MODULE}.")

        if (qualifier.isNotEmpty()) {
            return "$qualifier.$moduleName"
        }

        return moduleName
    }

    private fun findTopMostVmodFile(file: VirtualFile?): VirtualFile? {
        if (file == null) return null

        val projectDir = project.guessProjectDir() ?: return null

        var dir = file
        var depth = 0
        while (dir != null && dir != projectDir && dir.name != "/" && depth < 10) {
            if (dir.children.any { it.name == "v.mod" }) {
                return dir
            }

            dir = dir.parent
            depth++
        }

        return null
    }

    fun getImports(): List<VlangImportSpec> {
        return findChildByClass(VlangImportList::class.java)?.importDeclarationList?.mapNotNull {
            it.importSpec
        } ?: emptyList()
    }

    fun resolveImportSpec(name: String): VlangImportSpec? {
        return resolveImportNameAndSpec(name).second
    }

    private fun resolveImportNameAndSpec(name: String): Triple<String?, VlangImportSpec?, Boolean> {
        val imports = getImports()
        for (import in imports) {
            if (import.importAlias?.name == name) {
                return Triple(import.importPath.qualifiedName, import, false)
            }

            val selectiveImport = import.selectiveImportList?.referenceExpressionList?.any {
                it.getIdentifier().text == name
            } ?: false

            if (selectiveImport) {
                return Triple(import.importPath.qualifiedName + "." + name, import, true)
            }

            if (import.importPath.lastPart == name) {
                return Triple(import.importPath.qualifiedName, import, false)
            }
        }

        return Triple(null, null, false)
    }

    fun getFunctions(): List<VlangFunctionDeclaration> =
        getNamedElements(VlangTypes.FUNCTION_DECLARATION, VlangFunctionDeclarationStubElementType.ARRAY_FACTORY)

    fun getStructs(): List<VlangStructDeclaration> =
        getNamedElements(VlangTypes.STRUCT_DECLARATION, VlangStructDeclarationStubElementType.ARRAY_FACTORY)

    fun getEnums(): List<VlangEnumDeclaration> =
        getNamedElements(VlangTypes.ENUM_DECLARATION, VlangEnumDeclarationStubElementType.ARRAY_FACTORY)

    fun getInterfaces(): List<VlangInterfaceDeclaration> =
        getNamedElements(VlangTypes.INTERFACE_DECLARATION, VlangInterfaceDeclarationStubElementType.ARRAY_FACTORY)

    fun getGlobalVariables(): List<VlangGlobalVariableDefinition> {
        val value = {
            if (stub != null) {
                getChildrenByType(stub!!, VlangTypes.GLOBAL_VARIABLE_DEFINITION) { arrayOfNulls<VlangGlobalVariableDefinition>(it) }
            } else {
                val decls = children.filterIsInstance<VlangGlobalVariableDeclaration>()
                decls.flatMap { it.globalVariableDefinitionList }
            }
        }

        return CachedValuesManager.getCachedValue(this) {
            CachedValueProvider.Result.create(value(), this)
        }
    }

    fun getConstants(): List<VlangConstDefinition> {
        val value = {
            if (stub != null) {
                getChildrenByType(stub!!, VlangTypes.CONST_DEFINITION) { arrayOfNulls<VlangConstDefinition>(it) }
            } else {
                val decls = children.filterIsInstance<VlangConstDeclaration>()
                decls.flatMap { it.constDefinitionList }
            }
        }

        return CachedValuesManager.getCachedValue(this) {
            CachedValueProvider.Result.create(value(), this)
        }
    }

    fun getTypes(): List<VlangTypeAliasDeclaration> {
        val value = {
            if (stub != null) {
                getChildrenByType(stub!!, VlangTypes.TYPE_ALIAS_DECLARATION) { arrayOfNulls<VlangTypeAliasDeclaration>(it) }.filterNotNull()
            } else {
                children.filterIsInstance<VlangTypeAliasDeclaration>()
            }
        }

        return CachedValuesManager.getCachedValue(this) {
            CachedValueProvider.Result.create(value(), this)
        }
    }

    private inline fun <reified T : PsiElement> getNamedElements(elementType: IElementType, arrayFactory: ArrayFactory<T>): List<T> {
        return CachedValuesManager.getCachedValue(this) {
            val elements =
                if (stub != null) {
                    getChildrenByType(stub!!, elementType, arrayFactory)
                } else {
                    SyntaxTraverser.psiApi()
                        .children(this)
                        .filter(T::class.java)
                        .toList()
                }

            CachedValueProvider.Result.create(elements, this)
        }
    }

    private fun <E : PsiElement?> getChildrenByType(
        stub: StubElement<out PsiElement>,
        elementType: IElementType,
        f: ArrayFactory<E>,
    ): List<E> {
        return listOf(*stub.getChildrenByType(elementType, f))
    }

    companion object {
        fun String.normalizeSlashes(): String {
            return this.replace("\\", "/")
        }
    }
}