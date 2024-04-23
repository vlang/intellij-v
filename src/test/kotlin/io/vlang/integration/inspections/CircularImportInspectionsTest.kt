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

                import <warning descr="Circular import detected">first</warning>
                
                pub fn util() {
                    first.util()
                }
                """.trimIndent())

            enableInspection(VlangCircularImportInspection())
            testInspections()
        }
    }
}
