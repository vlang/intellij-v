package org.vlang.sdk

import com.intellij.openapi.projectRoots.*
import com.intellij.openapi.roots.OrderRootType
import org.jdom.Element
import org.vlang.ide.ui.VIcons
import java.io.File

class VlangSdkType : SdkType("V SDK") {
    companion object {
        fun getInstance() = findInstance(VlangSdkType::class.java)
    }

    override fun getIcon() = VIcons.V

    override fun suggestHomePath() = VlangSdkUtil.suggestSdkDirectory()?.path

    override fun isValidSdkHome(path: String): Boolean {
        VlangSdkService.LOG.debug("Validating sdk path: $path")
        val executablePath = VlangSdkService.getExecutablePath(path)
        if (executablePath == null) {
            VlangSdkService.LOG.debug("V executable is not found: ")
            return false
        }
        if (!File(executablePath).canExecute()) {
            VlangSdkService.LOG.debug("V binary cannot be executed: $path")
            return false
        }
        return true
    }

    override fun adjustSelectedSdkHome(homePath: String) = homePath // TODO

    override fun suggestSdkName(currentSdkName: String?, sdkHome: String): String {
        val version = getVersionString(sdkHome) ?: return "Unknown V version at $sdkHome"
        return "V $version"
    }

    override fun getVersionString(sdkHome: String) = "0.3.1" // TODO: get version from v executable

    override fun getDefaultDocumentationUrl(sdk: Sdk) = null

    override fun createAdditionalDataConfigurable(sdkModel: SdkModel, sdkModificator: SdkModificator) = null

    override fun saveAdditionalData(additionalData: SdkAdditionalData, additional: Element) {}

    override fun getPresentableName() = "V SDK"

    override fun setupSdkPaths(sdk: Sdk) {
        val versionString = sdk.versionString ?: throw RuntimeException("SDK version is not defined")
        val modificator = sdk.sdkModificator
        val path = sdk.homePath ?: return
        modificator.homePath = path
        for (file in VlangSdkUtil.getSdkDirectoriesToAttach(path, versionString)) {
            modificator.addRoot(file, OrderRootType.CLASSES)
            modificator.addRoot(file, OrderRootType.SOURCES)
        }
        modificator.commitChanges()
    }
}