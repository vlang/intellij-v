module types

fn foo(a string) string {}
fn foo1(a string) (int, string) {}
fn foo2(a string, b int) (int, string) {}

fn main() {
	expr_type(foo, "fn (string) string")
	expr_type(foo1, "fn (string) (int, string)")
	expr_type(foo2, "fn (string, int) (int, string)")
}
