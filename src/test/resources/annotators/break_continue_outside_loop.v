module annotators

fn main() {
	<error descr="'break' statement is outside the 'for' loop">break</error>
	<error descr="'continue' statement is outside the 'for' loop">continue</error>

	for {
		break
	}

	for {
		continue
	}

	for {
		for {
			continue
		}

		break
	}
}
