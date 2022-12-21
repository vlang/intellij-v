package org.vlang.ide.go

import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.stubs.index.VlangClassLikeIndex

open class VlangGotoClassLikeContributor :
    VlangGotoContributorBase<VlangNamedElement>(VlangNamedElement::class.java, VlangClassLikeIndex.KEY)
