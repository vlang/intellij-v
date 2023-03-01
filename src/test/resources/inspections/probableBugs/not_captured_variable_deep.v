module probableBugs

fn main() {
	spawn fn (param string) {
		println(param)

		fn (param2 string) {
			println(<error descr="Parameter 'param' is not captured">param</error>)
			println(param2)

			fn () {
				println(<error descr="Parameter 'param2' is not captured">param2</error>)
			}()
		}()
	}('')
}
