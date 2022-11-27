module unused

import os
import os as os2
import v.ast { Alias }
// import and alias both is unused
import v.transformer // alias is unused
import v.embed_file as embed
// all selective imports not used, so delete whole import
import v.builder { compile } // only last is unused, delete only them
import io { read_all, read_any } // only middle is unused, delete only them
import v.errors { Error } // only first is unused, delete only them

fn main() {
	os.read_bytes('')!
	os2.read_bytes('')!

	Alias{}
	embed.EmbedFileData{}
	compile()!
	read_all()!
	read_any()!
	Error{}

	transformer.Transformer{}
}
