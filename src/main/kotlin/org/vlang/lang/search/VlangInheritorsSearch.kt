package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import com.intellij.util.containers.JBIterable
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import org.vlang.lang.psi.types.VlangFunctionTypeEx
import org.vlang.lang.psi.types.VlangStructTypeEx
import org.vlang.lang.stubs.index.VlangFieldFingerprintIndex
import org.vlang.lang.stubs.index.VlangMethodFingerprintIndex

object VlangInheritorsSearch : QueryExecutorBase<VlangNamedElement, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(queryParameters: DefinitionsScopedSearch.SearchParameters, processor: Processor<in VlangNamedElement>) {
        if (!queryParameters.isCheckDeep) return

        val element = queryParameters.element as? VlangInterfaceDeclaration ?: return

        val visitedSpecs = ReferenceOpenHashSet<VlangNamedElement>()
        val interfaceType = element.interfaceType
        val ownMethods = interfaceType.methodList
        val ownFields = interfaceType.fieldList

        processMethodOwners(
            processor,
            element,
            interfaceType,
            ownMethods,
            ownFields,
            visitedSpecs
        )
    }

    fun processMethodOwners(
        processor: Processor<in VlangNamedElement>,
        typeSpec: VlangNamedElement,
        interfaceType: VlangInterfaceType,
        methods: Collection<VlangInterfaceMethodDefinition>,
        fields: List<VlangFieldDefinition>,
    ) {
        processMethodOwners(
            processor,
            typeSpec,
            interfaceType,
            methods,
            fields,
            ReferenceOpenHashSet()
        )
    }

    private fun processMethodOwners(
        processor: Processor<in VlangNamedElement>,
        typeSpec: VlangNamedElement,
        interfaceType: VlangInterfaceType,
        methods: Collection<VlangInterfaceMethodDefinition>,
        fields: List<VlangFieldDefinition>,
        visitedSpecs: MutableSet<VlangNamedElement>,
    ) {
        val searchScope = GlobalSearchScope.allScope(typeSpec.project)
        val project = typeSpec.project
        val condition = Condition { spec: VlangNamedElement ->
            val struct = (spec as? VlangStructDeclaration)?.structType ?: return@Condition false

            checkImplementsInterface(typeSpec.project, interfaceType, struct)
        }

        val candidateProcessor = VlangGotoUtil.createCandidatesProcessor(processor, condition)

        VlangGotoUtil.searchInContentFirst(project, searchScope) {
            if (methods.isNotEmpty()) {
                processMethodOwners(
                    project,
                    methods,
                    candidateProcessor,
                    visitedSpecs
                )
            } else {
                processFieldOwners(
                    project,
                    fields,
                    candidateProcessor,
                    visitedSpecs
                )
            }
        }
    }

    private fun processMethodOwners(
        project: Project,
        methods: Collection<VlangInterfaceMethodDefinition>,
        processor: Processor<in VlangNamedElement>,
        visitedSpecs: MutableSet<VlangNamedElement>,
    ): Boolean {
        for (method in JBIterable.from(methods).filter { obj -> obj.isValid }) {
            if (!processMethodStructs(project, processor, visitedSpecs, method)) {
                return false
            }
        }
        return true
    }

    private fun processFieldOwners(
        project: Project,
        fields: Collection<VlangFieldDefinition>,
        processor: Processor<in VlangNamedElement>,
        visitedSpecs: MutableSet<VlangNamedElement>,
    ): Boolean {
        for (field in JBIterable.from(fields).filter { obj -> obj.isValid }) {
            if (!processFieldStructs(project, processor, visitedSpecs, field)) {
                return false
            }
        }
        return true
    }

    private fun processMethodStructs(
        project: Project,
        processor: Processor<in VlangNamedElement>,
        visitedStructs: MutableSet<VlangNamedElement>,
        method: VlangInterfaceMethodDefinition,
    ): Boolean {
        val fingerprint = method.name ?: return true

        return VlangMethodFingerprintIndex.process(fingerprint, project, null) { methodSpec ->
            ProgressManager.checkCanceled()

            val receiverType = methodSpec.receiverType.toEx().unwrapPointer()
            if (receiverType !is VlangStructTypeEx) return@process true
            val structDecl = receiverType.resolve(project)
            structDecl == null || (!visitedStructs.add(structDecl)) || processor.process(structDecl)
        }
    }

    private fun processFieldStructs(
        project: Project,
        processor: Processor<in VlangNamedElement>,
        visitedStructs: MutableSet<VlangNamedElement>,
        field: VlangFieldDefinition,
    ): Boolean {
        val fingerprint = field.name ?: return true

        return VlangFieldFingerprintIndex.process(fingerprint, project, null) { fieldSpec ->
            ProgressManager.checkCanceled()

            val structDecl = fieldSpec.getOwner()
            !visitedStructs.add(structDecl) || processor.process(structDecl)
        }
    }

    fun checkImplementsInterface(
        project: Project,
        interfaceType: VlangInterfaceType,
        type: VlangStructType,
    ): Boolean {
        val methods = interfaceType.methodList
        val fields = interfaceType.fieldList
        val structMethods = type.toEx().methodsList(project)

        val structMethodsSet = structMethods.associateBy { it.name }
        methods.forEach {
            if (it.name != null && !structMethodsSet.contains(it.name)) {
                return false
            }
        }

        val structFields = type.fieldList
        val structFieldsSet = structFields.associateBy { it.name }
        fields.forEach {
            if (it.name != null && !structFieldsSet.contains(it.name)) {
                return false
            }
        }

        val fieldsIsEqual = fields.all { interfaceField ->
            val field = structFieldsSet[interfaceField.name] ?: return@all false
            val fieldType = field.getType(null) ?: return@all false
            val interfaceFieldType = interfaceField.getType(null) ?: return@all false

            fieldType.isEqual(interfaceFieldType)
        }

        if (!fieldsIsEqual) {
            return false
        }

        return methods.all { interfaceMethod ->
            val structMethod = structMethodsSet[interfaceMethod.name!!] ?: return@all true

            val interfaceTypeEx = VlangFunctionTypeEx.from(interfaceMethod) ?: return@all false
            val structTypeEx = VlangFunctionTypeEx.from(structMethod) ?: return@all false

            interfaceTypeEx.isEqual(structTypeEx)
        }
    }
}
