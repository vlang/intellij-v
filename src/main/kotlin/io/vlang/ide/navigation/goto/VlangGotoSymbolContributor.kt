package io.vlang.ide.navigation.goto

import io.vlang.lang.psi.VlangNamedElement
import io.vlang.lang.stubs.index.VlangNamesIndex

class VlangGotoSymbolContributor :
    VlangGotoContributorBase<VlangNamedElement>(VlangNamedElement::class.java, VlangNamesIndex.KEY)
