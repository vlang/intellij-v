package org.vlang.ide.codeInsight

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.util.findTopmostParentOfType
import org.vlang.lang.psi.VlangType
import org.vlang.lang.psi.impl.VlangElementFactory

@Service
class VlangBuiltinTypesUtil(private val project: Project) {
    val int = builtinType("int")
    val bool = builtinType("bool")
    val rune = builtinType("rune")
    val f32 = builtinType("f32")
    val f64 = builtinType("f64")
    val string = builtinType("string")
    val void = builtinType("void")
    val any = builtinType("any")
    val nil = builtinType("nil")
    val iError = builtinType("IError")

    private fun builtinType(name: String): VlangType? {
//        val builtin = VlangConfiguration.getInstance(project).builtinLocation
//        if (builtin != null) {
//            print("")
//        }

        val file = VlangElementFactory.createFileFromText(project, "fn f(a $name)")

        val element = file.findElementAt(8)
        return element?.findTopmostParentOfType()
    }

    companion object {
        fun getInstance(project: Project) = project.service<VlangBuiltinTypesUtil>()
    }
}
