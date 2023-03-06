# IntelliJ V Changelog

## [Unreleased]

## [0.0.1-beta.3] - 06.03.2023

### Added

- Added struct fields in Quick Documentation
- Added fields and methods to Quick Documentation for interfaces
- Added type methods in Quick Documentation (works only for declarations)
- Added highlighting for `break` and `continue` inside `for` and `select`
  If the `for` is marked with a label, then the label that is specified in
  the current `continue` or `break` will be highlighted.
- Added documentation for compile-time `fields` field
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
    - added pretty printer for `stringg.Builder`
    - show values for array of pointers
    - show custom `str()` method result right after name
    - show aliases for string as string
    - improved handling for maps
    - improved names in call stack
    - don't show `mr_*` variables
    - don't show thread__t1 and arg__t1 variables
- Improved modifier list handling inside structs
    - unindent modifiers list in structs when type `:` after it
    - improved completion for modifiers in struct
    - warn about duplicate modifier in list
    - warn about duplicate modifiers group in struct
- `continue` and `break` inside loops higher in the autocomplete list
- Improved completion for the top level, don't show expressions if there is a function, structure, etc. next.
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

- Added caching for `resolve()` for types, it should speed up resolving for big projects.
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
