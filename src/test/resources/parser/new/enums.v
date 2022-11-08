module new

enum SmallEnum as i8 {
	a = -1
	b = -4
	c
	d = 5
	e
}

enum BigEnum as u64 {
	a = 0xABCD_EF09_1234_5678
	b = 0xFFFF_FFFF_FFFF_FFF0
	c
	d = 5
	e
}

enum BigIEnum as i64 {
	a = -999_999_999_999
	b = -900_000_000_000
	c
	d = 900_000_000_000
	e
}

pub enum SymbolKind {
	none_
	const_group
	constant
	variable
	function
	method
	interface_
	typedef
	enum_
	enum_field
	struct_
	struct_field
}

pub enum Platform {
	auto
	ios
	macos
	linux
	windows
	freebsd
	openbsd
	netbsd
	dragonfly
	js // for interoperability in prefs.OS
	android
	termux // like android, but note that termux is running on devices natively, not cross compiling from other platforms
	solaris
	serenity
	vinix
	haiku
	raw
	cross
}
