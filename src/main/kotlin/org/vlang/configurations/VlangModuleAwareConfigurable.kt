package org.vlang.configurations

import com.intellij.application.options.ModuleAwareProjectConfigurable
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.UnnamedConfigurable
import com.intellij.openapi.project.Project
import org.vlang.sdk.VlangSdkService

abstract class VlangModuleAwareConfigurable(project: Project, displayName: String?, helpTopic: String?) :
    ModuleAwareProjectConfigurable<UnnamedConfigurable>(project, displayName, helpTopic) {

    override fun isSuitableForModule(module: Module): Boolean {
        if (module.isDisposed) {
            return false
        }
        val project = module.project
        return !project.isDisposed && VlangSdkService.getInstance(project).isVlangModule(module)
    }
}
