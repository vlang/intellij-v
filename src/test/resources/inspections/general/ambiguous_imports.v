module main

import os
import <error descr="Ambiguous import 'os', is already imported earlier">os</error>
import os as os2

import v.doc
import <error descr="Ambiguous import 'doc', is already imported earlier">v.doc</error>
import <error descr="Ambiguous import 'doc', is already imported earlier">v.doc { Doc }</error>
import v.doc as vdoc

fn main() {
	os.args
	os2.args

	doc.Doc{}
	vdoc.Doc{}
}
