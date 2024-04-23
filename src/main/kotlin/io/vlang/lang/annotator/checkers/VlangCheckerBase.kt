package io.vlang.lang.annotator.checkers

import com.intellij.lang.annotation.AnnotationHolder
import io.vlang.lang.psi.VlangVisitor

open class VlangCheckerBase(protected val holder: AnnotationHolder) : VlangVisitor()
