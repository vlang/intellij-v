package org.vlang.lang.annotator.checkers

import com.intellij.lang.annotation.AnnotationHolder
import org.vlang.lang.psi.VlangVisitor

open class VlangCheckerBase(protected val holder: AnnotationHolder) : VlangVisitor()
