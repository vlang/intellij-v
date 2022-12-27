module builtin

struct string {
pub:
	len int
}

pub fn (s string) contains(val string) bool
pub fn (s string) trim_space() string