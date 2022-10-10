package org.vlang.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.util.ArrayFactory
import org.vlang.configurations.VlangConfiguration
import org.vlang.ide.codeInsight.VlangAttributesUtil
import org.vlang.ide.ui.VIcons
import org.vlang.lang.VlangFileType
import org.vlang.lang.VlangLanguage
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.types.*

class VlangFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, VlangLanguage.INSTANCE), PsiImportHolder, PsiClassOwner {

    override fun getFileType() = VlangFileType.INSTANCE

    override fun toString() = "V Language file"

    override fun getIcon(flags: Int) = VIcons.Vlang

    override fun getReference() = references.getOrNull(0)

    override fun getReferences(): Array<PsiReference?> = ReferenceProvidersRegistry.getReferencesFromProviders(this)

    override fun getClasses(): Array<PsiClass> = emptyArray()

    override fun getPackageName() = getModuleName()

    override fun setPackageName(packageName: String?) {}

    override fun importClass(aClass: PsiClass) = false

    fun isTestFile(): Boolean = name.split(".").first().endsWith("_test")

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
        val moduleName = stub?.getModuleName()
        if (moduleName != null) {
            return moduleName
        }

        return getModule()?.name
    }

    fun getModuleQualifiedName(): String {
        val stub = stub as? VlangFileStub
        val moduleQualifiedName = stub?.getModuleQualifiedName()
        if (moduleQualifiedName != null) {
            return moduleQualifiedName
        }

        val moduleName = getModuleName() ?: return ""

        val projectDir = project.guessProjectDir() ?: return ""
        val stdlib = VlangConfiguration.getInstance(project).stdlibLocation
        val modules = VlangConfiguration.getInstance(project).modulesLocation

        val moduleNames = mutableListOf<String>()
        var dir = virtualFile?.parent?.parent // parent directory for directory with this file
        var depth = 0
        while (dir != projectDir && dir != stdlib && dir != modules && dir != null && depth < 10) {
            moduleNames.add(dir.name)

            dir = dir.parent
            depth++
        }

        val qualifier = moduleNames.reversed().joinToString(".").removePrefix("builtin.")
        if (qualifier.isNotEmpty()) {
            return "$qualifier.$moduleName"
        }
        return moduleName ?: ""
    }

    fun getImports(): List<VlangImportSpec> {
        return findChildByClass(VlangImportList::class.java)?.importDeclarationList?.mapNotNull {
            it.importSpec
        } ?: emptyList()
    }

    fun resolveImportName(name: String): String? {
        return resolveImportNameAndSpec(name).first
    }

    fun resolveImportNameAndSpec(name: String): Triple<String?, VlangImportSpec?, Boolean> {
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

//            val importName = import.name
//            if (importName == name) {
//                return importName to import
//            }

            if (import.importPath.lastPart == name) {
                return Triple(import.importPath.qualifiedName, import, false)
            }
        }

        return Triple(null, null, false)
    }

    fun resolveName(name: String): String? {
        val imports = getImports()
        for (import in imports) {
            if (import.importAlias?.name == name) {
                return import.getIdentifier().text
            }

            val selectiveImport = import.selectiveImportList?.referenceExpressionList?.any {
                it.getIdentifier().text == name
            } ?: false

            if (selectiveImport) {
                return import.name + "." + name
            }

            val importName = import.name
            if (importName == name) {
                return importName
            }

            if (import.lastPart == name) {
                return importName
            }
        }

        val module = getModuleName() ?: return name

        return "$module.$name"
    }

    fun getFunctions(): List<VlangFunctionDeclaration> =
        getNamedElements(VlangTypes.FUNCTION_DECLARATION, VlangFunctionDeclarationStubElementType.ARRAY_FACTORY)

    fun getStructs(): List<VlangStructDeclaration> =
        getNamedElements(VlangTypes.STRUCT_DECLARATION, VlangStructDeclarationStubElementType.ARRAY_FACTORY)

    fun getUnions(): List<VlangUnionDeclaration> =
        getNamedElements(VlangTypes.UNION_DECLARATION, VlangUnionDeclarationStubElementType.ARRAY_FACTORY)

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
                    VlangPsiImplUtil.traverser()
                        .children(this)
                        .filter(T::class.java)
                        .toList()
                }

            CachedValueProvider.Result.create(elements, this)
        }
    }

    fun getMethods(): List<VlangMethodDeclaration> {
        return CachedValuesManager.getCachedValue(
            this
        ) {
            val calc: List<VlangMethodDeclaration> =
                if (stub != null) getChildrenByType(
                    stub!!,
                    VlangTypes.METHOD_DECLARATION,
                    VlangMethodDeclarationStubElementType.ARRAY_FACTORY
                ) else VlangPsiImplUtil.traverser().children(this).filter(
                    VlangMethodDeclaration::class.java
                ).toList()
            CachedValueProvider.Result.create(
                calc,
                this
            )
        }
    }

    fun <E : PsiElement?> getChildrenByType(
        stub: StubElement<out PsiElement>,
        elementType: IElementType,
        f: ArrayFactory<E>,
    ): List<E> {
        return listOf(*stub.getChildrenByType(elementType, f))
    }
}
