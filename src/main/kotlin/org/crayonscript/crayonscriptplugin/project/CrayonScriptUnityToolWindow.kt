package org.crayonscript.crayonscriptplugin.project

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.SimpleTree
import org.crayonscript.crayonscriptplugin.services.CrayonScriptProjectService
import javax.swing.JComponent
import javax.swing.tree.TreeSelectionModel


class CrayonScriptUnityToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowPanel = CrayonScriptUnityToolWindowPanel(project)
        val tab = ContentFactory.SERVICE.getInstance().createContent(
            toolWindowPanel, "", false)
        toolWindow.contentManager.addContent(tab)
    }
}

class CrayonScriptUnityToolWindowPanel(project: Project) : SimpleToolWindowPanel(true, false) {
    private val unityToolWindow = CrayonScriptUnityToolWindow(project)
    init {
        setContent(unityToolWindow.content)
    }
}


class CrayonScriptUnityToolWindow(
    private val project: Project
) {
    private val projectTree = CrayonScriptUnityTree(project)
    val content: JComponent = ScrollPaneFactory.createScrollPane(projectTree, 0)
}

class CrayonScriptUnityTree(private val project: Project) : SimpleTree() {
    init {
        isRootVisible = false
        showsRootHandles = true
        selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION

        val projectService = project.service<CrayonScriptProjectService>()
        val crayonScriptProject = projectService.crayonScriptProject;
        var scenes = crayonScriptProject.getScenes()
        var scene = scenes[0]

        emptyText.text = scene.name
    }
}