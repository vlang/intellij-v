module first

import <error descr="Circular import detected">second</error>

pub fn util(){
	second.util()
}
