package org.vlang.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.vlang.debugger.VlangDebugInjectionListener
import org.vlang.lang.VlangTypes.Factory
import org.vlang.lang.doc.psi.impl.VlangDocCommentImpl
import org.vlang.lang.lexer.VlangLexer
import org.vlang.lang.psi.VlangDebuggerExpressionCodeFragment
import org.vlang.lang.psi.VlangDocElementTypes
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangTokenTypes

class VlangParserDefinition : ParserDefinition {
    override fun createLexer(project: Project) = VlangLexer()

    override fun createParser(project: Project) = VlangParser()

    override fun getWhitespaceTokens() = VlangTokenTypes.WHITE_SPACES

    override fun getCommentTokens() = VlangTokenTypes.COMMENTS

    override fun getStringLiteralElements() = VlangTokenTypes.STRING_LITERALS

    override fun getFileNodeType() = VlangFileElementType.INSTANCE

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        val default = { VlangFile(viewProvider) }

        val project = viewProvider.manager.project
        val injectionHost = InjectedLanguageManager.getInstance(project).getInjectionHost(viewProvider)

        if (injectionHost != null) {
            // this class is contained in clion.jar, so it cannot be used inside `is` type check
            if (injectionHost.javaClass.simpleName != "GDBExpressionPlaceholder") {
                return default()
            }

            val injectionListener = project.messageBus.syncPublisher(VlangDebugInjectionListener.INJECTION_TOPIC)
            val contextResult = VlangDebugInjectionListener.DebugContext()
            injectionListener.evalDebugContext(injectionHost, contextResult)
            val context = contextResult.element ?: return default()

            val fragment = VlangDebuggerExpressionCodeFragment(viewProvider, context)
            injectionListener.didInject(injectionHost)

            return fragment
        }

        return default()
    }

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY

    override fun createElement(node: ASTNode): PsiElement {
        if (node.elementType == VlangDocElementTypes.DOC_COMMENT) {
            return VlangDocCommentImpl(node.elementType, node.text)
        }
        return Factory.createElement(node)
    }
}
