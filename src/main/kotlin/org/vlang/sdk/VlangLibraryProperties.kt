package org.vlang.sdk

import com.intellij.openapi.roots.libraries.LibraryProperties
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.MapAnnotation

class VlangLibraryProperties : LibraryProperties<VlangLibraryProperties?>() {
    @get:MapAnnotation(surroundWithTag = false)
    var packageNameToDirsMap = mutableMapOf<String, List<String>>()

    override fun getState() = this

    override fun loadState(state: VlangLibraryProperties) {
        XmlSerializerUtil.copyBean(state, this)
    }

    override fun equals(other: Any?): Boolean {
        return other is VlangLibraryProperties && packageNameToDirsMap == other.packageNameToDirsMap
    }

    override fun hashCode(): Int {
        return packageNameToDirsMap.hashCode()
    }
}
