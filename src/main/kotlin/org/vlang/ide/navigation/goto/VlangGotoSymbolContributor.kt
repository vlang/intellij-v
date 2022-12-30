package org.vlang.ide.navigation.goto

import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.stubs.index.VlangNamesIndex

class VlangGotoSymbolContributor :
    VlangGotoContributorBase<VlangNamedElement>(VlangNamedElement::class.java, VlangNamesIndex.KEY)
