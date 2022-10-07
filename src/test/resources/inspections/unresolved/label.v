module unresolved

fn main() {
	loop: for i := 0; i < 10; i++ {
		for {
			if true {
				break <error descr="Unresolved label 'loop1'">loop1</error>
			}

			break loop
		}
	}

	goto loop
	goto <error descr="Unresolved label 'loop1'">loop1</error>
}
