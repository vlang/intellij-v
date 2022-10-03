package org.vlang.toolchain

import com.intellij.openapi.project.Project

class VlangToolchain {
    fun vfmt() = Vfmt()

    companion object {
        val Project.toolchain
            get() = VlangToolchain()
    }
}
