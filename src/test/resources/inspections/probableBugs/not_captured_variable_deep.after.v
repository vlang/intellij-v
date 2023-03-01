module probableBugs

fn main() {
	spawn fn (param string) {
		println(param)

		fn [param] (param2 string) {
			println(param)
			println(param2)

			fn [param2] () {
				println(param2)
			}()
		}()
	}('')
}
