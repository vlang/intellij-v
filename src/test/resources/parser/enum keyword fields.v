module main

enum Keywords {
	module
	import
	struct
	implements
	union
	interface
	enum
	const
	type
	fn
	return
	select
	match
	or
	if
	else
	goto
	assert
	for
	break
	continue
	unsafe
	defer
	go
	spawn
	rlock
	lock
	as
	in
	is
	nil
	true
	false
	none
	pub
	mut
	static
	shared
	volatile
	atomic
	__global
	dump
	sizeof
	typeof
	isreftype
	__offsetof
	// asm // tokenizer pushes ASM_BLOCK state which prevents normal tokenization
}

fn main() {
	test := Keywords.fn
}