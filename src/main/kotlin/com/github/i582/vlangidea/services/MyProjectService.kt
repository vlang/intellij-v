package com.github.i582.vlangidea.services

import com.intellij.openapi.project.Project
import com.github.i582.vlangidea.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
