package org.vlang.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.PsiManagerEx
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.impl.source.tree.FileElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.LightVirtualFile
import org.vlang.lang.VlangFileType
import org.vlang.lang.VlangLanguage
import org.vlang.utils.childOfType

abstract class VlangCodeFragment(
    fileViewProvider: FileViewProvider,
    contentElementType: IElementType,
    open val context: VlangCompositeElement,
    forceCachedPsi: Boolean = true,
) : VlangFile(fileViewProvider), PsiCodeFragment {

    constructor(
        project: Project,
        text: CharSequence,
        contentElementType: IElementType,
        context: VlangCompositeElement,
    ) : this(
        PsiManagerEx.getInstanceEx(project).fileManager.createFileViewProvider(
            LightVirtualFile("fragment.v", VlangLanguage, text), true
        ),
        contentElementType,
        context,
    )

    private var viewProvider = super.getViewProvider() as SingleRootFileViewProvider
    private var forcedResolveScope: GlobalSearchScope? = null
    private var isPhysical = true

    init {
        if (forceCachedPsi) {
            @Suppress("LeakingThis")
            viewProvider.forceCachedPsi(this)
        }
        init(TokenType.CODE_FRAGMENT, contentElementType)
    }

    final override fun init(elementType: IElementType, contentElementType: IElementType?) {
        super.init(elementType, contentElementType)
    }

    override fun getContext(): PsiElement =
        context

    final override fun getViewProvider(): SingleRootFileViewProvider = viewProvider

    override fun forceResolveScope(scope: GlobalSearchScope?) {
        forcedResolveScope = scope
    }

    override fun getForcedResolveScope(): GlobalSearchScope? = forcedResolveScope

    override fun isValid() = true

    override fun isPhysical() = isPhysical

    override fun clone(): PsiFileImpl {
        val clone = cloneImpl(calcTreeElement().clone() as FileElement) as VlangCodeFragment
        clone.isPhysical = false
        clone.myOriginalFile = this
        clone.viewProvider =
            SingleRootFileViewProvider(PsiManager.getInstance(project), LightVirtualFile(name, VlangLanguage, text), false)
        clone.viewProvider.forceCachedPsi(clone)
        return clone
    }

    override fun accept(visitor: PsiElementVisitor) {
        visitor.visitFile(this)
    }

    override fun getFileType(): VlangFileType = VlangFileType
}

open class VlangExpressionCodeFragment : VlangCodeFragment {
    protected constructor(fileViewProvider: FileViewProvider, context: VlangCompositeElement)
            : super(fileViewProvider, VlangCodeFragmentElementType.EXPR, context)

    constructor(project: Project, text: CharSequence, context: VlangCompositeElement)
            : super(project, text, VlangCodeFragmentElementType.EXPR, context)

    val expr: VlangExpression? get() = childOfType()
}

open class VlangDebuggerExpressionCodeFragment : VlangExpressionCodeFragment {
    constructor(fileViewProvider: FileViewProvider, context: VlangCompositeElement) : super(fileViewProvider, context)
    constructor(project: Project, text: CharSequence, context: VlangCompositeElement) : super(project, text, context)
}
