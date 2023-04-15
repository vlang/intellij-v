package org.vlang.lang.annotator

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.HighlightInfoType
import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.elementType
import org.vlang.ide.colors.VlangColor
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangPrimitiveTypes
import org.vlang.lang.sql.VlangSqlUtil
import org.vlang.utils.inside
import org.vlang.utils.parentNth
import java.util.function.Consumer

class VlangHighlightInfoCollectingVisitor : VlangVisitor(), HighlightVisitor, DumbAware {
    var myHighlightInfoSink: Consumer<HighlightInfo>? = null

    override fun suitableForFile(file: PsiFile): Boolean {
        return file is VlangFile && InjectedLanguageManager.getInstance(file.project).isInjectedFragment(file)
    }

    override fun visit(element: PsiElement) {
        element.accept(this)
    }

    override fun analyze(file: PsiFile, updateWholeFile: Boolean, holder: HighlightInfoHolder, highlight: Runnable): Boolean {
        myHighlightInfoSink = Consumer { highlightInfo: HighlightInfo ->
            holder.add(
                highlightInfo
            )
        }
        highlight.run()
        return true
    }

    override fun visitFunctionDeclaration(o: VlangFunctionDeclaration) {
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.PUBLIC_FUNCTION, VlangColor.FUNCTION)
        )
    }

    override fun visitMethodDeclaration(o: VlangMethodDeclaration) {
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.PUBLIC_FUNCTION, VlangColor.FUNCTION)
        )
    }

    override fun visitStructDeclaration(o: VlangStructDeclaration) {
        if (o.isUnion) {
            highlight(
                o.nameIdentifier,
                public(o, VlangColor.PUBLIC_UNION, VlangColor.UNION)
            )
            return
        }
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.PUBLIC_STRUCT, VlangColor.STRUCT)
        )
    }

    override fun visitEnumDeclaration(o: VlangEnumDeclaration) {
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.PUBLIC_ENUM, VlangColor.ENUM)
        )
    }

    override fun visitInterfaceDeclaration(o: VlangInterfaceDeclaration) {
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.PUBLIC_INTERFACE, VlangColor.INTERFACE)
        )
    }

    override fun visitTypeAliasDeclaration(o: VlangTypeAliasDeclaration) {
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.PUBLIC_TYPE_ALIAS, VlangColor.TYPE_ALIAS)
        )
    }

    override fun visitConstDefinition(o: VlangConstDefinition) {
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.PUBLIC_CONSTANT, VlangColor.CONSTANT)
        )
    }

    override fun visitFieldDefinition(o: VlangFieldDefinition) {
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.PUBLIC_FIELD, VlangColor.FIELD)
        )
    }

    override fun visitInterfaceMethodDefinition(o: VlangInterfaceMethodDefinition) {
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.INTERFACE_METHOD, VlangColor.INTERFACE_METHOD)
        )
    }

    override fun visitEnumFieldDefinition(o: VlangEnumFieldDefinition) {
        highlight(
            o.nameIdentifier,
            public(o, VlangColor.ENUM_FIELD, VlangColor.ENUM_FIELD)
        )
    }

    override fun visitVarDefinition(o: VlangVarDefinition) {
        highlight(
            o.nameIdentifier,
            mutable(o, VlangColor.MUTABLE_VARIABLE, VlangColor.VARIABLE)
        )
    }

    override fun visitReceiver(o: VlangReceiver) {
        highlight(
            o.nameIdentifier,
            mutable(o, VlangColor.MUTABLE_RECEIVER, VlangColor.RECEIVER)
        )
    }

    override fun visitParamDefinition(o: VlangParamDefinition) {
        highlight(
            o.nameIdentifier,
            mutable(o, VlangColor.MUTABLE_PARAMETER, VlangColor.PARAMETER)
        )
    }

    override fun visitGlobalVariableDefinition(o: VlangGlobalVariableDefinition) {
        highlight(o.nameIdentifier, VlangColor.GLOBAL_VARIABLE)
    }

    override fun visitLabelRef(o: VlangLabelRef) {
        highlight(o.identifier, VlangColor.LABEL)
    }

    override fun visitImportName(o: VlangImportName) {
        val parent = o.parent as? VlangImportPath ?: return
        val lastPart = parent.lastPartPsi
        highlight(lastPart, VlangColor.MODULE)
    }

    override fun visitModuleClause(o: VlangModuleClause) {
        highlight(o.nameIdentifier, VlangColor.MODULE)
    }

    override fun visitSqlBlock(o: VlangSqlBlock) {
        highlight(o, VlangColor.SQL_CODE)
    }

    override fun visitUnsafeExpression(o: VlangUnsafeExpression) {
        highlight(o, VlangColor.UNSAFE_CODE)
    }

    override fun visitElement(element: PsiElement) {
        if (element is LeafPsiElement) {
            val color = highlightLeaf(element)
            if (color != null) {
                highlight(element, color)
            }
        }
    }

    override fun visitComment(comment: PsiComment) {
        val docComment = comment as? VlangDocComment ?: return
        if (!docComment.isValidDoc()) return
        val range = docComment.getOwnerNameRangeInElement() ?: return
        highlight(docComment, VlangColor.COMMENT_REFERENCE, range.shiftRight(docComment.textOffset))
    }

    private fun highlightLeaf(element: PsiElement): VlangColor? {
        val parent = element.parent as? VlangCompositeElement ?: return null

        if (VlangCompletionUtil.isCompileTimeMethodIdentifier(element) && parent.parent is VlangCallExpr) {
            return VlangColor.CT_METHOD_CALL
        }

        if (VlangPrimitiveTypes.isPrimitiveType(element.text) &&
            (element.parentNth<VlangCallExpr>(2) != null || element.parent is VlangTypeReferenceExpression) &&
            element.parent !is VlangAliasType
        ) {
            // highlight only u8(100) call, not a.u8()
            val call = element.parentNth<VlangCallExpr>(2)
            val callExpr = call?.expression as? VlangReferenceExpression
            if (callExpr?.getQualifier() == null) {
                return VlangColor.BUILTIN_TYPE
            }
        }

        if ((element.text == "chan" || element.text == "thread") && element.inside<VlangType>()) {
            return VlangColor.KEYWORD
        }

        if (parent is VlangGenericParameter) {
            return VlangColor.GENERIC_PARAMETER
        }

        if (element.elementType == VlangTypes.IDENTIFIER && parent is VlangReferenceExpressionBase) {
            // highlighted in VlangAnnotator
            return null
        }

        if (parent is VlangAttribute || parent is VlangAttributeIdentifier) {
            return highlightAttribute(element)
        }

        if (VlangSqlUtil.insideSql(element) && VlangSqlUtil.isSqlKeyword(element.text)) {
            return VlangColor.SQL_KEYWORD
        }

        return when (element.elementType) {
            VlangTypes.IDENTIFIER -> highlightIdentifier(element)
            else                  -> null
        }
    }

    private fun highlightIdentifier(element: PsiElement): VlangColor? {
        return when (element.text) {
            // pseudo keywords
            in listOf("_likely_", "_unlikely_", "sql", "map") -> VlangColor.KEYWORD
            else                                              -> null
        }
    }

    private fun highlightAttribute(element: PsiElement): VlangColor? {
        if (element.parent is VlangPlainAttribute) {
            if (element.elementType == VlangTypes.IDENTIFIER || element.elementType == VlangTypes.UNSAFE) {
                return VlangColor.ATTRIBUTE
            }
        }

        if (element.parent is VlangAttributeIdentifier) {
            return VlangColor.ATTRIBUTE
        }

        if (element.parent is VlangAttribute && (element.elementType == VlangTypes.LBRACK || element.elementType == VlangTypes.RBRACK)) {
            return VlangColor.ATTRIBUTE
        }

        return null
    }

    override fun clone() = VlangHighlightInfoCollectingVisitor()

    private fun public(element: VlangNamedElement, ifPublic: VlangColor, ifNotPublic: VlangColor): VlangColor {
        return if (element.isPublic()) ifPublic else ifNotPublic
    }

    private fun mutable(element: VlangMutabilityOwner, ifMutable: VlangColor, ifNoMutable: VlangColor): VlangColor {
        return if (element.isMutable()) ifMutable else ifNoMutable
    }

    private fun highlightInfo(element: PsiElement?, attribute: TextAttributesKey, range: TextRange? = null) {
        if (element == null) return
        val builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
            .needsUpdateOnTyping(false)
            .textAttributes(attribute)

        if (range != null) builder.range(range) else builder.range(element)

        if (isUnitTestMode) builder.description(attribute.externalName)
        myHighlightInfoSink!!.accept(builder.createUnconditionally())
    }

    private fun highlight(element: PsiElement?, color: VlangColor, range: TextRange? = null) {
        highlightInfo(element, color.textAttributesKey, range)
    }

    companion object {
        private val isUnitTestMode = ApplicationManager.getApplication().isUnitTestMode
    }
}
