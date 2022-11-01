package org.vlang.lang.structure

import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.structure.VlangStructureViewFactory.Element

class VlangExportabilityComparator : Comparator<Element> {
    override fun compare(descriptor1: Element, descriptor2: Element): Int {
        val accessLevel1 = getAccessLevel(descriptor1)
        val accessLevel2 = getAccessLevel(descriptor2)
        return accessLevel2 - accessLevel1
    }

    companion object {
        val INSTANCE = VlangExportabilityComparator()

        private fun getAccessLevel(element: Element): Int {
            val value = element.value
            if (value is VlangNamedElement && value.isPublic()) {
                return 1
            }
            return -1
        }
    }
}
