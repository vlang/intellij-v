package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.psi.util.parentOfType
import com.intellij.util.Processor
import com.intellij.util.containers.JBIterable
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangFunctionTypeEx
import org.vlang.lang.stubs.index.VlangFieldFingerprintIndex
import org.vlang.lang.stubs.index.VlangMethodFingerprintIndex
import org.vlang.lang.stubs.index.VlangNamesIndex

class VlangInheritorsSearch : QueryExecutorBase<VlangNamedElement, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(queryParameters: DefinitionsScopedSearch.SearchParameters, processor: Processor<in VlangNamedElement>) {
        if (!queryParameters.isCheckDeep) return

        val o = queryParameters.element
        if (o is VlangInterfaceDeclaration) {
            val interfaceType = o.interfaceType
            val visitedSpecs = ReferenceOpenHashSet<VlangNamedElement>()
            val ownMethods = interfaceType.methodList
            val ownFields = interfaceType.getFieldList()

            processTypeSpec(
                queryParameters,
                processor,
                o,
                interfaceType,
                ownMethods,
                ownFields,
                true,
                visitedSpecs
            )
        }
    }

    fun processTypeSpec(
        parameter: DefinitionsScopedSearch.SearchParameters,
        processor: Processor<in VlangNamedElement>,
        typeSpec: VlangNamedElement,
        interfaceType: VlangInterfaceType,
        methods: Collection<VlangInterfaceMethodDefinition>,
        fields: List<VlangFieldDefinition>,
        canBeImplementedOutsidePackage: Boolean,
    ) {
        processTypeSpec(
            parameter,
            processor,
            typeSpec,
            interfaceType,
            methods,
            fields,
            canBeImplementedOutsidePackage,
            ReferenceOpenHashSet()
        )
    }

    fun processTypeSpec(
        parameter: DefinitionsScopedSearch.SearchParameters,
        processor: Processor<in VlangNamedElement>,
        typeSpec: VlangNamedElement,
        interfaceType: VlangInterfaceType,
        methods: Collection<VlangInterfaceMethodDefinition>,
        fields: List<VlangFieldDefinition>,
        canBeImplementedOutsidePackage: Boolean,
        visitedSpecs: MutableSet<VlangNamedElement>,
    ) {
        val searchScope = GlobalSearchScope.allScope(typeSpec.project)
        val project = typeSpec.project
        val condition = Condition { spec: VlangNamedElement ->
            val struct = (spec as? VlangStructDeclaration)?.structType ?: return@Condition false

            checkImplementsInterface(interfaceType, struct, typeSpec)
        }
        searchInContentFirst(project, searchScope) { scope ->
            if (methods.isNotEmpty()) {
                processMethodOwners(
                    project,
                    methods,
                    scope,
                    createCandidatesProcessor(processor, condition),
                    visitedSpecs
                )
            } else {
                processFieldOwners(
                    project,
                    fields,
                    scope,
                    createCandidatesProcessor(processor, condition),
                    visitedSpecs
                )
            }
        }
    }

    private fun checkImplementsInterface(
        interfaceType: VlangInterfaceType,
        type: VlangStructType,
        context: PsiElement?,
    ): Boolean {
        val methods = interfaceType.methodList
        val fields = interfaceType.getFieldList()

        val structName = (type.parent as? VlangStructDeclaration)?.getQualifiedName() ?: return false
        val structMethods = VlangNamesIndex.getAllPrefix("$structName.", type.project).filter {
            VlangMethodFingerprintIndex.find(it.name ?: "", type.project, null).isNotEmpty()
        }.mapNotNull { it as? VlangMethodDeclaration }

        val structFields = type.getFieldList()

        val structMethodsSet = structMethods.associateBy { it.name }
        methods.forEach {
            if (it.name != null && !structMethodsSet.contains(it.name)) {
                return false
            }
        }

        val structFieldsSet = structFields.associateBy { it.name }
        fields.forEach {
            if (it.name != null && !structFieldsSet.contains(it.name)) {
                return false
            }
        }

        val fieldsIsEqual = fields.all { interfaceField ->
            val field = structFieldsSet[interfaceField.name] ?: return@all false
            val fieldType = field.getType(null).toEx()
            val interfaceFieldType = interfaceField.getType(null).toEx()

            fieldType.isEqual(interfaceFieldType)
        }

        if (!fieldsIsEqual) {
            return false
        }

        return methods.all { interfaceMethod ->
            val structMethod = structMethodsSet[interfaceMethod.name!!] ?: return@all true
            val structSignature = structMethod.getSignature() ?: return@all false
            val interfaceSignature = interfaceMethod.getSignature()

            val interfaceTypeEx = VlangFunctionTypeEx(null, interfaceSignature)
            val structTypeEx = VlangFunctionTypeEx(null, structSignature)

            interfaceTypeEx.isEqual(structTypeEx)
        }
    }

    private fun searchInContentFirst(
        project: Project,
        scope: GlobalSearchScope,
        searcher: Processor<in GlobalSearchScope>,
    ) {
        val projectContentScope = ProjectScope.getContentScope(project)
        if (!searcher.process(scope.intersectWith(projectContentScope))) {
            return
        }
        searcher.process(scope.intersectWith(GlobalSearchScope.notScope(projectContentScope)))
    }

    private fun processMethodOwners(
        project: Project,
        methods: Collection<VlangInterfaceMethodDefinition>,
        scope: GlobalSearchScope,
        processor: Processor<in VlangNamedElement>,
        visitedSpecs: MutableSet<VlangNamedElement>,
    ): Boolean {
        for (method in JBIterable.from(methods).filter { obj -> obj.isValid }) {
            if (!processMethodStructs(project, scope, processor, visitedSpecs, method)) {
                return false
            }
        }
        return true
    }

    private fun processFieldOwners(
        project: Project,
        fields: Collection<VlangFieldDefinition>,
        scope: GlobalSearchScope,
        processor: Processor<in VlangNamedElement>,
        visitedSpecs: MutableSet<VlangNamedElement>,
    ): Boolean {
        for (field in JBIterable.from(fields).filter { obj -> obj.isValid }) {
            if (!processFieldStructs(project, scope, processor, visitedSpecs, field)) {
                return false
            }
        }
        return true
    }

    private fun processMethodStructs(
        project: Project,
        scope: GlobalSearchScope,
        processor: Processor<in VlangNamedElement>,
        visitedStructs: MutableSet<VlangNamedElement>,
        method: VlangInterfaceMethodDefinition,
    ): Boolean {
        val fingerprint = method.name ?: return true

        return VlangMethodFingerprintIndex.process(fingerprint, project, null) { m ->
            ProgressManager.checkCanceled()

            val structType = m.receiverType.resolveType() as? VlangStructType ?: return@process true
            val structDecl = structType.parent as? VlangStructDeclaration
            structDecl == null || (!visitedStructs.add(structDecl)) || processor.process(structDecl)
        }
    }

    private fun processFieldStructs(
        project: Project,
        scope: GlobalSearchScope,
        processor: Processor<in VlangNamedElement>,
        visitedStructs: MutableSet<VlangNamedElement>,
        field: VlangFieldDefinition,
    ): Boolean {
        val fingerprint = field.name ?: return true

        return VlangFieldFingerprintIndex.process(fingerprint, project, null) { field ->
            ProgressManager.checkCanceled()

            val structType = field.parentOfType<VlangStructType>() ?: return@process true
            val structDecl = structType.parent as? VlangStructDeclaration
            structDecl == null || (!visitedStructs.add(structDecl)) || processor.process(structDecl)
        }
    }

    private fun createCandidatesProcessor(
        processor: Processor<in VlangNamedElement>,
        condition: Condition<VlangNamedElement?>,
    ): Processor<VlangNamedElement?> {
        val visitedSpecs = ReferenceOpenHashSet<VlangNamedElement>()

        return Processor<VlangNamedElement?> { candidate ->
            ProgressManager.checkCanceled()
            if (!visitedSpecs.add(candidate)) return@Processor true
            if (condition.value(candidate) && !processor.process(candidate)) return@Processor false

            true
        }
    }
}
