package com.github.rvergis.crayonscriptintellijplugin.services

import com.github.rvergis.crayonscriptintellijplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
