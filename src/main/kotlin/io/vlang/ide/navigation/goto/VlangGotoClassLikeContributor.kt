package io.vlang.ide.navigation.goto

import io.vlang.lang.psi.VlangNamedElement
import io.vlang.lang.stubs.index.VlangClassLikeIndex

open class VlangGotoClassLikeContributor :
    VlangGotoContributorBase<VlangNamedElement>(VlangNamedElement::class.java, VlangClassLikeIndex.KEY)
