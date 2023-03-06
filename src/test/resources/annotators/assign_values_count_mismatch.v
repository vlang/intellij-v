module annotators

fn foo() {}

<error descr="Expected 2 values on the right side, got 1">a, b := 1</error>
a1, b1 := 1, 2
<error descr="Expected 2 values on the right side, got 3">a2, b2 := 1, 2, 3</error>
a3 := 2
<error descr="Expected 1 values on the right side, got 2">a4 := 1, 2</error>
a5 := 1, foo() // don't check currently

for index, token in tokens {
}
