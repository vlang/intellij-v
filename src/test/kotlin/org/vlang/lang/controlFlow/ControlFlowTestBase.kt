package org.vlang.lang.controlFlow

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language
import org.vlang.lang.psi.VlangFile

class ControlFlowTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/controlFlow"

    fun doTest(@Language("v") content: String, expectedControlFlow: String) {
        val file = myFixture.configureByText("a.v", content) as VlangFile

        val function = file.getFunctions().first { it.name == "main" }
        val controlFlow = function.controlFlow()
        assertEquals(expectedControlFlow.trimIndent(), controlFlow.toString().trimIndent())
    }

    fun `test simple`() = doTest(
        """
        fn main() {
            mut a := 100
            a = 100
            
            if a == 100 {
                a = 200 + a
                return 100
            }
            
            return 200
            
            a = 300
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT mut a := 100
p:1     s:-      2: VARIABLE_DECLARATION a
p:2     s:-      3: STATEMENT a = 100
p:3     s:-      4: ACCESS (WRITE) a
p:4     s:-      5: STATEMENT if a == 100 {
p:5     s:-      6: ACCESS (READ) a
p:6     s:-      7: HOST
p:7     s:-      8: CONDITION(true, CONDITIONAL_EXPR) 
p:8     s:-      9: STATEMENT a = 200 + a
p:9     s:-     10: ACCESS (WRITE) a
p:10    s:-     11: ACCESS (READ) a
p:11    s:-     12: STATEMENT return 100
p:12    s:-     13: RETURN 100
p:7     s:-     14: CONDITION(false, CONDITIONAL_EXPR) 
p:14    s:-     15: STATEMENT return 200
p:15    s:-     16: RETURN 200
p:-     s:-     17: STATEMENT a = 300
p:17    s:-     18: ACCESS (WRITE) a
p:13, 16, 18 s:-     19: EXIT_POINT
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
p:5     s:-      6: ACCESS (WRITE) canNotBeImmutable
p:6     s:-      7: STATEMENT println(canBeImmutable)
p:7     s:-      8: ACCESS (READ) println
p:8     s:-      9: ACCESS (READ) canBeImmutable
p:9     s:-     10: CALL println
p:10    s:-     11: STATEMENT println(canNotBeImmutable)
p:11    s:-     12: ACCESS (READ) println
p:12    s:-     13: ACCESS (READ) canNotBeImmutable
p:13    s:-     14: CALL println
p:14    s:-     15: EXIT_POINT
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
p:2     s:-      3: ACCESS (READ) a
p:3     s:-      4: STATEMENT foo.a = 200
p:4     s:-      5: ACCESS (WRITE) foo.a
p:5     s:-      6: ACCESS (READ) foo
p:6     s:-      7: STATEMENT println(foo.a)
p:7     s:-      8: ACCESS (READ) println
p:8     s:-      9: ACCESS (READ) foo.a
p:9     s:-     10: ACCESS (READ) foo
p:10    s:-     11: CALL println
p:11    s:-     12: EXIT_POINT
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
p:1     s:-      2: ACCESS (READ) foo
p:2     s:-      3: CALL foo
p:3     s:-      4: EXIT_POINT
        """.trimIndent()
    )

    fun `test panic function call`() = doTest(
        """
        fn main() {
            panic('oops')
            
            a := 100
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT panic('oops')
p:1     s:-      2: ACCESS (READ) panic
p:2     s:-      3: CALL panic
p:-     s:-      4: STATEMENT a := 100
p:4     s:-      5: VARIABLE_DECLARATION a
p:5, 3  s:-      6: EXIT_POINT
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
p:4     s:-      5: ACCESS (READ) foo
p:5     s:-      6: CALL foo
p:6     s:-      7: DEFER
p:7     s:-      8: EXIT_POINT
        """.trimIndent()
    )

    fun `test defer`() = doTest(
        """
        fn main() {
            mut a := 100
            
            defer {
                a = 200
            }
            
            mut b := 100
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
p:0     s:-      1: STATEMENT mut a := 100
p:1     s:-      2: VARIABLE_DECLARATION a
p:2     s:-      3: STATEMENT mut b := 100
p:3     s:-      4: VARIABLE_DECLARATION b
p:4     s:-      5: STATEMENT if b == 10 {
p:5     s:-      6: ACCESS (READ) b
p:6     s:-      7: HOST
p:7     s:-      8: CONDITION(true, CONDITIONAL_EXPR) 
p:8     s:-      9: STATEMENT return 100
p:9     s:-     10: RETURN 100
p:7     s:-     11: CONDITION(false, CONDITIONAL_EXPR) 
p:11    s:-     12: STATEMENT return 100
p:12    s:-     13: RETURN 100
p:10, 13 s:-     14: STATEMENT defer {
p:14    s:-     15: STATEMENT a = 200
p:15    s:-     16: ACCESS (WRITE) a
p:16    s:-     17: DEFER
p:17    s:-     18: STATEMENT defer {
p:18    s:-     19: STATEMENT b = 300
p:19    s:-     20: ACCESS (WRITE) b
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
p:2     s:-      3: ACCESS (READ) foo
p:3     s:-      4: CALL foo
p:4     s:-      5: HOST
p:5     s:-      6: CONDITION(true, CALL_EXPR) 
p:6     s:-      7: STATEMENT return 100
p:7     s:-      8: RETURN 100
p:5     s:-      9: CONDITION(false, CALL_EXPR) 
p:8, 9  s:-     10: EXIT_POINT
        """.trimIndent()
    )

    fun `test if else if`() = doTest(
        """
        fn main() {
            if 1 == 1 {
                return 100
            } else if 2 == 2 {
                return 200
            }
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT if 1 == 1 {
p:1     s:-      2: HOST
p:2     s:-      3: CONDITION(true, CONDITIONAL_EXPR) 
p:3     s:-      4: STATEMENT return 100
p:4     s:-      5: RETURN 100
p:2     s:-      6: CONDITION(false, CONDITIONAL_EXPR) 
p:6     s:-      7: STATEMENT if 1 == 1 {
p:7     s:-      8: HOST
p:8     s:-      9: CONDITION(true, CONDITIONAL_EXPR) 
p:9     s:-     10: STATEMENT return 200
p:10    s:-     11: RETURN 200
p:8     s:-     12: CONDITION(false, CONDITIONAL_EXPR) 
p:5, 11, 12 s:-     13: EXIT_POINT
        """.trimIndent()
    )

    fun `test match expression`() = doTest(
        """
        fn main() {
            a := 100
            match a {
                100 {
                    return 100
                }
                200 {
                    println('200')
                }
                300 {
                    println('300')
                }
                else {
                    return 300
                }
            }
            
            println('here')
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT a := 100
p:1     s:-      2: VARIABLE_DECLARATION a
p:2     s:-      3: STATEMENT match a {
p:3     s:-      4: ACCESS (READ) a
p:4     s:-      5: HOST
p:5     s:-      6: CONDITION(true, LITERAL) 
p:6     s:-      7: STATEMENT return 100
p:7     s:-      8: RETURN 100
p:5     s:-      9: CONDITION(true, LITERAL) 
p:9     s:-     10: STATEMENT println('200')
p:10    s:-     11: ACCESS (READ) println
p:11    s:-     12: CALL println
p:5     s:-     13: CONDITION(true, LITERAL) 
p:13    s:-     14: STATEMENT println('300')
p:14    s:-     15: ACCESS (READ) println
p:15    s:-     16: CALL println
p:5     s:-     17: CONDITION(false, null) 
p:17    s:-     18: STATEMENT return 300
p:18    s:-     19: RETURN 300
p:12, 16 s:-     20: STATEMENT println('here')
p:20    s:-     21: ACCESS (READ) println
p:21    s:-     22: CALL println
p:8, 19, 22 s:-     23: EXIT_POINT
        """.trimIndent()
    )

    fun `test match expression with types`() = doTest(
        """
            
        struct Foo {
            foo string
        }
        
        struct Boo {
            boo string
        }
            
        fn main() {
            mut foo := 100
            match foo {
                Foo {
                    foo.foo
                }
                Boo {
                    foo.boo
                }
                else {
                    return 300
                }
            }
            
            println('here')
        }
        """,
        """
p:-     s:-      0: ENTRY_POINT
p:0     s:-      1: STATEMENT mut foo := 100
p:1     s:-      2: VARIABLE_DECLARATION foo
p:2     s:-      3: STATEMENT match foo {
p:3     s:-      4: ACCESS (READ) foo
p:4     s:-      5: HOST
p:5     s:-      6: CONDITION(true, TYPE) 
p:6     s:-      7: ACCESS (READ) foo.foo
p:7     s:-      8: ACCESS (READ) foo
p:5     s:-      9: CONDITION(true, TYPE) 
p:9     s:-     10: ACCESS (READ) foo.boo
p:10    s:-     11: ACCESS (READ) foo
p:5     s:-     12: CONDITION(false, null) 
p:12    s:-     13: STATEMENT return 300
p:13    s:-     14: RETURN 300
p:8, 11 s:-     15: STATEMENT println('here')
p:15    s:-     16: ACCESS (READ) println
p:16    s:-     17: CALL println
p:14, 17 s:-     18: EXIT_POINT
        """.trimIndent()
    )
}
