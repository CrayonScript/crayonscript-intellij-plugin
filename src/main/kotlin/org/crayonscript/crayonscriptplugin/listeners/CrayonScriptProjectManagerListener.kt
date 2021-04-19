package org.crayonscript.crayonscriptplugin.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import org.crayonscript.crayonscriptplugin.services.CrayonScriptProjectService

internal class CrayonScriptProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.service<CrayonScriptProjectService>()
    }
}
