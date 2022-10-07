module outside

fn main() {
label:

	<warning descr="'go to' statement should be inside unsafe block">goto label</warning>

	unsafe {
		goto label // ok
	}
}
