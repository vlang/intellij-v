package io.vlang.integration.inspections

import io.vlang.ide.inspections.general.VlangCircularImportInspection
import io.vlang.integration.IntegrationTestBase

class CircularImportInspectionsTest : IntegrationTestBase() {
    fun `test simple circular import`() = doTest {
        directory("first") {
            file("utils.v", """
                module first

                import second
                
                pub fn util(){
                    second.util()
                }
                """.trimIndent())
        }

        directory("second") {
            file("utils.v", """
                module second

                import <error descr="Circular import detected">first</error>
                
                pub fn util() {
                    first.util()
                }
                """.trimIndent())

            enableInspection(VlangCircularImportInspection())
            testInspections()
        }
    }
}
