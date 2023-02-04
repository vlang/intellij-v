package org.vlang.lang.psi.impl.imports

import com.intellij.lang.ImportOptimizer
import com.intellij.openapi.util.EmptyRunnable
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*

class VlangImportOptimizer : ImportOptimizer {
    override fun supports(file: PsiFile) = file is VlangFile

    override fun processFile(file: PsiFile): Runnable {
        if (file !is VlangFile) {
            return EmptyRunnable.getInstance()
        }

        val imports = file.getImports()
        val importsToDelete = VlangCodeInsightUtil.findDuplicateImports(imports)
        val unusedImports = collectUnusedImports(file, imports)
        val aliasesToDelete = unusedImports.unusedAliases
        val selectiveImportSymbolsToDelete = unusedImports.unusedSelectiveImportSymbol
        importsToDelete += unusedImports.unusedImports

        return Runnable {
            if (importsToDelete.isNotEmpty() || aliasesToDelete.isNotEmpty()) {
                val manager = PsiDocumentManager.getInstance(file.project)
                val document = manager.getDocument(file)
                if (document != null) {
                    manager.commitDocument(document)
                }
            }
            for (importEntry in importsToDelete) {
                if (!importEntry.isValid) continue
                deleteImportSpec(importEntry)
            }

            for (alias in aliasesToDelete) {
                if (!alias.isValid) continue
                deleteImportAlias(alias)
            }

            for (selectiveImportSymbol in selectiveImportSymbolsToDelete) {
                if (!selectiveImportSymbol.isValid) continue
                deleteSelectiveImportSymbol(selectiveImportSymbol)
            }
        }
    }

    private fun deleteSelectiveImportSymbol(selectiveImportSymbol: VlangReferenceExpressionBase) {
        val prevLeaf = PsiTreeUtil.prevVisibleLeaf(selectiveImportSymbol)
        if (prevLeaf != null && prevLeaf.elementType == VlangTypes.COMMA) {
            prevLeaf.delete()
            val prevSpace = PsiTreeUtil.prevLeaf(selectiveImportSymbol)
            if (prevSpace != null && prevSpace is PsiWhiteSpace) {
                prevSpace.delete()
            }
        }

        if (prevLeaf == null || prevLeaf.elementType != VlangTypes.COMMA) {
            val nextLeaf = PsiTreeUtil.nextVisibleLeaf(selectiveImportSymbol)
            if (nextLeaf != null && nextLeaf.elementType == VlangTypes.COMMA) {
                val nextSpace = PsiTreeUtil.nextLeaf(nextLeaf)
                if (nextSpace != null && nextSpace is PsiWhiteSpace) {
                    nextSpace.delete()
                }
                nextLeaf.delete()
            }
        }
        selectiveImportSymbol.delete()
    }

    private fun deleteImportSpec(importSpec: VlangImportSpec?) {
        val importDeclaration = importSpec?.parent ?: return
        importDeclaration.delete()
    }

    private fun deleteImportAlias(alias: VlangImportAlias?) {
        alias?.delete()
    }

    companion object {
        data class UnusedImports(
            val unusedImports: MutableSet<VlangImportSpec>,
            val unusedAliases: MutableSet<VlangImportAlias>,
            val unusedSelectiveImportSymbol: MutableSet<VlangReferenceExpressionBase>,
        )

        fun collectUnusedImports(file: VlangFile, imports: List<VlangImportSpec>): UnusedImports {
            val importsToDelete = mutableSetOf<VlangImportSpec>()
            val unusedAliases = mutableSetOf<VlangImportAlias>()
            val usedImports = mutableMapOf<String, VlangImportSpec>()

            val selectiveImportSymbols = imports
                .mapNotNull { it.selectiveImportList?.referenceExpressionList }
                .flatten()
                .associateBy { it.text }
            val usedSelectiveImportSymbols = mutableMapOf<String, VlangReferenceExpressionBase>()
            val unusedSelectiveImportSymbols = mutableSetOf<VlangReferenceExpressionBase>()

            file.accept(object : VlangRecursiveElementVisitor() {
                override fun visitReferenceExpression(o: VlangReferenceExpression) {
                    super.visitReferenceExpression(o)
                    if (o.parent is VlangSelectiveImportList) return
                    checkReferenceExpression(o)
                }

                override fun visitTypeReferenceExpression(o: VlangTypeReferenceExpression) {
                    super.visitTypeReferenceExpression(o)
                    checkReferenceExpression(o)
                }

                private fun checkReferenceExpression(o: VlangReferenceExpressionBase) {
                    var qualifier = (o.getQualifier() ?: o) as? VlangReferenceExpressionBase
                    if (qualifier != null) {
                        var prevQualifier: VlangReferenceExpressionBase?
                        while (true) {
                            prevQualifier = qualifier?.getQualifier() as? VlangReferenceExpressionBase
                            if (prevQualifier == null) {
                                break
                            }

                            qualifier = prevQualifier
                        }

                        val name = qualifier?.getIdentifier()?.text ?: return
                        if (usedImports.contains(name)) {
                            return
                        }

                        val importSpec = imports.firstOrNull {
                            it.pathName == name || it.aliasName == name
                        }

                        if (importSpec != null) {
                            usedImports[name] = importSpec
                        }

                        if (selectiveImportSymbols.contains(name)) {
                            usedSelectiveImportSymbols[name] = qualifier
                        }
                    }

                    if (qualifier == o) {
                        val resolved = o.resolve()

                        // globals are implicit imports
                        if (resolved is VlangGlobalVariableDefinition) {
                            val moduleName = (resolved.containingFile as? VlangFile)?.getModuleQualifiedName() ?: return

                            val importSpec = imports.firstOrNull {
                                it.pathName == moduleName
                            }

                            if (importSpec != null) {
                                usedImports[moduleName] = importSpec
                            }
                        }
                    }
                }
            })

            selectiveImportSymbols.forEach { (name, symbol) ->
                if (!usedSelectiveImportSymbols.contains(name)) {
                    unusedSelectiveImportSymbols.add(symbol)
                }
            }

            imports.forEach { spec ->
                if (!usedImports.contains(spec.pathName) && !usedImports.contains(spec.aliasName)) {
                    if (spec.selectiveImportList != null) {
                        val list = spec.selectiveImportList!!.referenceExpressionList
                        val allUnused = list.all { unusedSelectiveImportSymbols.contains(it) }
                        if (!allUnused) {
                            return@forEach
                        }
                    }

                    importsToDelete.add(spec)
                }

                if (spec.importAlias != null) {
                    val aliasName = spec.importAlias?.name
                    if (!usedImports.contains(aliasName)) {
                        unusedAliases.add(spec.importAlias!!)
                    }
                }

            }

            return UnusedImports(importsToDelete, unusedAliases, unusedSelectiveImportSymbols)
        }
    }
}
