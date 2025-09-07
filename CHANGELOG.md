# IntelliJ V Changelog

## [Unreleased]

- Allow attributes on enum values. [#38](https://github.com/vlang/intellij-v/issues/38)
- Fix a freeze after clicking button to add a toolchain. [#37](https://github.com/vlang/intellij-v/issues/37)
- Allow newlines in between operators in expressions. [#34](https://github.com/vlang/intellij-v/issues/34)
- Introduce links in console when program panic's and shows backtrace. [#12](https://github.com/vlang/intellij-v/issues/12)
- Basic support for static methods. [#24](https://github.com/vlang/intellij-v/issues/24)
  - Please file issues for missing features.
- Update compile time pseudo variables:
  - Add @DIR, @LOCATION, @VCURRENTHASH, @VMODHASH, @BUILD_DATE, @BUILD_TIME, @BUILD_TIMESTAMP, 
    @OS, @CCOMPILER, @BACKEND, @PLATFORM     

## [2025.2.1] - 2025-08-09

- Support for intellij platform 2025.2
- Fix default run configuration to use current working directory

## [2025.1.1] - 2025-04-29

- Support for intellij platform 2025.1
  - Remove pluginUntilBuild setting - plugin if compatible should be usable across new major version releases
- Fix 'Slow operations are prohibited on EDT' error after project opening
- Fix mismatch of indexes and psi after `v up` command
- Change Snake case implementation to covert all caps as single word
- Fix IndexOutOfBoundsException while typing `}` at the end of the document
- Change order of default value and attribute. Fixes [#22](https://github.com/vlang/intellij-v/issues/22)
- Builtin functions will not show private members in code completion. Fixes [#9](https://github.com/vlang/intellij-v/issues/9)
- Add WASM. prefix
- Remove restrictions for C., JS. completions
- Refactor RunConfiguration:
  - Add validation
  - Use relative paths to working directory
  - Use `directory` as default run kind
  - Expand environment variables and pass to build and run/debug configuration
  - Add macro support

## [2024.3.1] - 2024-11-14

- Support for intellij platform 2024.3
- Fix circular dependency error
- Introduce new implements keyword for interface declarations
- Keywords can now be used in enums
- Around 60 deprecation fixes

## [0.0.1-beta.6]

- Support for IntelliJ Platform 2024.2
- Faster indexing (and smaller index) by filtering out test files from vlib
- Launch search for vlang installations in coroutine
- Support for static method syntax
  - it will not be highlighted as an error
  - no completions yet

### Build system

- update Gradle to 8.9
- move to gradle intellij platform plugin version 2.0.1

## [0.0.1-beta.5]

### Changed

- Support for IntelliJ Platform 2024.1
- Support for new attribute syntax (like `@[inline]`). Old syntax is still supported.

## [0.0.1-beta.4] - 17.04.2023

### Added

- Added initial debugger expression evaluation
- Added highlighting elements in doc comments
- Added `...` in Quick Documentation for vararg parameters
- Added warning for enum without fields
- Added support for new `index` variable inside array initialization and deprecated `it` variable (with quick fix)
- Added documentation for `$option`, `$int` etc.
- Add notification about new version

### Changed

- Now rename of element will also rename it name in doc comment
- Don't highlight parameters and receivers as unused in functions with `$tmpl` call
- Updated ORM handling for latest V changes
- Allow `@name` in module declaration
- Now, if the module name is a keyword, then plugin create a file with a name starting with "@"
- Check `v fmt` checkbox in commit dialog by default
- Place code vision on right side
- Don't show generated `defer` variables in debugger

### Improved

- Pretty printers for array and structs in some cases
- Handling project with **v.mod** in sub folders
- Naming convention inspection now checks variable names as well (thank you @saturn4er!)
- In debugger, now `NULL` displays as `nil`
- Values (numbers, strings, pointers) now highlight in debugger
- Function types now displayed in more readable way in debugger
- Enums with alignment now also displayed in debugger in correct way
- On Windows primitive types also displayed as `u32`/`i32`/etc. instead of `int` or `unsigned` in debugger
- Support for maps and arrays with pointer type values in debugger
- Don't step into some generated functions when debug
- Add name and address to pretty printer for interfaces in debugger

### Fixed

- `Fill all fields...` now uses first enum value as default value
- Several exceptions in different cases
- Missing Function name quickfix for empty comment
- False positive for Missing Function name inspection with multiline comment
- Type inference indexing of map pass by reference
- Build events parser
- Struct init parsing inside `for` without last statement
- Module name inspection now not warn about root module name
- Don't show completion variants when type literal value like `100`
- Don't warn about count arguments for unknown C methods/functions
- Bug with `str()` call in debugger for `strings.Builder`
- Case for compile-time constants like `$Option` or `$Int`

## [0.0.1-beta.3-231-EAP] - 06.03.2023

### Added

- Added struct fields in Quick Documentation
- Added fields and methods to Quick Documentation for interfaces
- Added type methods in Quick Documentation (works only for declarations)
- Added highlighting for `break` and `continue` inside `for` and `select`
  If the `for` is marked with a label, then the label that is specified in
  the current `continue` or `break` will be highlighted
- Added documentation for compile-time `fields` field
- Added documentation for `cap` field in `chan` init
- Added call count mismatch inspection
- Added initial name generation when refactor or rename
- Added `fori` postfix template
- Added support for `_likely_`/`_unlikely_` with documentation
- Added type inference for `10 << 100`
- Added completion for plain test functions
- Added renaming of all receivers
- Added Receiver names inspection
    - receiver name is generic (self, me, this)
    - receiver names are different
- New checkers:
    - `continue` and `break` outside `for`
    - default value for interface field is not allowed
    - recursive struct is not allowed
    - `err` is always none when expression is not Result
    - cannot propagate result of function with non-Result and non-Option type
    - cannot unwrap non-Result and non-Option type
    - use `!` to propagate Result type and `?` to propagate Option type
    - expression for `or` block has non-Result and non-Option type
    - send to non-channel type
    - cannot use `for i in mp` for maps
    - interface types not allowed inside sum type
    - captured list is empty
    - captured variable has the same name as one of anon function parameter
    - plugin parse `*Foo` as a pointer type and give an error that the real syntax is `&Foo`
    - duplicate embedded struct
    - struct embedding is not first in field list
    - duplicate modifier in list
    - duplicate modifiers group in struct
    - mismatch count values in assignments

### Improved

- When typing `}` for string interpolation, no character is entered if the next character is already `}`
- For maps, `for` postfix template now generates `for key, value in mp` instead of `for key in mp`
- `a << 100` now parsed as AppendExpression
- ReassignImmutableSymbol inspection now works for `a << 100`
- ReassignImmutableSymbol inspection now check captured variables
- ReassignImmutableSymbol inspection now check index expressions
- Improved not captured variable inspection
    - added support for receivers
    - added support for deep nested anon functions
- Improved debugger
    - added variable with all captured variables in current anon function
    - added pretty printer for `strings.Builder`
    - show values for array of pointers
    - show custom `str()` method result right after name
    - show aliases for string as string
    - improved handling for maps
    - improved names in call stack
    - don't show `mr_*` variables
    - don't show `thread__*` and `arg__*` variables
- Improved modifier list handling inside structs
    - unindent modifiers list in structs when type `:` after it
    - improved completion for modifiers in struct
    - warn about duplicate modifier in list
    - warn about duplicate modifiers group in struct
- `continue` and `break` inside loops higher in the autocomplete list
- Improved completion for the top level, don't show expressions if there is a function, structure, etc. next
- Improved postfix completion, added `unsafe` and `not` postfix templates and improved `nil` completion in safe context
- Improved `chan` init fields autocomplete

### Fixed

- Fixed live templates
- Fixed type inference for `for` over aliased array or map type
- Fixed highlighting of primitive types
- Fixed trailing struct resolving for pointer types
- Fixed resolving for compile-time `fields` field
- Fixed `str()` resolving
- Fixed completion for structs/types from unimported modules
- Fixed Implement Interface action
    - when generating methods to implement an interface, do not use fqn, but add imports and use short names
    - when regenerating interface methods, be aware that `mut x Foo` may also implement an interface method that is not
      mutable and same for fields
    - fixed generation of types for fields
- Fixed `err` variable resolving inside a nested if inside an if guard in an `else` block
- Fixed enum field resolving for struct init with `...var`
- Fixed `err` resolving inside call expr with `or` block
- Fixed `it` resolving if it used in inner if expression

### Changed

- Don't show `err` hint inside else or `or` block when no Result type
- Don't show hints for some obvious cases (`a := Foo{}`, `b := true`, `c := "hello"`)
- Don't highlight `_` as unused receiver
- Don't show `_` in autocompletion
- Don't warn about access to global variable
- Don't warn about unhandled option types
- Don't auto import main module in any case
- Don't add parentheses in function completion when the context type is a function

## [0.0.1-beta.2] - 06.02.2023

### Added

- Added caching for `resolve()` for types, it should speed up resolving for big projects
- Added auto import for module when complete global variable name
- Added context completion for enum fields
- Added `dump()` postfix template
- Added handling of compile time reflection
- Added definitions of constants like linux or android
- Added completion for `$if`, `$else`, `$for`
- Added "V fmt" checkbox on commit
- Added initial deprecation processing
- Added initial smartcasts support
- Added new "forc" template for `$for field in T.fields`

### Improved

- Improved shared type handling
- Improved unsafe context detection
- Improved compile time handling
- Improved live templates
- Extended VariableNotCaptured inspection for parameters
- Highlighting when indexing in progress

### Fixed

- Now plugin don't process stub and test files when search for supers or inheritors
- Fixed resolving for methods for same types in different folders with equal module name
- Fixed parsing of `foo.bar` inside SQL
- Fixed type inference for globals without value
- Fixed visibility of global variables in other modules
- Fixed struct completion for aliased types
- Fixed false positive in assignments for RawOptionOrResultTypeUsed inspection
- Fixed some bugs in ReassignImmutableSymbol inspection
- Fixed cast exception
- Fixed broken Java debugging with installed plugin

### Changed

- Increased v fmt timeout to 5 seconds to prevent exception on first run
- Don't show `void` type in completion and quick doc
- Don't show `private` keyword in quick documentation
- Support 2023.1 EAP

[Unreleased]: https://github.com/vlang/intellij-v//compare/v2025.2.1...HEAD
[2025.2.1]: https://github.com/vlang/intellij-v//compare/v2025.1.1...v2025.2.1
[2025.1.1]: https://github.com/vlang/intellij-v//compare/v2024.3.1...v2025.1.1
[2024.3.1]: https://github.com/vlang/intellij-v//compare/v0.0.1-beta.6...v2024.3.1
[0.0.1-beta.6]: https://github.com/vlang/intellij-v//compare/v0.0.1-beta.5...v0.0.1-beta.6
[0.0.1-beta.5]: https://github.com/vlang/intellij-v//compare/v0.0.1-beta.4...v0.0.1-beta.5
[0.0.1-beta.4]: https://github.com/vlang/intellij-v//compare/v0.0.1-beta.3-231-EAP...v0.0.1-beta.4
[0.0.1-beta.3-231-EAP]: https://github.com/vlang/intellij-v//compare/v0.0.1-beta.2...v0.0.1-beta.3-231-EAP
[0.0.1-beta.2]: https://github.com/vlang/intellij-v//commits/v0.0.1-beta.2
