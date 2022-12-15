package org.vlang.ide.test.configuration

enum class VlangTestScope {
    Directory,
    File,
    Function;

    companion object {
        fun from(s: String): VlangTestScope = VlangTestScope.values().find { it.name == s } ?: Directory
    }
}
