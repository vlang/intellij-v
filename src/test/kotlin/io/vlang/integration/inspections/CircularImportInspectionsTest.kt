package io.vlang.integration.inspections

import io.vlang.ide.inspections.general.VlangCircularImportInspection
import io.vlang.integration.IntegrationTestBase

class CircularImportInspectionsTest : IntegrationTestBase() {

    fun `test simple circular import`() = doTest {
        directory("first") {
            file(
                "utils.v",
                """
                module first

                import second

                pub fn util(){
                    second.util()
                }
                """.trimIndent()
            )
        }

        directory("second") {
            file(
                "utils.v",
                """
                module second

                import <error descr="Circular import detected">first</error>

                pub fn util() {
                    first.util()
                }
                """.trimIndent()
            )

            enableInspection(VlangCircularImportInspection())
            testInspections()
        }
    }

    fun `test no circular import`() = doTest {
        directory("first") {
            file(
                "utils.v",
                """
                module first

                pub fn util(){
                    println("hello")
                }
                """.trimIndent()
            )
        }

        file(
            "main.v", """
                module main

                import first

                pub fn util() {
                    first.util()
                }
                """.trimIndent()
        )

        enableInspection(VlangCircularImportInspection())
        testInspections()
    }

    fun `test circular import with test file`() = doTest {
        directory("first") {
            file(
                "utils.v",
                """
                module first

                pub fn util() {
                    println("hello")
                }
                """.trimIndent()
            )

            file("utils_test.v", """
                import first
                                
                test_util() {
                    assert true
                }
            """.trimIndent())
        }

        file(
            "main.v", """
                module main

                import first

                pub fn util() {
                    first.util()
                }
                """.trimIndent()
        )

        enableInspection(VlangCircularImportInspection())
        testInspections()
    }

    fun `test three way import cycle`() = doTest {

        directory("first") {
            file(
                "utils.v",
                """
                module first

                import second

                pub fn util(){
                    second.util()
                }
                """.trimIndent()
            )
        }

        directory("second") {
            file(
                "utils.v",
                """
                module second

                import third

                pub fn util() {
                    third.util()
                }
                """.trimIndent()
            )
        }

        directory("third") {
            file(
                "utils.v", """
                module third

                import <error descr="Circular import detected">first</error>

                pub fn util() {
                    first.util()
                }
                """.trimIndent()
            )

            enableInspection(VlangCircularImportInspection())
            testInspections()
        }

    }

//    //extension lambda to create file without language Syntax injection
//    val fileNoLangInj: DirectoryContext.(name: String, @Language("vlang") text: String) -> Unit = { name, text ->
//        file(name, text)
//    }

}
