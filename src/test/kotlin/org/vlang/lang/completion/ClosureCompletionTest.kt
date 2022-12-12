package org.vlang.lang.completion

class ClosureCompletionTest : CompletionTestBase() {
    fun `test simple closure`() = doTestCompletion("""
        module completion

        fn foo(cb fn ()) int {
        	return 0
        }

        fn main() {
        	foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn ()) int {
        	return 0
        }

        fn main() {
        	foo(fn () {
        		
        	})
        }
    """.trimIndent())

    fun `test closure with params`() = doTestCompletion("""
        module completion

        fn foo(cb fn (age int)) int {
        	return 0
        }

        fn main() {
        	foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (age int)) int {
        	return 0
        }

        fn main() {
        	foo(fn (EXPECTED_USER_INPUT_FOR_age int) {
        		
        	})
        }
    """.trimIndent())

    fun `test closure with vararg params`() = doTestCompletion("""
        module completion

        fn foo(cb fn (ages ...int)) int {
        	return 0
        }

        fn main() {
        	foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (ages ...int)) int {
        	return 0
        }

        fn main() {
        	foo(fn (EXPECTED_USER_INPUT_FOR_ages ...int) {
        		
        	})
        }
    """.trimIndent())

    fun `test closure with modifiers`() = doTestCompletion("""
        module completion

        fn foo(cb fn (mut shared age int, name string)) int {
        	return 0
        }

        fn main() {
        	foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (mut shared age int, name string)) int {
        	return 0
        }

        fn main() {
        	foo(fn (mut shared EXPECTED_USER_INPUT_FOR_age int, EXPECTED_USER_INPUT_FOR_name string) {
        		
        	})
        }
    """.trimIndent())

    fun `test closure without param name`() = doTestCompletion("""
        module completion

        fn foo(cb fn (int)) int {
        	return 0
        }

        fn main() {
        	foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (int)) int {
        	return 0
        }

        fn main() {
        	foo(fn (EXPECTED_USER_INPUT_FOR_it int) {
        		
        	})
        }
    """.trimIndent())

    fun `test closure without params name`() = doTestCompletion("""
        module completion

        fn foo(cb fn (int, string)) int {
        	return 0
        }

        fn main() {
        	foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (int, string)) int {
        	return 0
        }

        fn main() {
        	foo(fn (EXPECTED_USER_INPUT_FOR_it int, EXPECTED_USER_INPUT_FOR_it1 string) {
        		
        	})
        }
    """.trimIndent())

    fun `test closure for struct key`() = doTestCompletion("""
        module main

        struct WithCallback {
        	cb fn (foo int) ?bool
        }
        
        fn main() {
        	WithCallback{
        		cb: fn/*caret*/
        	}
        }
    """.trimIndent(), """
        module main

        struct WithCallback {
        	cb fn (foo int) ?bool
        }
        
        fn main() {
        	WithCallback{
        		cb: fn (EXPECTED_USER_INPUT_FOR_foo int) ?bool {
        			
        		}
        	}
        }
    """.trimIndent())

    fun `test closure as default field value`() = doTestCompletion("""
        module main

        struct WithCallback {
        	cb fn (foo int) ?bool = fn/*caret*/
        }
    """.trimIndent(), """
        module main

        struct WithCallback {
        	cb fn (foo int) ?bool = fn (EXPECTED_USER_INPUT_FOR_foo int) ?bool {
        		
        	}
        }
    """.trimIndent())

    fun `test closure after assign`() = doTestCompletion("""
        module main

        fn main() {
        	mut a := fn () {}
            
        	a = fn/*caret*/
        }
    """.trimIndent(), """
        module main
        
        fn main() {
        	mut a := fn () {}
            
        	a = fn () {
        		
        	}
        }
    """.trimIndent())

    fun `test no completion inside closure`() = checkExcludes("""
        module completion

        fn foo(cb fn ()) int {
        	return 0
        }

        fn main() {
        	foo(fn () {
        		fn/*caret*/
        	})?
        }
    """.trimIndent(), 1, "fn () {...}")

    fun `test simple generic closure with unresolved T`() = doTestCompletion("""
        module main

        fn foo<T>(cb fn (T)) int {
        	return 0
        }

        fn main() {
        	foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module main
        
        fn foo<T>(cb fn (T)) int {
        	return 0
        }
        
        fn main() {
        	foo(fn (EXPECTED_USER_INPUT_FOR_it EXPECTED_USER_INPUT_FOR_T) {
        		
        	})
        }
    """.trimIndent())

    fun `test simple generic closure with unresolved T with complex type`() = doTestCompletion("""
        module main

        fn foo<T>(cb fn (a map[string]T) T) int {
        	return 0
        }

        fn main() {
        	foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module main
        
        fn foo<T>(cb fn (a map[string]T) T) int {
        	return 0
        }
        
        fn main() {
        	foo(fn (EXPECTED_USER_INPUT_FOR_a map[string]EXPECTED_USER_INPUT_FOR_T) EXPECTED_USER_INPUT_FOR_T {
        		
        	})
        }
    """.trimIndent())

    fun `test simple generic closure with explicit resolved T`() = doTestCompletion("""
        module main

        fn foo<T>(cb fn (T)) int {
        	return 0
        }

        fn main() {
        	foo<int>(fn/*caret*/)
        }
    """.trimIndent(), """
        module main
        
        fn foo<T>(cb fn (T)) int {
        	return 0
        }
        
        fn main() {
        	foo<int>(fn (EXPECTED_USER_INPUT_FOR_it int) {
        		
        	})
        }
    """.trimIndent())

