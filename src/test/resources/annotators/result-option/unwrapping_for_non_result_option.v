module main

fn foo() int {}
fn opt() ?int {}
fn res() !int {}

if a := <error descr="Cannot unwrap non-Option and non-Result type in 'if' expression">foo()</error> {

}

if a := opt() {

}

if a := res() {

}

if a := <error descr="Cannot unwrap non-Option and non-Result type in 'if' expression">100</error> {

}

mp := map[string]int{}
if a := mp["foo"] {

}

ch := chan int{}
if a := <-ch {

}
