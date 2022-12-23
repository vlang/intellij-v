package org.vlang.projectWizard.nonidea

import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.ProjectSettingsStepBase
import com.intellij.platform.DirectoryProjectGenerator

open class VlangProjectSettingsStep(generator: DirectoryProjectGenerator<Unit>) :
    ProjectSettingsStepBase<Unit>(generator, AbstractNewProjectStep.AbstractCallback())
