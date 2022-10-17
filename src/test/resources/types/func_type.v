module types

struct Foo {}

fn foo(a string) string {}
fn foo1(a string) (int, string) {}
fn foo2(a string, b int) (int, string) {}
fn foo3(a string, b int) {}
fn foo4() {}

fn main() {
	expr_type(foo, "fn (string) string")
	expr_type(foo1, "fn (string) (int, string)")
	expr_type(foo2, "fn (string, int) (int, string)")
	expr_type(foo3, "fn (string, int)")
	expr_type(foo4, "fn ()")
	expr_type(fn () {}, "fn ()")
	expr_type(fn (int) {}, "fn (int)")
	expr_type(fn (int) string {}, "fn (int) string")
	expr_type(fn (int) (Foo, string) {}, "fn (int) (Foo, string)")
}
