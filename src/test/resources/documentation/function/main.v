module function

fn /*caret*/foo() {

}

fn /*caret*/foo1(a int) {

}

pub fn /*caret*/foo2(a int, b string) {

}

// foo3 is a function with a return value
fn /*caret*/foo3(a int, b string, /*caret*/c f64) string {

}

fn /*caret*/foo4<T>(a T) T {

}

fn /*caret*/foo5(mut a string) string {

}

// foo6 is a function with a return value
// and some
// long documentation
pub fn /*caret*/foo6(mut /*caret*/a string, shared b int, mut shared c string) !string {

}

fn /*caret*/foo7(mut a string, /*caret*/b shared int) !string {

}

fn /*caret*/foo8() ! {

}

fn /*caret*/foo9() (int, string) {

}

fn /*caret*/foo10(mut /*caret*/a ...string) {

}

fn /*caret*/foo11(s int, /*caret*/a ...string) {

}
