// Copyright (c) 2022 Petr Makhnev. All rights reserved.
// Use of this source code is governed by a MIT
// license that can be found in the LICENSE file.
module stubs

// AnyStruct is any structure in code.
//
// It is needed to define all implicit methods of structures.
struct AnyStruct {}

// str returns a string representation of the structure.
//
// **Note**
//
// This method is implicitly implemented by any struct,
// you can override it for your struct:
// ```
// struct MyStruct {
//     name string
// }
//
// pub fn (s MyStruct) str() string {
//     return s.name
// }
// ```
//
// Example:
//
// ```
// struct Foo {}
//
// fn main() {
//     s := Foo{}
//     println(s.str()) // Foo{}
// }
// ```
pub fn (a AnyStruct) str() string
