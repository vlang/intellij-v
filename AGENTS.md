This project is adding V language support to the intellij platform as a plugin.

Use Kotlin whenever possible.

Files in src/main/gen are generated automatically and do not modify them directly.

Parser is generated using the grammar file located at src/main/kotlin/io/vlang/lang/grammar/v.bnf which uses the Grammar-Kit format.
Lexer is generated using the lexer file located at src/main/kotlin/io/vlang/lang/lexer/v.flex which uses the JFlex format.

Do not add grammarkit plugin to gradle.
If you need to generate parser/lexer files, ask me to do it for you or if intellj mcp is avaiable use `ctrl+shift+g` on v.bnf or v.flex files.
