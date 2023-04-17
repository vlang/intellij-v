<p align="center">
<img src="docs/cover.png">
</p>

[![Association Official Project][AssociationOfficialBadge]][AssociationUrl]
![](https://img.shields.io/badge/Stable%20Release-01.07.2023-orange?logo=semantic-release)
![Build](https://github.com/i582/vlang-idea/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/20287-vlang.svg)](https://plugins.jetbrains.com/plugin/20287-vlang)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/20287-vlang.svg)](https://plugins.jetbrains.com/plugin/20287-vlang)

# V language plugin for JetBrains IDEs

## About

This plugin adds support of [V](https://vlang.io) language to JetBrains IDEs.

## Installation & Usage

For installation options, see the
[Quick Start Guide](https://plugins.jetbrains.com/plugin/20287-vlang/docs/#install-update-the-plugin).
If you want to jump straight in, use following instructions:

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Vlang"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/i582/vlang-idea/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

All the plugin's features are described in [documentation](https://plugins.jetbrains.com/plugin/20287-vlang/docs).

## Compatible IDEs

The plugin is compatible with all IntelliJ-based IDEs starting from the version 2022.3, with the following differences
in the sets of the available features:

|                  | Open-source and Educational IDEs<sup>*</sup> | [CLion] (commercial) | [IntelliJ IDEA] Ultimate, [PyCharm] Professional, [GoLand] (commercial) | [WebStorm], [PhpStorm], other commercial IDEs |
|------------------|----------------------------------------------|----------------------|-------------------------------------------------------------------------|-----------------------------------------------|
| Language support | +                                            | +                    | +                                                                       | +                                             |
| Debugger         | -                                            | +                    | +**                                                                     | -                                             |

\* [IntelliJ IDEA] Community Edition, [PyCharm] Community Edition, [PyCharm Edu and IntelliJ IDEA Edu].

\** Requires the
[Native Debugging Support](https://plugins.jetbrains.com/plugin/12775-native-debugging-support) plugin.
LLDB only

## License

This project is under the **MIT License**. See the
[LICENSE](https://github.com/i582/vlang-idea/blob/master/LICENSE)
file for the full license text.


[AssociationOfficialBadge]: https://vosca.dev/badge.svg

[AssociationUrl]: https://vosca.dev
