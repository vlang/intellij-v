package org.vlang.lang.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.vlang.lang.VlangLanguage
import org.vlang.lang.VlangTypes

class VlangFormattingModelBuilder : FormattingModelBuilder {
    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(settings, VlangLanguage)
            .around(VlangTypes.ASSIGN)
            .spaceIf(settings.getCommonSettings(VlangLanguage.id).SPACE_AROUND_ASSIGNMENT_OPERATORS)
            .around(VlangTypes.FUNCTION_DECLARATION).blankLines(settings.BLANK_LINES_AROUND_METHOD)
    }

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
//        CodeInsightSettings.getInstance().SMART_INDENT_ON_ENTER = false
        return FormattingModelProvider
            .createFormattingModelForPsiFile(
                formattingContext.containingFile,
                VlangFormattingBlock(
                    formattingContext.node,
                    spacingBuilder = createSpaceBuilder(codeStyleSettings),
                    withIdent = false,
                ),
                codeStyleSettings
            )
    }
}
