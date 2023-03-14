// Copyright (c) 2023 Petr Makhnev. All rights reserved.
// Use of this source code is governed by a MIT
// license that can be found in the LICENSE file.
module stubs

// This file contains definitions of compile time reflection.

// $Int describes any integer type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Int {
//     println(f.name)
//   }
// }
// ```
pub const $Int = TypeInfo{}

// $Float describes any float type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Float {
//     println(f.name)
//   }
// }
// ```
pub const $Float = TypeInfo{}

// $Array describes any array type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Array {
//     println(f.name)
//   }
// }
// ```
pub const $Array = TypeInfo{}

// $Map describes any map type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Map {
//     println(f.name)
//   }
// }
// ```
pub const $Map = TypeInfo{}

// $Struct describes any struct type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Struct {
//     println(f.name)
//   }
// }
// ```
pub const $Struct = TypeInfo{}

// $Interface describes any interface type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Interface {
//     println(f.name)
//   }
// }
// ```
pub const $Interface = TypeInfo{}

// $Enum describes any enum type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Enum {
//     println(f.name)
//   }
// }
// ```
pub const $Enum = TypeInfo{}

// $Alias describes any alias type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Alias {
//     println(f.name)
//   }
// }
// ```
pub const $Alias = TypeInfo{}

// $Sumtype describes any sumtype type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Sumtype {
//     println(f.name)
//   }
// }
// ```
pub const $Sumtype = TypeInfo{}

// $Function describes any function type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Function {
//     println(f.name)
//   }
// }
// ```
pub const $Function = TypeInfo{}

// $Option describes any option type.
//
// Example:
// ```
// $for f in Test.fields {
//   $if f.typ is $Option {
//     println(f.name)
//   }
// }
// ```
pub const $Option = TypeInfo{}

struct CompileTimeTypeInfo {
pub:
	// fields describes the list of structure fields.
	// This field can only be used inside `$for`.
	//
	// Example:
	// ```v
	// struct Foo {
	//   a int
	//   b string
	// }
	//
	// fn main() {
	//   $for field in Foo.fields {
	//     println(field.name)
	//   }
	// }
	// ```
	fields []FieldData
}
