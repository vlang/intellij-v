module main

import x

$match @OS {
	'linux' {
		const os = 'linux'
	}
	'windows' {
		const os = 'windows'
	}
	$else {
		const os = 'other'
	}
}


fn func[T]() bool {
	$match T {
		u8, u16 {
			return true
		}
		$else {
			// return false
			$compile_error('fail')
		}
	}
}

fn test[T]() bool {
	return $match T {
	i8 { true }
	i16 { true }
	$else { $compile_error('unsupported type') }
	}
}

struct My {
	a int
	b f64
	c string
}

fn main() {
	a := 10
	$match a {
		10 { println('10') }
		20, 30 { println('10') }
		$else { println('not 10') }
	}

	$match typeof(a) {
		int { println('int') }
		$else { println('not int') }
	}

	x := My{}
	$for f in x.fields {
		f_name := f.name
		$match $int {
			f.typ {
				println('${f_name}=int,')
			}
		}
	}
}
