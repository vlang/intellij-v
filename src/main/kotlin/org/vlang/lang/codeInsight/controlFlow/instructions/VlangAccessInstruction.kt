package org.vlang.lang.codeInsight.controlFlow.instructions

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector

interface VlangAccessInstruction : VlangInstruction {
    val access: ReadWriteAccessDetector.Access
}
