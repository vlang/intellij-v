package io.vlang.ide.inspections.general

import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.codeInspection.util.IntentionName
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPointerManager
import io.vlang.ide.inspections.VlangBaseInspection
import io.vlang.ide.intentions.VlangImplementInterfaceIntention
import io.vlang.lang.psi.*
import io.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import io.vlang.lang.psi.types.VlangFunctionTypeEx
import javax.swing.Icon

class VlangInterfaceNotImplementedInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitStructType(o: VlangStructType) {
                val implementsClause = o.implementsClause
                if (implementsClause != null) {
                    val interfaces =
                        implementsClause.typeReferenceExpressionList.map { it to (it.resolve() as? VlangInterfaceDeclaration)?.interfaceType }
                    interfaces.forEach { (ref, iface) ->
                        if (iface != null
                            && (!iface.fieldList.all { isAlreadyImplementedField(o, it) }
                                    || !iface.methodList.all { isAlreadyImplementedMethod(o, it) })
                        ) {

                            val smartRef = SmartPointerManager.createPointer(ref)
                            holder.registerProblem(
                                ref,
                                "Explicit interface <code>#ref</code> not implemented",
//                                LocalQuickFix.from(VlangImplementInterfaceIntention().asModCommandAction())!!

//                                LocalQuickFixBackedByIntentionAction(VlangImplementInterfaceIntention())

                                object : LocalQuickFixAndIntentionActionOnPsiElement(ref), Iconable {
                                    val action = VlangImplementInterfaceIntention()
                                    override fun invoke(
                                        project: Project,
                                        file: PsiFile,
                                        editor: Editor?,
                                        startElement: PsiElement,
                                        endElement: PsiElement,
                                    ) {
                                        action(project, editor, file)
                                    }

                                    override fun getText(): @IntentionName String = "Implement explicit interface `${startElement.text}`"

                                    override fun getFamilyName(): @IntentionFamilyName String = action.familyName

                                    override fun getIcon(flags: Int): Icon? = action.getIcon(flags)
                                }
                            )
                        }
                    }
                }
                super.visitStructType(o)
            }
        }
    }

    private fun isAlreadyImplementedMethod(struct: VlangStructType, method: VlangInterfaceMethodDefinition): Boolean {
        val structMethods = struct.toEx().methodsList(struct.project)
        return structMethods
            .filter { it.name == method.name }
            // if interface method immutable, don't care about struct method mutability
            .filter { !method.isMutable || it.receiver.isMutable() == method.isMutable }
            .any { structMethod ->
                val interfaceTypeEx = VlangFunctionTypeEx.from(method) ?: return@any false
                val structTypeEx = VlangFunctionTypeEx.from(structMethod) ?: return@any false

                interfaceTypeEx.isEqual(structTypeEx)
            }
    }

    private fun isAlreadyImplementedField(struct: VlangStructType, field: VlangFieldDefinition): Boolean {
        val structFields = struct.fieldList
        return structFields
            .filter { it.name == field.name }
            // if interface field immutable, don't care about struct field mutability
            .filter { !field.isMutable() || it.isMutable() == field.isMutable() }
            .any { structField ->
                val structFieldType = structField.getType(null) ?: return@any false
                val interfaceFieldType = field.getType(null) ?: return@any false
                structFieldType.isEqual(interfaceFieldType)
            }
    }
}
