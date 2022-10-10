module unused

import os
import os as os2
<warning descr="Unused import 'arrays'">import arrays</warning>
import v.ast { Alias }
<warning descr="Unused import 'builder'">import v.builder</warning>
<warning descr="Unused import 'dep'">import v.depgraph as dep</warning>
import v.embed_file as embed
import v.doc { ast_comment_to_doc_comment } // ast_comment_to_doc_comment not used, TODO

fn main() {
	os.read_bytes('')
	os2.read_bytes('')

	Alias{}
	embed.EmbedFileData{}
}
