package org.vlang.ide.annotators

class VlangCommonProblemsTest : AnnotatorTestBase() {
    fun `test break and continue outside loop`() = doTest("break_continue_outside_loop.v")
    fun `test recursive struct`() = doTest("recursive_struct.v")
    fun `test interface field with default value`() = doTest("interface_field_default_value.v")
    fun `test send channel expr`() = doTest("send_channel_expr_for_non_chan_type.v")
    fun `test for over map without key`() = doTestQuickFix("for_over_map_without_key.v", "Add `_` variable to loop iterate over map")
    fun `test sum type with interface`() = doTest("sum_type_with_interface_type.v")
    fun `test capture variable list`() = doTestQuickFix("capture_list_variables.v")
    fun `test wrong pointer type`() = doTestQuickFix("wrong_pointer_type.v")
    fun `test struct embedding`() = doTestQuickFix("struct_embedding.v")
    fun `test return count mismatch`() = doTestQuickFix("return_count_mismatch.v")
    fun `test duplicate struct fields group modifier`() = doTest("duplicate_struct_fields_group_modifier.v")
    fun `test duplicate modifier in list`() = doTest("duplicate_modifier_in_list.v")
    fun `test unfinished modifier list in structs`() = doTest("unfinished_modifier_list_in_structs.v")
    fun `test wrong modifiers order in structs`() = doTestQuickFix("wrong_modifiers_order_in_structs.v")
//    fun `test if expression without else`() = doTestQuickFix("if_expression_without_else.v")
    fun `test attributes for interface members`() = doTest("attributes_for_interface_members.v")
    fun `test assign values count mismatch`() = doTest("assign_values_count_mismatch.v")
    fun `test it variable inside array init is deprecated`() = doTestQuickFix("it_variable_inside_array_init_is_deprecated.v")
    fun `test empty enum`() = doTest("empty_enum.v")
    fun `test mut parameter`() = doTest("mut_parameter.v")
}
