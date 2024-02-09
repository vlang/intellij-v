package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import com.intellij.util.containers.JBIterable
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.stubs.index.VlangInterfaceFieldFingerprintIndex
import org.vlang.lang.stubs.index.VlangInterfaceInheritorsIndex
import org.vlang.lang.stubs.index.VlangInterfaceMethodFingerprintIndex
import org.vlang.utils.isStubFile
import org.vlang.utils.isTestFile

object VlangSuperSearch : QueryExecutorBase<VlangNamedElement, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(parameter: DefinitionsScopedSearch.SearchParameters, processor: Processor<in VlangNamedElement>) {
        val element = parameter.element
        if (element !is VlangStructDeclaration || !element.isValid()) return
        val visitedInterfaces = ReferenceOpenHashSet<VlangInterfaceDeclaration>()
        val ownMethods = element.structType.toEx().methodsList(element.project)
        processMethodOwners(processor, element, ownMethods, visitedInterfaces)
        val ownFields = element.structType.fieldList
        processFieldOwners(processor, element, ownFields, visitedInterfaces)
    }

    fun processMethodOwners(
        processor: Processor<in VlangNamedElement>,
        struct: VlangStructDeclaration,
        methods: Collection<VlangMethodDeclaration>,
    ) {
        processMethodOwners(processor, struct, methods, ReferenceOpenHashSet())
    }

    private fun processMethodOwners(
        processor: Processor<in VlangNamedElement>,
        struct: VlangStructDeclaration,
        methods: Collection<VlangMethodDeclaration>,
        visitedInterfaces: MutableSet<VlangInterfaceDeclaration>,
    ) {
        val project = struct.project
        val searchScope = GlobalSearchScope.allScope(project)
        val candidatesProcessor = createCandidatesProcessor(processor, struct)

        VlangGotoUtil.searchInContentFirst(project, searchScope) {
            processMethodInterfaces(project, methods, candidatesProcessor, visitedInterfaces)
        }
    }

    private fun processMethodInterfaces(
        project: Project,
        methods: Collection<VlangMethodDeclaration>,
        processor: Processor<in VlangNamedElement>,
        visitedSpecs: MutableSet<VlangInterfaceDeclaration>,
    ): Boolean {
        for (method in JBIterable.from(methods).filter { obj -> obj.isValid }) {
            if (!processMethodInterfaces(project, processor, visitedSpecs, method)) {
                return false
            }
        }
        return true
    }

    private fun processMethodInterfaces(
        project: Project,
        processor: Processor<in VlangNamedElement>,
        visitedInterfaces: MutableSet<VlangInterfaceDeclaration>,
        method: VlangMethodDeclaration,
    ): Boolean {
        val fingerprint = method.name ?: return true
        val containingFile = method.containingFile

        return VlangInterfaceMethodFingerprintIndex.process(fingerprint, project, null) { methodDefinition ->
            ProgressManager.checkCanceled()
            if (!needProcess(containingFile, methodDefinition)) return@process true

            val interfaceDeclaration = methodDefinition.getOwner()
            val interfaceName = interfaceDeclaration.name
            val interfaceWithEmbeddedThisInterface = VlangInterfaceInheritorsIndex.find(interfaceName, project, null)

            interfaceWithEmbeddedThisInterface.any {
                !visitedInterfaces.add(it) || processor.process(it)
            }

            !visitedInterfaces.add(interfaceDeclaration) || processor.process(interfaceDeclaration)
        }
    }

    fun processFieldOwners(
        processor: Processor<in VlangNamedElement>,
        struct: VlangStructDeclaration,
        fields: Collection<VlangFieldDefinition>,
    ) {
        processFieldOwners(processor, struct, fields, ReferenceOpenHashSet())
    }

    private fun processFieldOwners(
        processor: Processor<in VlangNamedElement>,
        struct: VlangStructDeclaration,
        fields: Collection<VlangFieldDefinition>,
        visitedInterfaces: MutableSet<VlangInterfaceDeclaration>,
    ) {
        val project = struct.project
        val searchScope = GlobalSearchScope.allScope(project)
        val candidatesProcessor = createCandidatesProcessor(processor, struct)

        VlangGotoUtil.searchInContentFirst(project, searchScope) {
            processFieldInterfaces(project, fields, candidatesProcessor, visitedInterfaces)
        }
    }

    private fun processFieldInterfaces(
        project: Project,
        fields: Collection<VlangFieldDefinition>,
        processor: Processor<in VlangNamedElement>,
        visitedInterfaces: MutableSet<VlangInterfaceDeclaration>,
    ): Boolean {
        for (field in JBIterable.from(fields).filter { obj -> obj.isValid }) {
            if (!processFieldInterfaces(project, processor, visitedInterfaces, field)) {
                return false
            }
        }
        return true
    }

    private fun processFieldInterfaces(
        project: Project,
        processor: Processor<in VlangNamedElement>,
        visitedInterfaces: MutableSet<VlangInterfaceDeclaration>,
        field: VlangFieldDefinition,
    ): Boolean {
        val fingerprint = field.name ?: return true
        val containingFile = field.containingFile

        return VlangInterfaceFieldFingerprintIndex.process(fingerprint, project, null) { fieldDefinition ->
            ProgressManager.checkCanceled()
            if (!needProcess(containingFile, fieldDefinition)) return@process true
            val interfaceDeclaration = fieldDefinition.getOwner() as VlangInterfaceDeclaration

            (!visitedInterfaces.add(interfaceDeclaration)) || processor.process(interfaceDeclaration)
        }
    }

    private fun needProcess(contextFile: PsiFile, elementToProcess: PsiElement): Boolean {
        val containingFile = elementToProcess.containingFile
        if (containingFile == contextFile) {
            // allow to process elements in the same file
            // even if test or stub file
            return true
        }

        val virtualFile = containingFile.virtualFile ?: containingFile.originalFile.virtualFile ?: return false
        if (virtualFile.isTestFile || virtualFile.isStubFile) {
            // do not process test or stub files
            return false
        }

        return true
    }

    private fun createCandidatesProcessor(
        processor: Processor<in VlangNamedElement>,
        struct: VlangStructDeclaration,
    ): Processor<VlangNamedElement?> {
        val condition = condition@{ interfaceDecl: VlangNamedElement ->
            val interfaceType = (interfaceDecl as? VlangInterfaceDeclaration)?.interfaceType ?: return@condition false
            val structType = struct.structType

            VlangInheritorsSearch.checkImplementsInterface(struct.project, interfaceType, structType)
        }

        return VlangGotoUtil.createCandidatesProcessor(processor, condition)
    }

}
