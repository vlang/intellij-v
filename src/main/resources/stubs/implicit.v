// Copyright (c) 2022 Petr Makhnev. All rights reserved.
// Use of this source code is governed by a MIT
// license that can be found in the LICENSE file.
module stubs

// Any is any type in code.
//
// It is needed to define all implicit methods of all types.
type Any = any

// str returns a string representation of the type.
//
// **Note**
//
// This method is implicitly implemented by any type,
// you can override it for your type:
// ```
// struct MyStruct {
//   name string
// }
//
// pub fn (s MyStruct) str() string {
//   return s.name
// }
// ```
//
// Example:
//
// ```
// struct Foo {}
//
// fn main() {
//   s := Foo{}
//   println(s.str()) // Foo{}
//
//   mp := map[string]int{}
//   println(mp.str()) // map[string]int{}
// }
// ```
pub fn (a Any) str() string
