package org.vlang.ide.codeInsight.imports

import com.intellij.lang.ImportOptimizer
import com.intellij.psi.PsiFile
import org.vlang.lang.psi.VlangFile

class VlangImportOptimizer : ImportOptimizer {
    //    override fun supports(file: PsiFile) = file is VlangFile
//
//    override fun processFile(file: PsiFile): Runnable {
//        if (file !is VlangFile) {
//            return EmptyRunnable.getInstance()
//        }
//
//        val importMap = file.getImportMap()
//        val importEntriesToDelete = mutableSetOf<PsiElement>()
//        val importIdentifiersToDelete = findRedundantImportIdentifiers(importMap)
//        importEntriesToDelete.addAll(findDuplicatedEntries(importMap))
//        importEntriesToDelete.addAll(filterUnusedImports(file, importMap).values)
//
//        if (importEntriesToDelete.isEmpty() && importIdentifiersToDelete.isEmpty()) {
//            return EmptyRunnable.getInstance()
//        }
//
//        return object : CollectingInfoRunnable {
//            override fun getUserNotificationInfo(): String? {
//                val entriesToDelete = importEntriesToDelete.size
//                val identifiersToDelete = importIdentifiersToDelete.size
//                var result = ""
//                if (entriesToDelete > 0) {
//                    result = "Removed " + entriesToDelete + " import" + if (entriesToDelete > 1) "s" else ""
//                }
//                if (identifiersToDelete > 0) {
//                    result += if (result.isEmpty()) "Removed " else " and "
//                    result += identifiersToDelete.toString() + " alias" + if (identifiersToDelete > 1) "es" else ""
//                }
//                return StringUtil.nullize(result)
//            }
//
//            override fun run() {
//                if (importEntriesToDelete.isNotEmpty() || importIdentifiersToDelete.isNotEmpty()) {
//                    val manager = PsiDocumentManager.getInstance(file.project)
//                    val document = manager.getDocument(file)
//                    if (document != null) {
//                        manager.commitDocument(document)
//                    }
//                }
//                for (importEntry in importEntriesToDelete) {
//                    if (importEntry.isValid) {
//                        deleteImportSpec(getImportSpec(importEntry))
//                    }
//                }
//                for (identifier in importIdentifiersToDelete) {
//                    if (identifier.isValid) {
//                        identifier.delete()
//                    }
//                }
//            }
//        }
//    }
//
//    companion object {
//        fun findRedundantImportIdentifiers(importMap: Map<String, VlangImportSpec>): Set<PsiElement> {
//            val importIdentifiersToDelete = mutableSetOf<PsiElement>()
//            for (importEntry in importMap.values()) {
//                val importSpec = getImportSpec(importEntry)
//                if (importSpec != null) {
//                    val localPackageName: String = importSpec.getLocalPackageName()
//                    if (!StringUtil.isEmpty(localPackageName)) {
//                        if (Comparing.equal(importSpec.getAlias(), localPackageName)) {
//                            importIdentifiersToDelete.add(importSpec.identifier)
//                        }
//                    }
//                }
//            }
//            return importIdentifiersToDelete
//        }
//
//        fun filterUnusedImports(
//            file: PsiFile,
//            importMap: Map<String, VlangImportSpec>,
//        ): Map<String, VlangImportSpec> {
//            val result = mutableMapOf<String, VlangImportSpec>()
//            result.putAll(importMap)
//
//            for (importEntry in implicitImports) {
//                val spec = getImportSpec(importEntry)
//                if (spec != null && spec.isDot() && hasImportUsers(spec)) {
//                    result.remove(".", importEntry)
//                }
//            }
//            file.accept(object : VlangRecursiveVisitor() {
//                override fun visitTypeReferenceExpression(o: VlangTypeReferenceExpression) {
//                    var lastQualifier: VlangTypeReferenceExpression? = o.getQualifier()
//                    if (lastQualifier != null) {
//                        var previousQualifier: VlangTypeReferenceExpression?
//                        while (lastQualifier!!.getQualifier().also { previousQualifier = it } != null) {
//                            lastQualifier = previousQualifier
//                        }
//                        markAsUsed(lastQualifier.getIdentifier(), lastQualifier.reference)
//                    }
//                }
//
//                override fun visitReferenceExpression(o: VlangReferenceExpression) {
//                    var lastQualifier: VlangReferenceExpression? = o.getQualifier()
//                    if (lastQualifier != null) {
//                        var previousQualifier: VlangReferenceExpression
//                        while (lastQualifier!!.getQualifier().also { previousQualifier = it } != null) {
//                            lastQualifier = previousQualifier
//                        }
//                        markAsUsed(lastQualifier!!.getIdentifier(), lastQualifier!!.reference)
//                    }
//                }
//
//                private fun markAsUsed(qualifier: PsiElement, reference: PsiReference) {
//                    val qualifierText = qualifier.text
//                    if (!result.containsKey(qualifierText)) {
//                        // already marked
//                        return
//                    }
//                    val resolve = reference.resolve()
//                    if (!(resolve is PsiDirectory || resolve is VlangImportSpec || resolve is PsiDirectoryContainer)) {
//                        return
//                    }
//                    val qualifiersToDelete: MutableCollection<String> = ContainerUtil.newHashSet()
//                    for (spec in result[qualifierText]) {
//                        for ((key, value) in result.entrySet()) {
//                            for (importSpec in value) {
//                                if (importSpec === spec) {
//                                    qualifiersToDelete.add(key)
//                                }
//                            }
//                        }
//                    }
//                    for (qualifierToDelete in qualifiersToDelete) {
//                        result.remove(qualifierToDelete)
//                    }
//                }
//            })
//            return result
//        }
//
//        private fun hasImportUsers(spec: VlangImportSpec): Boolean {
//            synchronized(spec) {
//                val list = spec.getUserData(VlangReferenceBase.IMPORT_USERS)
//                if (list != null) {
//                    for (e in list) {
//                        if (e.isValid) {
//                            return true
//                        }
//                        ProgressManager.checkCanceled()
//                    }
//                }
//            }
//            return false
//        }
//
//        fun findDuplicatedEntries(importMap: Map<String, VlangImportSpec>): Set<VlangImportSpec> {
//            val duplicatedEntries: MutableSet<VlangImportSpec> = ContainerUtil.newLinkedHashSet()
//            for ((_, importsWithSameName) in importMap.entrySet()) {
//                if (importsWithSameName.size > 1) {
//                    val importsWithSameString = collectImportsWithSameString(importsWithSameName)
//                    for ((_, value) in importsWithSameString.entrySet()) {
//                        val duplicates: List<VlangImportSpec> = ContainerUtil.newArrayList(
//                            value
//                        )
//                        if (duplicates.size > 1) {
//                            duplicatedEntries.addAll(duplicates.subList(1, duplicates.size))
//                        }
//                    }
//                }
//            }
//            return duplicatedEntries
//        }
//
//        private fun deleteImportSpec(importSpec: VlangImportSpec?) {
//            val importDeclaration = PsiTreeUtil.getParentOfType(importSpec, VlangImportDeclaration::class.java)
//            if (importSpec != null && importDeclaration != null) {
//                var startElementToDelete: PsiElement = importSpec
//                var endElementToDelete: PsiElement = importSpec
//                if (importDeclaration.getImportSpecList().size() === 1) {
//                    startElementToDelete = importDeclaration
//                    endElementToDelete = importDeclaration
//                    val nextSibling = endElementToDelete.getNextSibling()
//                    if (nextSibling != null && nextSibling.node.elementType === VlangTypes.SEMICOLON) {
//                        endElementToDelete = nextSibling
//                    }
//                }
//                // todo: delete after proper formatter implementation
//                val nextSibling = endElementToDelete.nextSibling
//                if (nextSibling is PsiWhiteSpace && nextSibling.textContains('\n')) {
//                    endElementToDelete = nextSibling
//                }
//                startElementToDelete.parent.deleteChildRange(startElementToDelete, endElementToDelete)
//            }
//        }
//
//        private fun collectImportsWithSameString(importsWithSameName: Collection<VlangImportSpec>): Map<String, VlangImportSpec> {
//            val importsWithSameString = Map.create<String, VlangImportSpec>()
//            for (duplicateCandidate in importsWithSameName) {
//                val importSpec = getImportSpec(duplicateCandidate)
//                if (importSpec != null) {
//                    importsWithSameString.putValue(importSpec.getPath(), importSpec)
//                }
//            }
//            return importsWithSameString
//        }
//
//        fun getImportSpec(importEntry: PsiElement): VlangImportSpec? {
//            return PsiTreeUtil.getNonStrictParentOfType(importEntry, VlangImportSpec::class.java)
//        }
//    }
    override fun supports(file: PsiFile): Boolean {
        return file is VlangFile
    }

    override fun processFile(file: PsiFile): Runnable {
        return Runnable {}
    }
}
