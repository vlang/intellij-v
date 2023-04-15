package org.vlang.ide.comments

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiReferenceBase

abstract class VlangCommentReference(element: PsiComment, rangeInElement: TextRange) :
    PsiReferenceBase<PsiComment>(element, rangeInElement, true)
