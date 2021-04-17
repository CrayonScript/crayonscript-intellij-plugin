package org.crayonscript.crayonscriptplugin.services

import com.intellij.openapi.project.Project
import org.crayonscript.crayonscriptplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
