package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Conditions
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.psi.util.parentOfType
import com.intellij.util.Processor
import com.intellij.util.containers.JBIterable
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.stubs.index.VlangInterfaceFieldFingerprintIndex
import org.vlang.lang.stubs.index.VlangInterfaceMethodFingerprintIndex

class VlangSuperSearch : QueryExecutorBase<VlangNamedElement, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(parameter: DefinitionsScopedSearch.SearchParameters, processor: Processor<in VlangNamedElement>) {
        val element = parameter.element
        if (element !is VlangNamedElement || !element.isValid()) return
        val typeSpec = element.getType(null) ?: return
        val visitedSpecs = ReferenceOpenHashSet<VlangNamedElement>()
        val ownMethods = VlangLangUtil.getMethodList(element.project, typeSpec)
        processMethodOwners(processor, element, ownMethods, visitedSpecs)
    }

    companion object {
        fun processMethodOwners(
            processor: Processor<in VlangNamedElement>,
            typeSpec: VlangNamedElement,
            methods: Collection<VlangNamedElement>,
        ) {
            processMethodOwners(processor, typeSpec, methods, ReferenceOpenHashSet())
        }

        private fun processMethodOwners(
            processor: Processor<in VlangNamedElement>,
            typeSpec: VlangNamedElement,
            methods: Collection<VlangNamedElement>,
            visitedSpecs: MutableSet<VlangNamedElement>,
        ) {
            val project = typeSpec.project
            val searchScope = GlobalSearchScope.allScope(project)
            val candidatesProcessor = VlangGotoUtil.createCandidatesProcessor(processor, Conditions.alwaysTrue())
            VlangGotoUtil.searchInContentFirst(project, searchScope) {
                processMethodInterfaces(project, methods, candidatesProcessor, visitedSpecs)
            }
        }

        private fun processMethodInterfaces(
            project: Project,
            methods: Collection<VlangNamedElement>,
            processor: Processor<in VlangNamedElement>,
            visitedSpecs: MutableSet<VlangNamedElement>,
        ): Boolean {
            for (m in JBIterable.from(methods).filter { obj -> obj.isValid }) {
                if (!processMethodInterfaces(project, processor, visitedSpecs, m)) {
                    return false
                }
            }
            return true
        }

        private fun processMethodInterfaces(
            project: Project,
            processor: Processor<in VlangNamedElement>,
            visitedInterfaces: MutableSet<VlangNamedElement>,
            method: VlangNamedElement,
        ): Boolean {
            val fingerprint = method.name ?: return true
            return VlangInterfaceMethodFingerprintIndex.process(fingerprint, project, null) { methodSpec ->
                ProgressManager.checkCanceled()
                val iface = methodSpec.parentOfType<VlangInterfaceDeclaration>()
                iface == null || !visitedInterfaces.add(iface) || processor.process(iface)
            }
        }

        fun processFieldOwners(
            processor: Processor<in VlangNamedElement>,
            typeSpec: VlangNamedElement,
            fields: Collection<VlangNamedElement>,
        ) {
            processFieldOwners(processor, typeSpec, fields, ReferenceOpenHashSet())
        }

        private fun processFieldOwners(
            processor: Processor<in VlangNamedElement>,
            typeSpec: VlangNamedElement,
            fields: Collection<VlangNamedElement>,
            visitedSpecs: MutableSet<VlangNamedElement>,
        ) {
            val project = typeSpec.project
            val searchScope = GlobalSearchScope.allScope(project)
            val candidatesProcessor = VlangGotoUtil.createCandidatesProcessor(processor, Conditions.alwaysTrue())
            VlangGotoUtil.searchInContentFirst(project, searchScope) { scope ->
                processFieldInterfaces(project, fields, scope, candidatesProcessor, visitedSpecs)
            }
        }

        private fun processFieldInterfaces(
            project: Project,
            fields: Collection<VlangNamedElement>,
            scope: GlobalSearchScope,
            processor: Processor<in VlangNamedElement>,
            visitedSpecs: MutableSet<VlangNamedElement>,
        ): Boolean {
            for (m in JBIterable.from(fields).filter { obj -> obj.isValid }) {
                if (!processFieldInterfaces(project, scope, processor, visitedSpecs, m)) {
                    return false
                }
            }
            return true
        }

        private fun processFieldInterfaces(
            project: Project,
            scope: GlobalSearchScope,
            processor: Processor<in VlangNamedElement>,
            visitedInterfaces: MutableSet<VlangNamedElement>,
            field: VlangNamedElement,
        ): Boolean {
            val fingerprint = field.name ?: return true
            return VlangInterfaceFieldFingerprintIndex.process(fingerprint, project, null) { fieldSpec ->
                ProgressManager.checkCanceled()
                val iface = fieldSpec.parentOfType<VlangInterfaceDeclaration>()
                iface == null || !visitedInterfaces.add(iface) || processor.process(iface)
            }
        }
    }
}
