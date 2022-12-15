package org.vlang.ide.test.configuration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.Project
import org.vlang.ide.ui.VIcons

class VlangTestConfigurationType : ConfigurationTypeBase(
    ID, "V Test",
    "Run V test",
    VIcons.V
) {
    companion object {
        const val ID = "VlangTest"
    }

    init {
        addFactory(object : ConfigurationFactory(this) {
            override fun getId() = ID

            override fun createTemplateConfiguration(project: Project) =
                VlangTestConfiguration(project, this, "Run V test")

            override fun getOptionsClass() = VlangTestConfigurationOptions::class.java

            override fun getName(): String = "Run V test"

            override fun isEditableInDumbMode(): Boolean = true
        })
    }
}
