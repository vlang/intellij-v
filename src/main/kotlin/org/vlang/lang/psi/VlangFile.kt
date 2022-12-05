package org.vlang.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.project.guessProjectDir
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
import org.vlang.ide.codeInsight.VlangAttributesUtil
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
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

class VlangFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, VlangLanguage.INSTANCE) {
    override fun getFileType() = VlangFileType.INSTANCE

    override fun toString() = "V Language file"

    override fun getIcon(flags: Int) = VIcons.V

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement,
    ): Boolean {
        return ResolveUtil.processChildren(this, processor, state, lastParent, place)
    }

    fun isScriptScript(): Boolean = name.endsWith(".vsh")

    fun isTestFile(): Boolean = name.split(".").first().endsWith("_test")

    fun isCFile(): Boolean = name.endsWith(".c.v")

    fun isJSFile(): Boolean = name.endsWith(".js.v")

    fun isTranslatedFile(): Boolean {
        return getFileAttributes().any {
            VlangAttributesUtil.isTranslated(it)
        }
    }

    private fun getFileAttributes(): List<VlangAttribute> {
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
        return findChildByClass(VlangModuleClause::class.java)
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

        val projectDir = project.guessProjectDir() ?: return ""
        val stdlib = VlangConfiguration.getInstance(project).stdlibLocation
        val modules = VlangConfiguration.getInstance(project).modulesLocation
        val src = VlangConfiguration.getInstance(project).srcLocation
        val localModules = VlangConfiguration.getInstance(project).localModulesLocation
        val rootDirs = listOfNotNull(projectDir, stdlib, modules, src, localModules)

        val moduleNames = mutableListOf<String>()
        var dir = virtualFile?.parent
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

        val qualifierNames = moduleNames.reversed().toMutableList()

        if (modules != null && virtualFile != null && virtualFile.path.startsWith(modules.path)) {
            if (qualifierNames.isNotEmpty() && qualifierNames[0] == moduleName) {
                // iui.iui.* -> iui.*
                qualifierNames.removeAt(0)
            }
            if (qualifierNames.lastOrNull() == "src") {
                // iui.src.* -> iui.*
                qualifierNames.removeLast()
            }
        }

        val qualifier = qualifierNames.reversed()
            .joinToString(".")
            .removePrefix("${VlangCodeInsightUtil.BUILTIN_MODULE}.")

        if (qualifier.isNotEmpty()) {
            return "$qualifier.$moduleName"
        }

        return moduleName
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
                val decls =
                    getChildrenByType(stub!!, VlangTypes.GLOBAL_VARIABLE_DECLARATION) { arrayOfNulls<VlangGlobalVariableDeclaration>(it) }
                decls.filterNotNull().flatMap { it.globalVariableDefinitionList }
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
                val decls = getChildrenByType(stub!!, VlangTypes.CONST_DECLARATION) { arrayOfNulls<VlangConstDeclaration>(it) }
                decls.filterNotNull().flatMap { it.constDefinitionList }
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
}
