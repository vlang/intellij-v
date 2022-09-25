package org.vlang.sdk

import com.intellij.compiler.CompilerWorkspaceConfiguration
import com.intellij.ide.util.projectWizard.JavaModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleBuilderListener
import com.intellij.ide.util.projectWizard.SourcePathsBuilder
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.projectRoots.SdkTypeId
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.Pair

class VlangModuleBuilder : JavaModuleBuilder(), SourcePathsBuilder, ModuleBuilderListener {
    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        addListener(this)
        super.setupRootModel(modifiableRootModel)
    }

    override fun getSourcePaths(): List<Pair<String, String>> {
        return emptyList()
    }

    override fun getModuleType(): ModuleType<*> {
        return VlangModuleType.getInstance()
    }

    override fun isSuitableSdkType(sdkType: SdkTypeId): Boolean {
        return sdkType === VlangSdkType.getInstance()
    }

    override fun moduleCreated(module: Module) {
        CompilerWorkspaceConfiguration.getInstance(module.project).CLEAR_OUTPUT_DIRECTORY = false
    }
}
