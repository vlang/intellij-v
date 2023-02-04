// Copyright (c) 2023 Petr Makhnev. All rights reserved.
// Use of this source code is governed by a MIT
// license that can be found in the LICENSE file.
module stubs

// This file contains definitions of compile time reflection.

pub const $Int = TypeInfo{}
pub const $Float = TypeInfo{}
pub const $Array = TypeInfo{}
pub const $Map = TypeInfo{}
pub const $Struct = TypeInfo{}
pub const $Interface = TypeInfo{}
pub const $Enum = TypeInfo{}
pub const $Alias = TypeInfo{}
pub const $Sumtype = TypeInfo{}
pub const $Funtion = TypeInfo{}

struct CompileTimeTypeInfo {
pub:
	fields []FieldData
}
