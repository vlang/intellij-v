package org.vlang.ide.inspections

import org.vlang.ide.inspections.namingConventions.*

class VlangNamingConventionsInspectionsTest : InspectionTestBase("namingConventions") {
    fun `test functions`()   = doTest( "functions.v", FUNCTION)
    fun `test class like`()  = doTest("class_like.v", CLASS_LIKE)
    fun `test aliases`()     = doTest("aliases.v", ALIAS)
    fun `test fields`()      = doTest("fields.v", FIELDS)
    fun `test constants`()   = doTest("constants.v", CONSTANTS)
    fun `test non v names`() = doTest("non_v_names.v", FUNCTION, CLASS_LIKE, ALIAS)

    fun `test pseudo builtin`() = doTest("builtin/pseudo_builtin.v", FUNCTION, CLASS_LIKE, ALIAS)
    fun `test translated`()     = doTest("translated/translated.v", FUNCTION, CLASS_LIKE, ALIAS)

    fun `test generic receivers names`() = doTest("generic_receivers_names.v", VlangReceiverNamesInspection())
    fun `test different receivers names`() = doTest("different_receivers_names.v", VlangReceiverNamesInspection())

    fun `test var names`() = doTest("variables.v", VlangVarNamingConventionInspection())

    companion object {
        val FUNCTION = VlangFunctionNamingConventionInspection()
        val CLASS_LIKE = VlangClassLikeNamingConventionInspection()
        val ALIAS = VlangTypeAliasNamingConventionInspection()
        val FIELDS = VlangFieldNamingConventionInspection()
        val CONSTANTS = VlangConstantNamingConventionInspection()
    }
}
