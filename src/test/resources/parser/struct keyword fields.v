module main

struct Keywords {
	import     u8
	struct     u8
	implements u8
	union      u8
	interface  u8
	enum       u8
	const      u8
	type       u8
	fn         u8
	return     u8
	select     u8
	match      u8
	or         u8
	if         u8
	else       u8
	goto       u8
	assert     u8
	for        u8
	break      u8
	continue   u8
	unsafe     u8
	defer      u8
	go         u8
	spawn      u8
	rlock      u8
	lock       u8
	as         u8
	in         u8
	is         u8
	nil        u8
	none       u8
	static     u8
	shared     u8
	atomic     u8
	dump       u8
	sizeof     u8
	typeof     u8
	isreftype  u8
	module     u8
	true       u8
	false      u8
	pub        u8
	mut        u8
	volatile   u8
	//	asm       u8 // lexer issue
	// __global   u8 // fields starting from _ should not be supported
	// __offsetof u8 // fields starting from _ should not be supported
}

struct KeywordsWithModifiers {
	import     u8
pub:
	struct     u8
pub mut:
	implements u8
__global:
	return     u8
}

fn main() {
	test := Keywords {
		import:     12
		struct:     12
		implements: 12
		union:      12
		interface:  12
		enum:       12
		const:      12
		type:       12
		fn:         12
		return:     12
		select:     12
		match:      12
		or:         12
		if:         12
		else:       12
		goto:       12
		assert:     12
		for:        12
		break:      12
		continue:   12
		unsafe:     12
		defer:      12
		go:         12
		spawn:      12
		rlock:      12
		lock:       12
		as:         12
		in:         12
		is:         12
		nil:        12
		none:       12
		static:     12
		shared:     12
		atomic:     12
		dump:       12
		sizeof:     12
		typeof:     12
		isreftype:  12
		module:     12
		true:       12
		false:      12
		pub:        12
		mut:        12
		volatile:   12
		// asm:        12
		// __global:   12
		// __offsetof: 12
	}

	dump(test.pub)
	dump(test.import)
	dump(test.struct)
	dump(test.implements)
	dump(test.union)
	dump(test.interface)
	dump(test.enum)
	dump(test.const)
	dump(test.type)
	dump(test.fn)
	dump(test.return)
	dump(test.select)
	dump(test.match)
	dump(test.or)
	dump(test.if)
	dump(test.else)
	dump(test.goto)
	dump(test.assert)
	dump(test.for)
	dump(test.break)
	dump(test.continue)
	dump(test.unsafe)
	dump(test.defer)
	dump(test.go)
	dump(test.spawn)
	dump(test.rlock)
	dump(test.lock)
	dump(test.as)
	dump(test.in)
	dump(test.is)
	dump(test.nil)
	dump(test.none)
	dump(test.static)
	dump(test.shared)
	dump(test.atomic)
	dump(test.dump)
	dump(test.sizeof)
	dump(test.typeof)
	dump(test.isreftype)
	dump(test.module)
	dump(test.true)
	dump(test.false)
	dump(test.pub)
	dump(test.mut)
	dump(test.volatile)
	// dump(test.asm
	// dump(test.__global)
	// dump(test.__offsetof)
}
