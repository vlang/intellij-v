package org.vlang.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.util.ArrayFactory
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

    private fun getImports(): List<VlangImportSpec> {
        return findChildByClass(VlangImportList::class.java)?.importDeclarationList?.mapNotNull {
            it.importSpec
        } ?: emptyList()
    }

    fun resolveImportName(name: String): String? {
        return resolveImportNameAndSpec(name).first
    }

    fun resolveImportNameAndSpec(name: String): Pair<String?, VlangImportSpec?> {
        val imports = getImports()
        for (import in imports) {
            if (import.importAlias?.name == name) {
                return import.identifier.text to import
            }

            val selectiveImport = import.selectiveImportList?.referenceExpressionList?.any {
                it.getIdentifier().text == name
            } ?: false

            if (selectiveImport) {
                return import.name + "." + name to import
            }

            val importName = import.name
            if (importName == name) {
                return importName to import
            }

            if (import.lastPart == name) {
                return importName to import
            }
        }

        return null to null
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

    // TODO
    fun getTypes(): List<VlangStructDeclaration> =
        getNamedElements(VlangTypes.TYPE_ALIAS_DECLARATION, VlangStructDeclarationStubElementType.ARRAY_FACTORY)

    private inline fun <reified T : PsiElement?> getNamedElements(elementType: IElementType, arrayFactory: ArrayFactory<T>): List<T> {
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
