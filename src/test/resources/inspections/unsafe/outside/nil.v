module outside

fn main() {
	println(<warning descr="'nil' must be used only inside unsafe block">nil</warning>)

	unsafe {
		println(nil) // ok
	}
}
