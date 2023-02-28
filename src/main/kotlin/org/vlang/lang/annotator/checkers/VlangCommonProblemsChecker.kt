package org.vlang.lang.annotator.checkers

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangChannelTypeEx
import org.vlang.lang.psi.types.VlangStructTypeEx

class VlangCommonProblemsChecker(holder: AnnotationHolder) : VlangCheckerBase(holder) {
    override fun visitContinueStatement(stmt: VlangContinueStatement) {
        val owner = VlangLangUtil.getContinueStatementOwner(stmt)
        if (owner == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "'continue' statement is outside the 'for' loop")
                .create()
        }
    }

    override fun visitBreakStatement(o: VlangBreakStatement) {
        val owner = VlangLangUtil.getBreakStatementOwner(o)
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
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Invalid recursive type: ${owner.name} refers to itself, try change the type to '&${owner.name}'"
                ).create()
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

    override fun visitSendExpr(expr: VlangSendExpr) {
        val left = expr.left
        val leftType = left.getType(null) ?: return
        if (leftType !is VlangChannelTypeEx) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Cannot push on non-channel `${leftType.readableName(expr)}`, left expression of 'send' operator must be 'chan T' type"
            )
                .range(expr)
                .create()
        }
    }
}
