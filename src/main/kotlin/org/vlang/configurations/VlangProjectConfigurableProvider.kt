package org.vlang.configurations

import com.intellij.openapi.module.Module
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurableProvider
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.options.UnnamedConfigurable
import com.intellij.openapi.project.Project

class VlangProjectConfigurableProvider(private val myProject: Project) : ConfigurableProvider() {
    override fun createConfigurable(): Configurable {
        val projectSettingsConfigurable = VlangProjectSettingsConfigurable(myProject)
//        val librariesConfigurable = GoLibrariesConfigurableProvider(myProject).createConfigurable()
//        val sdkConfigurable = VlangSdkService.getInstance(myProject).createSdkConfigurable()
//        val autoImportConfigurable: Configurable = GoAutoImportConfigurable(myProject, false)
        return VlangCompositeConfigurable(
            projectSettingsConfigurable,
//            librariesConfigurable,
//            autoImportConfigurable
        )
    }

    private class VlangCompositeConfigurable(vararg configurables: Configurable) : SearchableConfigurable.Parent.Abstract() {
        private var myConfigurables: Array<out Configurable>?

        init {
            myConfigurables = configurables
        }

        override fun buildConfigurables(): Array<out Configurable>? {
            return myConfigurables
        }

        override fun getId(): String {
            return "v"
        }

        override fun getDisplayName(): String {
            return "V"
        }

        override fun getHelpTopic(): String? {
            return null
        }

        override fun disposeUIResources() {
            super.disposeUIResources()
            myConfigurables = null
        }
    }

    class VlangProjectSettingsConfigurable(project: Project) : VlangModuleAwareConfigurable(project, "Project Settings", null) {
        override fun createModuleConfigurable(module: Module): UnnamedConfigurable {
            return VlangModuleSettingsConfigurable(module, false)
        }
    }
}
