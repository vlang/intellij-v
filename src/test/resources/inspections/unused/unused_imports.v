module unused

import os
import os as os2
<warning descr="Unused import 'arrays'">import arrays</warning>
import v.ast { Alias }
<warning descr="Unused import 'cflag'">import v.cflag</warning>
<warning descr="Unused import 'depgraph'">import v.depgraph <warning descr="Unused import alias">as dep</warning></warning> // import and alias both is unused
import v.transformer <warning descr="Unused import alias">as transform</warning> // alias is unused
import v.embed_file as embed
<warning descr="Unused import 'doc'">import v.doc { <warning descr="Unused imported symbol 'ast_comment_to_doc_comment'">ast_comment_to_doc_comment</warning> }</warning> // all selective imports not used, so delete whole import
import v.builder { compile, <warning descr="Unused imported symbol 'new_builder'">new_builder</warning> } // only last is unused, delete only them
import io { read_all, <warning descr="Unused imported symbol 'new_multi_writer'">new_multi_writer</warning>, read_any } // only middle is unused, delete only them
import v.errors { <warning descr="Unused imported symbol 'CompilerMessage'">CompilerMessage</warning>, Error } // only first is unused, delete only them

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
