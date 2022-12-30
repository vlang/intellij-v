package org.vlang.ide.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader

object VIcons {
    val V = IconLoader.getIcon("/icons/vlang.svg", this::class.java)
    val Vsh = IconLoader.getIcon("/icons/vsh.svg", this::class.java)
    val Module = AllIcons.FileTypes.Config

    val Test = AllIcons.RunConfigurations.TestState.Run
    val TestGreen = AllIcons.RunConfigurations.TestState.Green2
    val TestRed = AllIcons.RunConfigurations.TestState.Red2

    val Struct = AllIcons.Nodes.Class
    val Interface = AllIcons.Nodes.Interface
    val Enum = AllIcons.Nodes.Enum
    val Union = AllIcons.Nodes.AbstractClass // TODO: change
    val Method = AllIcons.Nodes.Method
    val Function = AllIcons.Nodes.Function
    val Variable = AllIcons.Nodes.Variable
    val Constant = AllIcons.Nodes.Constant
    val Parameter = AllIcons.Nodes.Parameter
    val Field = AllIcons.Nodes.Field
    val Receiver = AllIcons.Nodes.Parameter
    val Alias = AllIcons.Nodes.Alias
    val Directory = AllIcons.Nodes.Folder

    val SourceRoot = AllIcons.Modules.SourceRoot
    val LocalModulesRoot = AllIcons.Nodes.ModuleGroup
}
