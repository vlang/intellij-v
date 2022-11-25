package org.vlang.ide.documentation

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import org.vlang.lang.psi.*

object CommentsConverter {
    fun toHtml(comments: List<PsiComment>): String {
        return comments.joinToString("<br>") {
            if (it.tokenType == VlangTokenTypes.LINE_COMMENT) {
                it.text.removePrefix("//")
            } else {
                it.text
                    .removePrefix("/*")
                    .removeSuffix("*/")
                    .lines().joinToString("<br>") {
                        it.removePrefix(" * ")
                    }
            }
        }
    }

    fun getCommentsForElement(element: PsiElement?): List<PsiComment> {
        var comments = getCommentsInner(adjustElement(element))
        if (comments.isEmpty()) {
            if (element is VlangVarDefinition || element is VlangConstDefinition) {
                val parent = element.parent // spec
                comments = getCommentsInner(parent)
                if (comments.isEmpty() && parent != null) {
                    return getCommentsInner(parent.parent) // declaration
                }
            } else if (element is VlangTypeAliasDeclaration) {
                return getCommentsInner(element.parent) // type declaration
            } else if (element is VlangFieldDefinition || element is VlangEnumFieldDefinition || element is VlangInterfaceMethodDefinition) {
                val declaration = element.parent
                val group = declaration.parent

                val groupComments = tryGetCommentsFromGroup(group, declaration)
                if (groupComments.isNotEmpty()) {
                    return groupComments
                }

                // comments after field definition
                // ```
                // struct Foo {
                //   name string // comment
                // }
                // ```
                val parent = element.parent
                val lastChild = parent?.lastChild
                if (lastChild is PsiComment) {
                    return listOf(lastChild)
                }
            }
        }
        return comments
    }

    private fun tryGetCommentsFromGroup(
        group: PsiElement?,
        declaration: PsiElement?,
    ): List<PsiComment> {
        if (group is VlangMembersGroup) {
            // group with modifiers
            if (group.memberModifiers != null) {
                return emptyList()
            }

            if (group.interfaceMethodDeclarationList.firstOrNull() == declaration) {
                return getCommentsInner(group)
            }

            if (group.fieldDeclarationList.firstOrNull() == declaration) {
                return getCommentsInner(group)
            }
        }

        if (group is VlangFieldsGroup) {
            // group with modifiers
            if (group.memberModifiers != null) {
                return emptyList()
            }

            if (group.fieldDeclarationList.firstOrNull() == declaration) {
                return getCommentsInner(group)
            }
        }

        return emptyList()
    }

    private fun getCommentsInner(element: PsiElement?): List<PsiComment> {
        if (element == null) {
            return emptyList()
        }
        val result = mutableListOf<PsiComment>()
        var e: PsiElement?
        e = element.prevSibling
        while (e != null) {
            if (e is PsiWhiteSpace) {
                if (e.getText().contains("\n\n")) return result
                e = e.getPrevSibling()
                continue
            }
            if (e is PsiComment) {
                result.add(0, e)
            } else {
                return result
            }
            e = e.prevSibling
        }
        return result
    }

    private fun adjustElement(element: PsiElement?): PsiElement? {
        if (element is VlangFieldDefinition || element is VlangEnumFieldDefinition || element is VlangInterfaceMethodDefinition) {
            return element.parent
        }

        return element
    }

    fun getCommentsForModule(element: VlangModuleClause): List<PsiComment> {
        val result = mutableListOf<PsiComment>()
        var e: PsiElement? = element.nextSibling
        while (e != null && (e is PsiWhiteSpace || e is PsiComment)) {
            // comment after comment with empty lines
            if (result.isNotEmpty() && e is PsiWhiteSpace && e.text.contains("\n\n")) {
                return result
            }

            if (e is PsiComment) {
                result.add(e)
            }

            e = e.nextSibling
        }

        return result
    }
}
