package io.vlang.integration.inspections

import io.vlang.ide.inspections.general.VlangCircularImportInspection
import io.vlang.integration.IntegrationTestBase

class CircularImportInspectionsTest : IntegrationTestBase() {

    fun `test simple circular import`() = doTest {
        directory("first") {
            fileNoLangInj("utils.v",
                """
                module first

                import second

                pub fn util(){
                    second.util()
                }
                """.trimIndent())
        }

        directory("second") {
            fileNoLangInj("utils.v",
                """
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

    fun `test three way import cycle`() = doTest {

        directory("first") {
            fileNoLangInj("utils.v",
                """
                module first

                import second

                pub fn util(){
                    second.util()
                }
                """.trimIndent())
        }

        directory("second") {
            fileNoLangInj("utils.v",
                """
                module second

                import third

                pub fn util() {
                    third.util()
                }
                """.trimIndent())
        }

        directory("third") {
            fileNoLangInj("utils.v",
                """
                module third

                import <error descr="Circular import detected">first</error>

                pub fn util() {
                    first.util()
                }
                """.trimIndent())

            enableInspection(VlangCircularImportInspection())
            testInspections()
        }

    }

    //extension lambda to create file without language Sytax injection
    val fileNoLangInj : DirectoryContext.(name: String, text: String) -> Unit = { name, text ->
        file(name, text)
    }

}
