package org.vlang.ide.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader

object VIcons {
    val Vlang = IconLoader.getIcon("/icons/vlang.svg", this::class.java)
    val Module = AllIcons.FileTypes.Config

    var Struct = AllIcons.Nodes.Class
    var Interface = AllIcons.Nodes.Interface
    var Enum = AllIcons.Nodes.Enum
    var Union = AllIcons.Nodes.AbstractClass // TODO: change
    var Method = AllIcons.Nodes.Method
    var Function = AllIcons.Nodes.Function
    var Variable = AllIcons.Nodes.Variable
    var Constant = AllIcons.Nodes.Constant
    var Parameter = AllIcons.Nodes.Parameter
    var Field = AllIcons.Nodes.Field
    var Receiver = AllIcons.Nodes.Parameter
    var Directory = AllIcons.Nodes.Folder
}
