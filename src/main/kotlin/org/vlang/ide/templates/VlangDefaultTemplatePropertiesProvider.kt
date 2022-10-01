package org.vlang.ide.templates

import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider
import com.intellij.psi.PsiDirectory
import java.util.*

class VlangDefaultTemplatePropertiesProvider : DefaultTemplatePropertiesProvider {
    override fun fillProperties(directory: PsiDirectory, props: Properties) {
        VlangTemplateUtil.setModuleNameAttribute(directory, props)
    }
}
