package org.vlang.ide.navigation.goto

import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.stubs.index.VlangClassLikeIndex

open class VlangGotoClassLikeContributor :
    VlangGotoContributorBase<VlangNamedElement>(VlangNamedElement::class.java, VlangClassLikeIndex.KEY)
