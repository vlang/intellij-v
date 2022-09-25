package org.vlang.project

import com.intellij.openapi.util.SimpleModificationTracker
import com.intellij.util.ThreeState
import com.intellij.util.xmlb.annotations.Tag

@Tag("buildTags")
class VlangBuildTargetSettings : SimpleModificationTracker() {
    var os = DEFAULT
    var arch = DEFAULT
    var cgo = ThreeState.UNSURE
    var compiler = ANY_COMPILER
    var vVersion = DEFAULT
    var customFlags = emptyArray<String>()

    override fun hashCode(): Int {
        var result = os.hashCode()
        result = 31 * result + arch.hashCode()
        result = 31 * result + cgo.hashCode()
        result = 31 * result + compiler.hashCode()
        result = 31 * result + vVersion.hashCode()
        result = 31 * result + customFlags.contentHashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangBuildTargetSettings

        if (os != other.os) return false
        if (arch != other.arch) return false
        if (cgo != other.cgo) return false
        if (compiler != other.compiler) return false
        if (vVersion != other.vVersion) return false
        if (!customFlags.contentEquals(other.customFlags)) return false

        return true
    }

    companion object {
        const val ANY_COMPILER = "Any"
        const val DEFAULT = "default"
    }
}
