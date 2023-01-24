package org.vlang.ide.editor

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.*

class VlangBreadcrumbsInfoProvider : BreadcrumbsProvider {
    private interface ElementHandler<T : VlangCompositeElement> {
        fun accepts(e: PsiElement): Boolean

        fun elementInfo(e: T): String
    }

    private val handlers = listOf<ElementHandler<*>>(
        VlangFunctionHandler,
        VlangMethodHandler,
        VlangStructHandler,
        VlangInterfaceHandler,
        VlangEnumHandler,
        VlangIfHandler,
        VlangElseHandler,
        VlangForHandler,
        VlangMatchHandler,
        VlangMatchArmHandler,
        VlangDeferHandler,
        VlangAnonFunctionHandler,
        VlangOrHandler,
        VlangSqlHandler,
        VlangLiteralValueHandler,
        VlangFieldNameHandler,
    )

    private object VlangFunctionHandler : ElementHandler<VlangFunctionDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is VlangFunctionDeclaration

        override fun elementInfo(e: VlangFunctionDeclaration): String = "${e.name}()"
    }

    private object VlangMethodHandler : ElementHandler<VlangMethodDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is VlangMethodDeclaration

        override fun elementInfo(e: VlangMethodDeclaration): String = "${e.receiverType?.text ?: ""}.${e.name}()"
    }

    private object VlangStructHandler : ElementHandler<VlangStructDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is VlangStructDeclaration

        override fun elementInfo(e: VlangStructDeclaration): String = "${e.kindName} ${e.name}"
    }

    private object VlangInterfaceHandler : ElementHandler<VlangInterfaceDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is VlangInterfaceDeclaration

        override fun elementInfo(e: VlangInterfaceDeclaration): String = "interface ${e.name}"
    }

    private object VlangEnumHandler : ElementHandler<VlangEnumDeclaration> {
        override fun accepts(e: PsiElement): Boolean = e is VlangEnumDeclaration

        override fun elementInfo(e: VlangEnumDeclaration): String = "enum ${e.name}"
    }

    private object VlangIfHandler : ElementHandler<VlangIfExpression> {
        override fun accepts(e: PsiElement): Boolean = e is VlangIfExpression

        override fun elementInfo(e: VlangIfExpression): String = buildString {
            append("if ")
            val condition = e.expression
            if (condition != null) {
                append(condition.text.truncate())
            } else {
                append("?")
            }
        }
    }

    private object VlangElseHandler : ElementHandler<VlangElseBranch> {
        override fun accepts(e: PsiElement): Boolean = e is VlangElseBranch

        override fun elementInfo(e: VlangElseBranch): String = buildString {
            append("else")
        }
    }

    private object VlangForHandler : ElementHandler<VlangForStatement> {
        override fun accepts(e: PsiElement): Boolean = e is VlangForStatement

        override fun elementInfo(e: VlangForStatement): String = buildString {
            if (e.parent is VlangLabeledStatement) {
                appendLabelInfo((e.parent as VlangLabeledStatement).labelDefinition)
            }
            append("for ")
        }
    }

    private object VlangMatchHandler : ElementHandler<VlangMatchExpression> {
        override fun accepts(e: PsiElement): Boolean = e is VlangMatchExpression

        override fun elementInfo(e: VlangMatchExpression): String = buildString {
            append("match ")
            val condition = e.expression
            if (condition != null) {
                append(condition.text.truncate())
            } else {
                append("?")
            }
        }
    }

    private object VlangMatchArmHandler : ElementHandler<VlangMatchArm> {
        override fun accepts(e: PsiElement): Boolean = e is VlangMatchArm

        override fun elementInfo(e: VlangMatchArm): String = buildString {
            val parameters = e.parameterList.joinToString(", ") { it.text }
            append("${parameters.truncate()} =>")
        }
    }

    private object VlangDeferHandler : ElementHandler<VlangDeferStatement> {
        override fun accepts(e: PsiElement): Boolean = e is VlangDeferStatement

        override fun elementInfo(e: VlangDeferStatement): String = buildString {
            append("defer")
        }
    }

    private object VlangOrHandler : ElementHandler<VlangOrBlockExpr> {
        override fun accepts(e: PsiElement): Boolean = e is VlangOrBlockExpr

        override fun elementInfo(e: VlangOrBlockExpr): String = buildString {
           append("or")
        }
    }

    private object VlangSqlHandler : ElementHandler<VlangSqlExpression> {
        override fun accepts(e: PsiElement): Boolean = e is VlangSqlExpression

        override fun elementInfo(e: VlangSqlExpression): String = buildString {
            append("sql ")
            val db = e.expression
            if (db != null) {
                append(db.text.truncate())
            }
        }
    }

    private object VlangLiteralValueHandler : ElementHandler<VlangLiteralValueExpression> {
        override fun accepts(e: PsiElement): Boolean = e is VlangLiteralValueExpression

        override fun elementInfo(e: VlangLiteralValueExpression): String = buildString {
            val type = e.type
            append(type.text.truncate())
            append("{...}")
        }
    }

    private object VlangFieldNameHandler : ElementHandler<VlangElement> {
        override fun accepts(e: PsiElement): Boolean = e is VlangElement && e.key != null && e.key?.fieldName != null

        override fun elementInfo(e: VlangElement): String = buildString {
            val fieldName = e.key!!.fieldName!!
            append(fieldName.text)
            append(":")
        }
    }

    private object VlangAnonFunctionHandler : ElementHandler<VlangFunctionLit> {
        override fun accepts(e: PsiElement): Boolean = e is VlangFunctionLit

        override fun elementInfo(e: VlangFunctionLit): String = buildString {
            append("fn ")
            val signature = e.getSignature()?.text ?: "()"
            append(signature.truncate())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun handler(e: PsiElement): ElementHandler<in VlangCompositeElement>? {
        return if (e is VlangCompositeElement)
            handlers.firstOrNull { it.accepts(e) } as ElementHandler<in VlangCompositeElement>?
        else null
    }

    override fun getLanguages(): Array<Language> = arrayOf(VlangLanguage)

    override fun acceptElement(e: PsiElement): Boolean = handler(e) != null

    override fun getElementInfo(e: PsiElement): String = handler(e)!!.elementInfo(e as VlangCompositeElement)

    companion object {
        private const val ellipsis = "${Typography.ellipsis}"

        private fun String.truncate(): String {
            return if (length > 16)
                "${substring(0, 16 - ellipsis.length)}$ellipsis"
            else this
        }

        private fun StringBuilder.appendLabelInfo(labelDecl: VlangLabelDefinition?) {
            if (labelDecl != null) {
                append(labelDecl.text).append(' ')
            }
        }
    }
}
