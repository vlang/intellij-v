package org.vlang.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilderFactory
import com.intellij.psi.impl.source.tree.ICodeFragmentElementType
import com.intellij.psi.impl.source.tree.TreeElement
import com.intellij.psi.tree.IElementType
import org.vlang.lang.VlangLanguage
import org.vlang.lang.VlangParser
import org.vlang.lang.VlangTypes

class VlangCodeFragmentElementType(private val elementType: IElementType, debugName: String) :
    ICodeFragmentElementType(debugName, VlangLanguage) {

    override fun parseContents(chameleon: ASTNode): ASTNode? {
        if (chameleon !is TreeElement) return null
        val project = chameleon.manager.project
        val builder = PsiBuilderFactory.getInstance().createBuilder(project, chameleon)
        val root = VlangParser().parse(elementType, builder)
        return root.firstChildNode
    }

    companion object {
        val EXPR = VlangCodeFragmentElementType(VlangTypes.EXPRESSION, "VLANG_EXPRESSION")
//        val STMT = VlangCodeFragmentElementType(RsElementTypes.STATEMENT_CODE_FRAGMENT_ELEMENT, "RS_STMT_CODE_FRAGMENT")
//        val TYPE_REF = VlangCodeFragmentElementType(RsElementTypes.TYPE_REFERENCE_CODE_FRAGMENT_ELEMENT, "RS_TYPE_REF_CODE_FRAGMENT")
//        val TYPE_PATH = VlangCodeFragmentElementType(RsElementTypes.TYPE_PATH_CODE_FRAGMENT_ELEMENT, "RS_TYPE_PATH_CODE_FRAGMENT")
//        val VALUE_PATH = VlangCodeFragmentElementType(RsElementTypes.VALUE_PATH_CODE_FRAGMENT_ELEMENT, "RS_VALUE_PATH_CODE_FRAGMENT")
//        val REPL = VlangCodeFragmentElementType(RsElementTypes.REPL_CODE_FRAGMENT_ELEMENT, "RS_REPL_CODE_FRAGMENT")
    }
}
