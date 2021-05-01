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
import org.crayonscript.crayonscriptplugin.util.CrayonScriptUnityObjectNode
import org.crayonscript.crayonscriptplugin.util.CrayonScriptUtils
import javax.swing.JComponent
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.*


class CrayonScriptUnityToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowPanel = CrayonScriptUnityToolWindowPanel(project)
        val tab = ContentFactory.SERVICE.getInstance().createContent(
            toolWindowPanel, "", false
        )
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

    val content: JComponent

    init {
        val projectService = project.service<CrayonScriptProjectService>()
        val crayonScriptProject = projectService.crayonScriptProject;

        var sceneObjects = mutableListOf<CrayonScriptUnityObjectNode>()
        var scenes = crayonScriptProject.getScenes()
        for (scene in scenes) {
            var sceneObject = CrayonScriptUtils.getSceneObject(scene)
            sceneObjects.add(sceneObject)
        }

        var rootObject = CrayonScriptUnityObjectNode(
            CrayonScriptUnityObjectNode.ROOT_OBJECT_ID,
            CrayonScriptUnityObjectNode.ROOT_FILE_ID
        )
        rootObject.processTopNode(crayonScriptProject)

        for (sceneObject in sceneObjects) {
            rootObject.addChild(sceneObject)
        }

        var rootTreeNode = CrayonScriptTreeNode(project, rootObject)
        rootTreeNode.setupTreeNodes()

        var rootTreeModel = CrayonScriptTreeModel(project, rootTreeNode)

        val crayonScriptTree = CrayonScriptTree(project, rootTreeModel)

        content = ScrollPaneFactory.createScrollPane(crayonScriptTree, 0)
    }
}

class CrayonScriptTree(
    private val project: Project,
    private val crayonScriptTreeModel: CrayonScriptTreeModel
) : SimpleTree(crayonScriptTreeModel) {

    init {
        this.isRootVisible = true
        this.showsRootHandles = true
        this.selectionModel = CrayonScriptTreeSelectionModel()
        this.cellRenderer = CrayonScriptTreeCellRenderer(project)
    }
}

class CrayonScriptTreeModel(
    private val project: Project,
    private val crayonScriptTreeNode: CrayonScriptTreeNode
) : DefaultTreeModel(crayonScriptTreeNode) {

    init {
        this.addTreeModelListener(CrayonScriptTreeModelListener())
    }
}

class CrayonScriptTreeNode(
    private val project: Project,
    private val crayonScriptObject: CrayonScriptUnityObjectNode
) : DefaultMutableTreeNode(crayonScriptObject) {

    fun setupTreeNodes() {
        for (childObject in this.crayonScriptObject.getChildren()) {
            val childTreeNode = CrayonScriptTreeNode(project, childObject!!)
            this.add(childTreeNode)
            childTreeNode.setupTreeNodes()
        }
    }
}

class CrayonScriptTreeCellRenderer(
    private val project: Project
) : DefaultTreeCellRenderer() {

}

class CrayonScriptTreeSelectionModel() : DefaultTreeSelectionModel() {

    init {
        this.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
    }
}

class CrayonScriptTreeModelListener() : TreeModelListener {

    override fun treeNodesChanged(e: TreeModelEvent?) {
        TODO("Not yet implemented")
    }

    override fun treeNodesInserted(e: TreeModelEvent?) {
        TODO("Not yet implemented")
    }

    override fun treeNodesRemoved(e: TreeModelEvent?) {
        TODO("Not yet implemented")
    }

    override fun treeStructureChanged(e: TreeModelEvent?) {
        TODO("Not yet implemented")
    }
}


