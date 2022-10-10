module unused

import os
import os as os2
import v.ast { Alias }
import v.embed_file as embed
import v.doc { ast_comment_to_doc_comment } // ast_comment_to_doc_comment not used, TODO

fn main() {
	os.read_bytes('')
	os2.read_bytes('')

	Alias{}
	embed.EmbedFileData{}
}
