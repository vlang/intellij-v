package org.vlang.ide.codeInsight.imports

import com.intellij.lang.ImportOptimizer
import com.intellij.openapi.util.EmptyRunnable
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangImportSpec

class VlangImportOptimizer : ImportOptimizer {
    override fun supports(file: PsiFile) = file is VlangFile

    override fun processFile(file: PsiFile): Runnable {
        if (file !is VlangFile) {
            return EmptyRunnable.getInstance()
        }

        val imports = file.getImports()
        val importsToDelete = VlangCodeInsightUtil.findDuplicateImports(imports)
        importsToDelete += collectUnusedImports(imports)

        return Runnable {
            if (importsToDelete.isNotEmpty()) {
                val manager = PsiDocumentManager.getInstance(file.project)
                val document = manager.getDocument(file)
                if (document != null) {
                    manager.commitDocument(document)
                }
            }
            for (importEntry in importsToDelete) {
                if (importEntry.isValid) {
                    deleteImportSpec(importEntry)
                }
            }
        }
    }

    private fun collectUnusedImports(imports: List<VlangImportSpec>): MutableSet<VlangImportSpec> {
        val importsToDelete = mutableSetOf<VlangImportSpec>()

        imports.forEach { spec ->
            if (!VlangCodeInsightUtil.isImportUsed(spec)) {
                importsToDelete.add(spec)
            }
        }

        return importsToDelete
    }

    private fun deleteImportSpec(importSpec: VlangImportSpec?) {
        val importDeclaration = importSpec?.parent ?: return
        importDeclaration.delete()
    }
}
