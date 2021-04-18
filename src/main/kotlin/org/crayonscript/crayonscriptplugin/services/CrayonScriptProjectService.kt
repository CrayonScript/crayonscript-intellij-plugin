package org.crayonscript.crayonscriptplugin.services

import com.intellij.openapi.project.Project
import org.crayonscript.crayonscriptplugin.CrayonScriptBundle

class CrayonScriptProjectService(project: Project) {

    init {
        println(CrayonScriptBundle.message("projectService", project.name))
    }
}
