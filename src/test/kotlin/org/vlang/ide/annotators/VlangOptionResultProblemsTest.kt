package org.vlang.ide.annotators

class VlangOptionResultProblemsTest : AnnotatorTestBase("result-option") {
    fun `test err always none`() = doTest("err_is_always_none.v")
    fun `test or expr for non result option`() = doTest("or_expr_for_non_result_option.v")
    fun `test wrong usage of Exclamation Mark or Question Mark`() = doTest("wrong_usage_of_excl_or_quest_mark.v")
    fun `test unwrapping for non result option`() = doTest("unwrapping_for_non_result_option.v")
}