    fun `test simple generic closure with resolved T from prev param`() = doTestCompletion("""
        module main

        fn foo<T>(val T, cb fn (T)) int {
        	return 0
        }

        fn main() {
        	foo(10, fn/*caret*/)
        }
    """.trimIndent(), """
        module main
        
        fn foo<T>(val T, cb fn (T)) int {
        	return 0
        }
        
        fn main() {
        	foo(10, fn (EXPECTED_USER_INPUT_FOR_it int) {
        		
        	})
        }
    """.trimIndent())

    fun `test simple generic closure with resolved T from struct`() = doTestCompletion("""
        module main
        
        struct Array<T> {}
        
        fn (a Array<T>) foo(cb fn (T)) int {
        	return 0
        }
        
        fn main() {
            a := Array<string>{}
        	a.foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module main
        
        struct Array<T> {}
        
        fn (a Array<T>) foo(cb fn (T)) int {
        	return 0
        }
        
        fn main() {
            a := Array<string>{}
        	a.foo(fn (EXPECTED_USER_INPUT_FOR_it string) {
        		
        	})
        }
    """.trimIndent())

    fun `test simple generic closure with resolved T and U from struct`() = doTestCompletion("""
        module main
        
        struct Array<T, U> {}
        
        fn (a Array<T, U>) foo(cb fn (T) U) int {
        	return 0
        }
        
        fn main() {
            a := Array<string, int>{}
        	a.foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module main
        
        struct Array<T, U> {}
        
        fn (a Array<T, U>) foo(cb fn (T) U) int {
        	return 0
        }
        
        fn main() {
            a := Array<string, int>{}
        	a.foo(fn (EXPECTED_USER_INPUT_FOR_it string) int {
        		
        	})
        }
    """.trimIndent())

    fun `test simple generic closure with resolved T from struct and unresolved U`() = doTestCompletion("""
        module main
        
        struct Array<T> {}
        
        fn (a Array<T>) foo<U>(cb fn (T) U) int {
        	return 0
        }
        
        fn main() {
            a := Array<string>{}
        	a.foo(fn/*caret*/)
        }
    """.trimIndent(), """
        module main
        
        struct Array<T> {}
        
        fn (a Array<T>) foo<U>(cb fn (T) U) int {
        	return 0
        }
        
        fn main() {
            a := Array<string>{}
        	a.foo(fn (EXPECTED_USER_INPUT_FOR_it string) EXPECTED_USER_INPUT_FOR_U {
        		
        	})
        }
    """.trimIndent())

    fun `test closure for field of generic struct init`() = doTestCompletion("""
        module main
        
        struct Array[T] {}
        
        struct User[T] {
            field fn (Array[T])
        }
        
        fn main() {
            User[string]{
                field: fn/*caret*/
            }
        }
    """.trimIndent(), """
        module main
        
        struct Array[T] {}
        
        struct User[T] {
            field fn (Array[T])
        }
        
        fn main() {
            User[string]{
                field: fn (EXPECTED_USER_INPUT_FOR_it Array[string]) {
        			
        		}
            }
        }
    """.trimIndent())

    fun `test closure inside function call for field of generic struct init`() = doTestCompletion("""
        module main
        
        struct Array<T> {}
        
        fn (a Array<T>) map<U>(cb fn (T) U) Array<U> {}
        
        struct User<T> {
            field fn (Array<T>)
        }
        
        fn main() {
            arr := Array<int>{}
            User<string>{
                field: arr.map(fn/*caret*/)
            }
        }
    """.trimIndent(), """
        module main
        
        struct Array<T> {}
        
        fn (a Array<T>) map<U>(cb fn (T) U) Array<U> {}
        
        struct User<T> {
            field fn (Array<T>)
        }
        
        fn main() {
            arr := Array<int>{}
            User<string>{
                field: arr.map(fn (EXPECTED_USER_INPUT_FOR_it int) EXPECTED_USER_INPUT_FOR_U {
        			
        		})
            }
        }
    """.trimIndent())

    fun `test closure with tuple return type`() = doTestCompletion("""
        module main
        
        pub fn to_map[K, V, X, Y](m map[K]V, f fn (key K, val V) (X, Y)) map[X]Y {
            mut mp := map[X]Y{}
        
            for k, v in m {
                x, y := f(k, v)
                mp[x] = y
            }
        
            return mp
        }
        
        fn main() {
            m := map[string]int{}
            to_map(m, fn/*caret*/)
        }
    """.trimIndent(), """
        module main
        
        pub fn to_map[K, V, X, Y](m map[K]V, f fn (key K, val V) (X, Y)) map[X]Y {
            mut mp := map[X]Y{}
        
            for k, v in m {
                x, y := f(k, v)
                mp[x] = y
            }
        
            return mp
        }
        
        fn main() {
            m := map[string]int{}
            to_map(m, fn (EXPECTED_USER_INPUT_FOR_key string, EXPECTED_USER_INPUT_FOR_val int) (EXPECTED_USER_INPUT_FOR_X, EXPECTED_USER_INPUT_FOR_Y) {
        		
        	})
        }
    """.trimIndent())
}
