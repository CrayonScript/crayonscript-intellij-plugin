package org.crayonscript.crayonscriptplugin.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import org.crayonscript.crayonscriptplugin.CrayonScriptBundle
import org.crayonscript.crayonscriptplugin.project.CrayonScriptProject

import com.mxgraph.swing.util.mxGraphTransferable

class CrayonScriptProjectService(
    private val project: Project
) : DumbAware, Disposable {

    val crayonScriptProject: CrayonScriptProject

    init {
        println(CrayonScriptBundle.message("projectService", project.name))
        val clsLoader = mxGraphTransferable::class.java.classLoader
        Thread.currentThread().contextClassLoader = clsLoader
        crayonScriptProject = CrayonScriptProject(project)
    }

    /**
     * Usually not invoked directly, see class javadoc.
     */
    override fun dispose() {
        TODO("Not yet implemented")
    }
}

