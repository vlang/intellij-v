# IntelliJ V Changelog

## [Unreleased]

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
