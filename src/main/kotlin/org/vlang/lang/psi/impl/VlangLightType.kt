package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.impl.light.LightElement
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangPsiImplUtil.getUnderlyingType
import org.vlang.lang.psi.impl.VlangPsiImplUtil.resolveType
import org.vlang.lang.stubs.VlangTypeStub

abstract class VlangLightType<E : VlangCompositeElement>(
    protected val element: E,
) : LightElement(element.manager, element.language), VlangType {

    init {
        navigationElement = element
    }

    override val genericArguments: VlangGenericArguments?
        get() = null

    override fun getContainingFile(): PsiFile = element.containingFile

    override fun getTypeReferenceExpression() = null

    override fun getIdentifier() = null

    override fun toString() = javaClass.simpleName + "{" + element + "}"

    override fun getElementType() = null

    override fun getStub(): VlangTypeStub? = null

    override fun getUnderlyingType(): VlangType? = getUnderlyingType(this)

    override fun resolveType() = resolveType(this)

    class LightArrayType(type: VlangType) : VlangLightType<VlangType>(type), VlangArrayType {
        override fun getText() = "[]" + element.text

        override fun getType() = element

        override fun getTypeModifiers(): VlangTypeModifiers? = null

        override fun getLbrack(): PsiElement {
            return object : FakePsiElement() {
                override fun getText(): String {
                    return "["
                }

                override fun getParent(): PsiElement {
                    return this@LightArrayType
                }
            }
        }

        override fun getRbrack(): PsiElement {
            return object : FakePsiElement() {
                override fun getText(): String {
                    return "]"
                }

                override fun getParent(): PsiElement {
                    return this@LightArrayType
                }
            }
        }
    }

    class LightMapType(private val keyType: VlangType, valueType: VlangType) : VlangLightType<VlangType>(valueType), VlangMapType {
        override fun getText() = "[]" + element.text

        override fun getType() = element

        override fun getTypeModifiers(): VlangTypeModifiers? = null

        override fun getTypeList() = mutableListOf(keyType, element)

        override fun getKeyType() = keyType

        override fun getValueType() = element

        override fun getLbrack(): PsiElement {
            return object : FakePsiElement() {
                override fun getText(): String {
                    return "["
                }

                override fun getParent(): PsiElement {
                    return this@LightMapType
                }
            }
        }

        override fun getRbrack(): PsiElement {
            return object : FakePsiElement() {
                override fun getText(): String {
                    return "]"
                }

                override fun getParent(): PsiElement {
                    return this@LightMapType
                }
            }
        }
    }

    class LightPointerType(type: VlangType) : VlangLightType<VlangType>(type), VlangPointerType {
        override fun getText() = "&" + element.text

        override fun getType() = element

        override fun getTypeModifiers() = null
    }

    class LightFunctionType(o: VlangSignatureOwner) : VlangLightType<VlangSignatureOwner>(o), VlangFunctionType {
        override fun getSignature() = element.getSignature()

        override fun getFn(): PsiElement {
            return if (element is VlangFunctionOrMethodDeclaration)
                (element as VlangFunctionOrMethodDeclaration).getFn()
            else
                element
        }

        override val genericParameters: VlangGenericParameters? = null

        override fun getText(): String {
            val signature = element.getSignature()
            return "fn " + if (signature != null) signature.text else "<null>"
        }

        override fun getType() = null

        override fun getTypeModifiers() = null
    }

    class VlangGenericType(private val name: String, param: VlangCompositeElement) : VlangLightType<VlangCompositeElement>(param) {
        override fun getName() = name

        override fun getText() = name

        override fun getType() = null

        override fun getTypeModifiers() = null
    }
}
