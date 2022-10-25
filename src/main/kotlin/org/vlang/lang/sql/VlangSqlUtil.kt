package org.vlang.lang.sql

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*

object VlangSqlUtil {
    val sqlKeywords = setOf(
        "select", "count", "from", "where", "order",
        "by", "limit", "create", "drop", "table",
        "update", "set", "insert", "into", "delete", "offset",
    )

    fun isSqlKeyword(keyword: String): Boolean {
        return keyword in sqlKeywords
    }

    fun insideSql(element: PsiElement): Boolean {
        return element.parentOfType<VlangSqlExpression>() != null
    }

    fun fieldReference(element: PsiElement): Boolean {
        return leftPartOfExpression(element) ||
                inWhereExpression(element) ||
                element.parentOfType<VlangSqlOrderByClause>() != null
    }

    private fun inWhereExpression(element: PsiElement): Boolean {
        val whereClause = element.parentOfType<VlangSqlWhereClause>() ?: return false
        if (whereClause.expression !is VlangBinaryExpr) {
            return true
        }

        return false
    }

    private fun leftPartOfExpression(element: PsiElement): Boolean {
        val expr = element.parentOfType<VlangBinaryExpr>() ?: return false
        return PsiTreeUtil.isAncestor(expr.left, element, false)
    }

    fun getTable(element: PsiElement): VlangSqlTableName? {
        val parentSqlStatement = element.parentOfType<VlangSqlBlockStatement>(true) ?: return null
        return PsiTreeUtil.findChildOfType(parentSqlStatement, VlangSqlTableName::class.java)
    }
}
