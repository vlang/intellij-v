package io.vlang.ide.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.Project
import io.vlang.ide.ui.VIcons

class VlangRunConfigurationType : ConfigurationTypeBase(
    ID,
    "V",
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
                VlangRunConfiguration(project, this, null)

            override fun getOptionsClass() = VlangRunConfigurationOptions::class.java

            override fun getName(): String = "V Build"

            override fun isEditableInDumbMode(): Boolean = true
        })
    }
}
