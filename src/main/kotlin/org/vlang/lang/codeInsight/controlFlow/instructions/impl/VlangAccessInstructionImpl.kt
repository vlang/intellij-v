package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import com.intellij.psi.PsiElement
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangAccessInstruction

abstract class VlangAccessInstructionImpl(anchor: PsiElement, override val access: ReadWriteAccessDetector.Access) :
    VlangLinearInstructionImpl(anchor), VlangAccessInstruction
