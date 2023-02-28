package org.vlang.lang.annotator.checkers

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangStructTypeEx

class VlangCommonProblemsChecker(holder: AnnotationHolder) : VlangCheckerBase(holder) {
    override fun visitContinueStatement(stmt: VlangContinueStatement) {
        val owner = VlangPsiImplUtil.getContinueStatementOwner(stmt)
        if (owner == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "'continue' statement is outside the 'for' loop")
                .create()
        }
    }

    override fun visitBreakStatement(o: VlangBreakStatement) {
        val owner = VlangPsiImplUtil.getBreakStatementOwner(o)
        if (owner == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "'break' statement is outside the 'for' loop")
                .create()
        }
    }

    override fun visitFieldDeclaration(decl: VlangFieldDeclaration) {
        val owner = decl.parentOfType<VlangNamedElement>()
        if (owner is VlangStructDeclaration) {
            val type = decl.type?.toEx() ?: return
            if (type is VlangStructTypeEx && type.anchor() == owner.structType) {
                holder.newAnnotation(HighlightSeverity.ERROR, "Invalid recursive type: ${owner.name} refers to itself, try change the type to '&${owner.name}'")
                    .create()
            }
        }

        if (owner is VlangInterfaceDeclaration) {
            val defaultValue = decl.defaultFieldValue
            if (defaultValue != null) {
                holder.newAnnotation(HighlightSeverity.ERROR, "Interface field cannot have a default value")
                    .range(defaultValue)
                    .create()
            }
        }
    }
}
