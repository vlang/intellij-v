package org.vlang.ide.go

import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.stubs.index.VlangNamesIndex

class VlangGotoSymbolContributor :
    VlangGotoContributorBase<VlangNamedElement>(VlangNamedElement::class.java, VlangNamesIndex.KEY)
