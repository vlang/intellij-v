module new

import v.cflag

struct Empty {}

struct GenericEmpty<T> {}

struct Result<T, E> {}

struct Single<T> {
	value T
}

struct Multiple<T> {
	value T
	next  Multiple<T>
}

struct SinglePublic {
pub:
	value Single<int>
}

struct SingleMutablePublic {
pub mut:
	value Single<int>
}

struct MultiplePublic {
pub:
	value Multiple<int>
	next  Multiple<T>
}

struct MultiplyGroups {
pub:
	value Multiple<int>
pub mut:
	next Multiple<T>
}

struct FirstNonGroup {
	value Multiple<int>
mut:
	next Multiple<T>
}

struct GlobalField {
__global:
	value int
}

struct MutliplyFields {
	// value, value2 int // not allowed
}

struct SharedTypeField {
mut:
	value shared Multiple<int>
}

struct ComplexFields {
	value        string        [sql: 'varchar(255)']
	with_default string = 'default'
	both         shared string [sql: 'varchar(255)'] = 'default'
}

// Complex example
[heap; minify]
pub struct Table {
mut:
	parsing_type string // name of the type to enable recursive type parsing
pub mut:
	type_symbols       []&TypeSymbol
	type_idxs          map[string]int
	fns                map[string]Fn
	iface_types        map[string][]Type
	dumps              map[int]string // needed for efficiently generating all _v_dump_expr_TNAME() functions
	imports            []string       // List of all imports
	modules            []string       // Topologically sorted list of all modules registered by the application
	global_scope       &Scope = unsafe { nil }
	cflags             []cflag.CFlag
	redefined_fns      []string
	fn_generic_types   map[string][][]Type // for generic functions
	interfaces         map[int]InterfaceDecl
	cmod_prefix        string // needed for ast.type_to_str(Type) while vfmt; contains `os.`
	is_fmt             bool
	used_fns           map[string]bool // filled in by the checker, when pref.skip_unused = true;
	used_consts        map[string]bool // filled in by the checker, when pref.skip_unused = true;
	used_globals       map[string]bool // filled in by the checker, when pref.skip_unused = true;
	used_vweb_types    []Type // vweb context types, filled in by checker, when pref.skip_unused = true;
	used_maps          int    // how many times maps were used, filled in by checker, when pref.skip_unused = true;
	panic_handler      FnPanicHandler = default_table_panic_handler
	panic_userdata     voidptr        = unsafe { nil } // can be used to pass arbitrary data to panic_handler;
	panic_npanics      int
	cur_fn             &FnDecl = unsafe { nil } // previously stored in Checker.cur_fn and Gen.cur_fn
	cur_concrete_types []Type  // current concrete types, e.g. <int, string>
	gostmts            int     // how many `go` statements there were in the parsed files.
	// When table.gostmts > 0, __VTHREADS__ is defined, which can be checked with `$if threads {`
	enum_decls        map[string]EnumDecl
	module_deprecated map[string]bool
	module_attrs      map[string][]Attr // module attributes
	builtin_pub_fns   map[string]bool
	pointer_size      int
	// cache for type_to_str_using_aliases
	cached_type_to_str map[u64]string
	anon_struct_names  map[string]int // anon struct name -> struct sym idx
	// counter for anon struct, avoid name conflicts.
	anon_struct_counter int
}
