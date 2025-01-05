<p align="center">
<img src="docs/cover.png">
</p>

![Build](https://github.com/vlang/intellij-v/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/24183-vlang.svg)](https://plugins.jetbrains.com/plugin/24183-vlang)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/24183-vlang.svg)](https://plugins.jetbrains.com/plugin/24183-vlang)

# [V](https://vlang.io) language plugin for JetBrains IDEs

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

## Installation & Usage

If you want to jump straight in, use following instructions:

### Using IDE built-in plugin system:

<kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Vlang"</kbd> >
<kbd>Install Plugin</kbd>

### From JetBrains Marketplace:

Visit [vlang](https://plugins.jetbrains.com/plugin/24183-vlang) plugin page and click on the <kbd>Install</kbd> button.

### Manually:

Build plugin locally or download pre-build version from [releases](https://github.com/vlang/intellij-v/releases). Install it manually using
<kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

> [!NOTE]  
> IDE restart might be required after the plugin installation.

## Building the plugin

To build the plugin clone the repository and run the following command:

```bash
./gradlew buildPlugin
```

The plugin will be built and saved to the `build/distributions` directory. To install it follow instructions from the [manual installation](https://github.com/vlang/intellij-v#building-the-plugin) section.

## License

This project is under the **MIT License**. See the
[LICENSE](https://github.com/vlang/intellij-v/blob/master/LICENSE)
file for the full license text.
