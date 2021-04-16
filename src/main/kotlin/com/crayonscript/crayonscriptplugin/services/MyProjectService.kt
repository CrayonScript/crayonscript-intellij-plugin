package com.crayonscript.crayonscriptplugin.services

import com.crayonscript.crayonscriptplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
