package org.vlang.lang.sql

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*
import org.vlang.utils.parentOfTypeWithStop

object VlangSqlUtil {
    val sqlKeywords = setOf(
        "select", "count", "from", "where", "order",
        "by", "limit", "create", "drop", "table",
        "update", "set", "insert", "into", "delete", "offset", "desc", "asc",
    )

    fun isSqlKeyword(keyword: String): Boolean {
        return keyword in sqlKeywords
    }

    fun insideSql(element: PsiElement): Boolean {
        val parentSql = element.parentOfType<VlangSqlExpression>()
        if (PsiTreeUtil.isAncestor(parentSql?.expression, element, false)) {
            return false
        }

        return parentSql != null
    }

    fun fieldReference(element: PsiElement): Boolean {
        return leftPartOfExpression(element) ||
                leftPartOfUpdateItem(element) ||
                inWhereExpression(element) ||
                element.parentOfType<VlangSqlOrderByClause>() != null
    }

    private fun inWhereExpression(element: PsiElement): Boolean {
        val whereClause = element.parentOfType<VlangSqlWhereClause>() ?: return false
        return whereClause.expression !is VlangBinaryExpr
    }

    private fun leftPartOfExpression(element: PsiElement): Boolean {
        val expr = element.parentOfTypeWithStop<VlangBinaryExpr>(VlangSqlBlock::class) ?: return false
        if (expr is VlangOrBlockExpr) {
            return false
        }
        return PsiTreeUtil.isAncestor(expr.left, element, false)
    }

    private fun leftPartOfUpdateItem(element: PsiElement): Boolean {
        val expr = element.parentOfTypeWithStop<VlangSqlUpdateItem>(VlangSqlBlock::class) ?: return false
        if (expr is VlangOrBlockExpr) {
            return false
        }
        return PsiTreeUtil.isAncestor(expr.expressionList.firstOrNull(), element, false)
    }

    fun getTable(element: PsiElement): VlangSqlTableName? {
        val parentSqlStatement = element.parentOfType<VlangSqlBlockStatement>(true) ?: return null
        return PsiTreeUtil.findChildOfType(parentSqlStatement, VlangSqlTableName::class.java)
    }
}
