package org.vlang.lang.completion

class StructInitCompletionTest : CompletionTestBase() {
    fun `test single field`() = checkIncludes(
        """
        module main
        
        struct Foo {
            name string
        }
        
        fn get() {}
        
        fn main() {
            arr := Foo{/*caret*/}
        }

        """.trimIndent(),
        0,
        "name", "get", // first field can be any expression
    )

    fun `test several field`() = checkIncludes(
        """
        module main
        
        struct Foo {
            name  string
            age   int
            other bool
        }
        
        fn get() {}
        
        fn main() {
            arr := Foo{/*caret*/}
        }

        """.trimIndent(),
        0,
        "name", "age", "other", "get", // first field can be any expression
    )

    fun `test second in several field`() = checkEquals(
        """
        module main
        
        struct Foo {
            name  string
            age   int
            other bool
        }
        
        fn get() {}
        
        fn main() {
            arr := Foo{name: 'hello', /*caret*/}
        }

        """.trimIndent(),
        0,
        "age", "other", // second and further field can be only fields from struct
        "", // Fill all fields...
    )

    fun `test second in several field with public one`() = checkEquals(
        """
        module main
        
        struct Foo {
            name  string
            age   int
        pub:
            other bool
        }
        
        fn get() {}
        
        fn main() {
            arr := Foo{name: 'hello', /*caret*/}
        }

        """.trimIndent(),
        0,
        "age", "other", // second and further field can be only fields from struct
        "", // Fill all fields...
    )

    fun `test fill all fields different types`() = doTestCompletion(
        """
        module main
        
        struct Foo {
        	name       string
        	age        int
        	float      f32
        	run        rune
        	char_ptr   charptr
        	bool_val   bool
        	mp         map[string]int
        	arr        []string
        	fixed_arr  [3]string
        	ch         chan int
        	pt         &Foo
        	str_or_int StringOrInt
        	cb         fn (n map[string]int) string
        	my_str     String
        	other      Boo
        }
        
        fn main() {
        	arr := Foo{/*caret*/}
        }
        
        """.trimIndent(),
        """
        module main
        
        struct Foo {
        	name       string
        	age        int
        	float      f32
        	run        rune
        	char_ptr   charptr
        	bool_val   bool
        	mp         map[string]int
        	arr        []string
        	fixed_arr  [3]string
        	ch         chan int
        	pt         &Foo
        	str_or_int StringOrInt
        	cb         fn (n map[string]int) string
        	my_str     String
        	other      Boo
        }
        
        fn main() {
        	arr := Foo{
        		name: ''
        		age: 0
        		float: 0.0
        		run: `0`
        		char_ptr: unsafe { nil }
        		bool_val: false
        		mp: {}
        		arr: []
        		fixed_arr: []!
        		ch: chan int{}
        		pt: unsafe { nil }
        		str_or_int: 0
        		cb: fn (n map[string]int) string {}
        		my_str: 0
        		other: 0
        	}
        }

        """.trimIndent(),
    )

    fun `test fill all fields simple`() = doTestCompletion(
        """
        module main
        
        struct Foo {
            name  string
            age   int
        pub:
            other bool
        }
        
        fn main() {
        	arr := Foo{/*caret*/}
        }
        
        """.trimIndent(),
        """
        module main
        
        struct Foo {
            name  string
            age   int
        pub:
            other bool
        }
        
        fn main() {
        	arr := Foo{
        		name: ''
        		age: 0
        		other: false
        	}
        }
        
        """.trimIndent(),
    )

    fun `test fill all fields simple 2`() = doTestCompletion(
        """
        module main
        
        struct Foo {
            name  string
            age   int
        pub:
            other bool
        }
        
        fn main() {
        	arr := Foo{
        		/*caret*/
        	}
        }
        
        """.trimIndent(),
        """
        module main
        
        struct Foo {
            name  string
            age   int
        pub:
            other bool
        }
        
        fn main() {
        	arr := Foo{
        		name: ''
        		age: 0
        		other: false
        	}
        }
        
        """.trimIndent(),
    )

    fun `test fill all fields with @`() = doTestCompletion(
        """
        module main
        
        struct Foo {
            name  string
            @age   int
        pub:
            @other bool
        }
        
        fn main() {
        	arr := Foo{/*caret*/}
        }
        
        """.trimIndent(),
        """
        module main
        
        struct Foo {
            name  string
            @age   int
        pub:
            @other bool
        }
        
        fn main() {
        	arr := Foo{
        		name: ''
        		@age: 0
        		@other: false
        	}
        }
        
        """.trimIndent(),
    )

    fun `test fill all fields in incomplete init`() = doTestCompletion(
        """
        module main
        
        struct Foo {
            name  string
            age   int
        pub:
            other bool
        }
        
        fn main() {
        	arr := Foo{
        		name: 'hello'
        		/*caret*/
        	}
        }
        
        """.trimIndent(),
        """
        module main
        
        struct Foo {
            name  string
            age   int
        pub:
            other bool
        }
        
        fn main() {
        	arr := Foo{
        		name: 'hello'
        		age: 0
        		other: false
        	}
        }
        
        """.trimIndent(),
    )

    fun `test fill all fields for enum`() = doTestCompletion(
        """
        module main
        
        enum EnumValue {
            red
        }
        
        enum EnumValue2 {
            green
            blue
        }
        
        struct Foo {
            val  EnumValue
            val2 EnumValue2
        }
        
        fn main() {
            arr := Foo{/*caret*/}
        }
        
        """.trimIndent(),
        """
        module main
        
        enum EnumValue {
            red
        }
        
        enum EnumValue2 {
            green
            blue
        }
        
        struct Foo {
            val  EnumValue
            val2 EnumValue2
        }
        
        fn main() {
            arr := Foo{
        		val: .red
        		val2: .green
        	}
        }
        
        """.trimIndent(),
    )
}
