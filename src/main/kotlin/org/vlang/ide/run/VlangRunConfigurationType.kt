package org.vlang.ide.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.Project
import org.vlang.ide.ui.VIcons

class VlangRunConfigurationType : ConfigurationTypeBase(
    ID, "V Build",
    "Run V project",
    VIcons.V
) {
    companion object {
        const val ID = "VlangRun"
    }

    init {
        addFactory(object : ConfigurationFactory(this) {
            override fun getId() = ID

            override fun createTemplateConfiguration(project: Project) =
                VlangRunConfiguration(project, this, "Run V project")

            override fun getOptionsClass() = VlangRunConfigurationOptions::class.java
        })
    }
}
