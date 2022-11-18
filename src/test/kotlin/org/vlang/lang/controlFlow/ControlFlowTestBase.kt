package org.vlang.lang.controlFlow

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.vlang.lang.psi.VlangFile

class ControlFlowTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/controlFlow"

    fun doTest(content: String, expectedControlFlow: String) {
        val file = myFixture.configureByText("a.v", content) as VlangFile

        val function = file.getFunctions().first { it.name == "main" }
        val controlFlow = function.controlFlow()
        assertEquals(expectedControlFlow.trimIndent(), controlFlow.toString().trimIndent())
    }

    fun `test simple`() = doTest(
        """
        fn main() {
            a := 100
            a = 100
            
            if a == 100 {
                a = 200
                return 100
            }
            
            return 200
            
            a = 300
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT a := 100
p:1     s:-      2: VARIABLE_DECLARATION a
p:2     s:-      3: STATEMENT a = 100
p:3     s:-      4: ACCESS_VARIABLE (WRITE) a
p:4     s:-      5: STATEMENT if a == 100 {
p:5     s:-      6: ACCESS_VARIABLE (READ) a
p:6     s:-      7: HOST
p:7     s:-      8: CONDITION(true, CONDITIONAL_EXPR) 
p:8     s:-      9: STATEMENT a = 200
p:9     s:-     10: ACCESS_VARIABLE (WRITE) a
p:10    s:-     11: STATEMENT return 100
p:11    s:-     12: RETURN 100
p:7     s:-     13: CONDITION(false, CONDITIONAL_EXPR) 
p:13    s:-     14: STATEMENT return 200
p:14    s:-     15: RETURN 200
p:-     s:-     16: STATEMENT a = 300
p:16    s:-     17: ACCESS_VARIABLE (WRITE) a
p:12, 15, 17 s:-     18: EXIT_POINT
        """
    )

    fun `test variable read write`() = doTest(
        """
        fn println(a int) {}
            
        fn main() {
            mut canBeImmutable := 100
            mut canNotBeImmutable := 100
        
            canNotBeImmutable = 200
        
            println(canBeImmutable)
            println(canNotBeImmutable)
        }
        """,
        """
        p:-     s:-      0: ENTRY_POINT
        p:0     s:-      1: STATEMENT mut canBeImmutable := 100
        p:1     s:-      2: VARIABLE_DECLARATION canBeImmutable
        p:2     s:-      3: STATEMENT mut canNotBeImmutable := 100
        p:3     s:-      4: VARIABLE_DECLARATION canNotBeImmutable
        p:4     s:-      5: STATEMENT canNotBeImmutable = 200
        p:5     s:-      6: ACCESS_VARIABLE (WRITE) canNotBeImmutable
        p:6     s:-      7: STATEMENT println(canBeImmutable)
        p:7     s:-      8: ACCESS_VARIABLE (READ) canBeImmutable
        p:8     s:-      9: FUNCTION_CALL println
        p:9     s:-     10: STATEMENT println(canNotBeImmutable)
        p:10    s:-     11: ACCESS_VARIABLE (READ) canNotBeImmutable
        p:11    s:-     12: FUNCTION_CALL println
        p:12    s:-     13: EXIT_POINT
        """
    )

    fun `test field read write`() = doTest(
        """
        fn println(a int) {}
        
        struct Foo {
        mut:
            a int
        }
            
        fn main() {
            mut foo := Foo{a: 100}
            foo.a = 200
        
            println(foo.a)
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT mut foo := Foo{a: 100}
p:1     s:-      2: VARIABLE_DECLARATION foo
p:2     s:-      3: STATEMENT foo.a = 200
p:3     s:-      4: ACCESS_FIELD (WRITE) a
p:4     s:-      5: ACCESS_VARIABLE (WRITE) foo
p:5     s:-      6: STATEMENT println(foo.a)
p:6     s:-      7: ACCESS_FIELD (READ) a
p:7     s:-      8: ACCESS_VARIABLE (READ) foo
p:8     s:-      9: FUNCTION_CALL println
p:9     s:-     10: EXIT_POINT
        """
    )

    fun `test if else`() = doTest(
        """
        fn main() {
            if 1 {
                return 1
            } else {
                return 2
            }
            
            return 3
        }
        """,
        """
        p:-     s:-      0: ENTRY_POINT
        p:0     s:-      1: STATEMENT if 1 {
        p:1     s:-      2: HOST
        p:2     s:-      3: CONDITION(true, LITERAL) 
        p:3     s:-      4: STATEMENT return 1
        p:4     s:-      5: RETURN 1
        p:2     s:-      6: CONDITION(false, LITERAL) 
        p:6     s:-      7: STATEMENT return 2
        p:7     s:-      8: RETURN 2
        p:-     s:-      9: STATEMENT return 3
        p:9     s:-     10: RETURN 3
        p:5, 8, 10 s:-     11: EXIT_POINT
        """.trimIndent()
    )

    fun `test function call`() = doTest(
        """
        fn foo() {
            return 1
        }
            
        fn main() {
            foo()
        }
        """,
        """
        p:-     s:-      0: ENTRY_POINT
        p:0     s:-      1: STATEMENT foo()
        p:1     s:-      2: FUNCTION_CALL foo
        p:2     s:-      3: EXIT_POINT
        """.trimIndent()
    )

    fun `test noreturn function call`() = doTest(
        """
        [noreturn]
        fn foo() {
            exit(1)
        }
            
        fn main() {
            foo()
            
            a := 100
        }
        """,
        """
        p:-     s:-      0: ENTRY_POINT
        p:0     s:-      1: STATEMENT foo()
        p:1     s:-      2: FUNCTION_CALL foo
        p:-     s:-      3: STATEMENT a := 100
        p:3     s:-      4: VARIABLE_DECLARATION a
        p:4, 2  s:-      5: EXIT_POINT
        """.trimIndent()
    )

    fun `test simple defer`() = doTest(
        """
        fn foo() {}
            
        fn main() {
            defer {
                foo()
            }
        
            a := 100
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT a := 100
p:1     s:-      2: VARIABLE_DECLARATION a
p:2     s:-      3: STATEMENT defer {
p:3     s:-      4: STATEMENT foo()
p:4     s:-      5: FUNCTION_CALL foo
p:5     s:-      6: DEFER
p:6     s:-      7: EXIT_POINT
        """.trimIndent()
    )

    fun `test defer`() = doTest(
        """
        fn main() {
            a := 100
            
            defer {
                a = 200
            }
            
            b := 100
            if b == 10 {
                return 100
            }
            
            defer {
                b = 300
            }
            
            return 100
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT a := 100
p:1     s:-      2: VARIABLE_DECLARATION a
p:2     s:-      3: STATEMENT b := 100
p:3     s:-      4: VARIABLE_DECLARATION b
p:4     s:-      5: STATEMENT if b == 10 {
p:5     s:-      6: ACCESS_VARIABLE (READ) b
p:6     s:-      7: HOST
p:7     s:-      8: CONDITION(true, CONDITIONAL_EXPR) 
p:8     s:-      9: STATEMENT return 100
p:9     s:-     10: RETURN 100
p:7     s:-     11: CONDITION(false, CONDITIONAL_EXPR) 
p:11    s:-     12: STATEMENT return 100
p:12    s:-     13: RETURN 100
p:10, 13 s:-     14: STATEMENT defer {
p:14    s:-     15: STATEMENT a = 200
p:15    s:-     16: ACCESS_VARIABLE (WRITE) a
p:16    s:-     17: DEFER
p:17    s:-     18: STATEMENT defer {
p:18    s:-     19: STATEMENT b = 300
p:19    s:-     20: ACCESS_VARIABLE (WRITE) b
p:20    s:-     21: DEFER
p:21    s:-     22: EXIT_POINT
        """.trimIndent()
    )

    fun `test or block`() = doTest(
        """
        fn foo() ?int { return 100 }
            
        fn main() {
            foo() or {
                return 100
            }
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT foo() or {
p:1     s:-      2: STATEMENT foo() or {
p:2     s:-      3: FUNCTION_CALL foo
p:3     s:-      4: HOST
p:4     s:-      5: CONDITION(true, CALL_EXPR) 
p:5     s:-      6: STATEMENT return 100
p:6     s:-      7: RETURN 100
p:4     s:-      8: CONDITION(false, CALL_EXPR) 
p:7, 8  s:-      9: EXIT_POINT
        """.trimIndent()
    )
}
