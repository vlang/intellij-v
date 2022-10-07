package org.vlang.ide.inspections

import org.vlang.ide.inspections.namingConventions.VlangClassLikeNamingConventionInspection
import org.vlang.ide.inspections.namingConventions.VlangFieldNamingConventionInspection
import org.vlang.ide.inspections.namingConventions.VlangFunctionNamingConventionInspection
import org.vlang.ide.inspections.namingConventions.VlangTypeAliasNamingConventionInspection

class VlangNamingConventionsInspectionsTest : InspectionTestBase("namingConventions") {
    fun `test functions`()   = doTest( "functions.v", FUNCTION)
    fun `test class like`()  = doTest("class_like.v", CLASS_LIKE)
    fun `test aliases`()     = doTest("aliases.v", ALIAS)
    fun `test fields`()      = doTest("fields.v", FIELDS)
    fun `test non v names`() = doTest("non_v_names.v", FUNCTION, CLASS_LIKE, ALIAS)

    fun `test pseudo builtin`() = doTest("builtin/pseudo_builtin.v", FUNCTION, CLASS_LIKE, ALIAS)
    fun `test translated`()     = doTest("translated/translated.v", FUNCTION, CLASS_LIKE, ALIAS)

    companion object {
        val FUNCTION = VlangFunctionNamingConventionInspection()
        val CLASS_LIKE = VlangClassLikeNamingConventionInspection()
        val ALIAS = VlangTypeAliasNamingConventionInspection()
        val FIELDS = VlangFieldNamingConventionInspection()
    }
}
