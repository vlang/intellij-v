package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import com.intellij.util.containers.JBIterable
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.stubs.index.VlangInterfaceFieldFingerprintIndex
import org.vlang.lang.stubs.index.VlangInterfaceInheritorsIndex
import org.vlang.lang.stubs.index.VlangInterfaceMethodFingerprintIndex

class VlangSuperSearch : QueryExecutorBase<VlangNamedElement, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(parameter: DefinitionsScopedSearch.SearchParameters, processor: Processor<in VlangNamedElement>) {
        val element = parameter.element
        if (element !is VlangStructDeclaration || !element.isValid()) return
        val type = element.getType(null) ?: return
        val visitedInterfaces = ReferenceOpenHashSet<VlangInterfaceDeclaration>()
        val ownMethods = VlangLangUtil.getMethodList(element.project, type)
        processMethodOwners(processor, element, ownMethods, visitedInterfaces)
    }

    companion object {
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

            return VlangInterfaceMethodFingerprintIndex.process(fingerprint, project, null) { methodDefinition ->
                ProgressManager.checkCanceled()
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

            VlangGotoUtil.searchInContentFirst(project, searchScope) { scope ->
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

            return VlangInterfaceFieldFingerprintIndex.process(fingerprint, project, null) { fieldDefinition ->
                ProgressManager.checkCanceled()
                val interfaceDeclaration = fieldDefinition.getOwner() as VlangInterfaceDeclaration
                (!visitedInterfaces.add(interfaceDeclaration)) || processor.process(interfaceDeclaration)
            }
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
}
