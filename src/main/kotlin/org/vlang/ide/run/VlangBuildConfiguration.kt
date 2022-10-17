package org.vlang.ide.run

import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.roots.ProjectModelBuildableElement
import com.intellij.openapi.roots.ProjectModelExternalSource

@Suppress("UnstableApiUsage")
open class VlangBuildConfiguration(
    val configuration: VlangRunConfiguration,
    val environment: ExecutionEnvironment
) : ProjectModelBuildableElement {
    override fun getExternalSource(): ProjectModelExternalSource? = null
}
