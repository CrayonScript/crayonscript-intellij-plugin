package org.crayonscript.crayonscriptplugin.project

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.SimpleTree
import org.crayonscript.crayonscriptplugin.services.CrayonScriptProjectService
import org.crayonscript.crayonscriptplugin.util.CrayonScriptUnityObjectNode
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

class CrayonScriptUnityToolWindowPanel(project: Project) : SimpleToolWindowPanel(true, false),
    AsyncFileListener, AsyncFileListener.ChangeApplier, Disposable {

    private val crayonScriptProject:CrayonScriptProject = project.service<CrayonScriptProjectService>().crayonScriptProject
    private val unityToolWindow = CrayonScriptUnityToolWindow(crayonScriptProject)

    init {
        setContent(unityToolWindow.createContent())
        VirtualFileManager.getInstance().addAsyncFileListener(this, this)
    }

    override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier? {
        val scenes = crayonScriptProject.getScenes()
        for (event in events) {
            for (scene in scenes) {
                if (event.file == scene) {
                    return this
                }
            }
        }
        return null
    }

    override fun dispose() {
        // TODO("Not yet implemented")
    }

    override fun beforeVfsChange() {
        super.beforeVfsChange()
        removeAll()
        revalidate()
        repaint()
    }

    override fun afterVfsChange() {
        super.afterVfsChange()
        setContent(unityToolWindow.createContent())
    }

}


class CrayonScriptUnityToolWindow(private val crayonScriptProject: CrayonScriptProject) {

    fun createContent(): JComponent {
        val crayonScriptTree = CrayonScriptTree(crayonScriptProject)
        return ScrollPaneFactory.createScrollPane(crayonScriptTree, 0)
    }
}

class CrayonScriptTree(
    private val crayonScriptProject: CrayonScriptProject,
    private val crayonScriptTreeModel: CrayonScriptTreeModel
) : SimpleTree(crayonScriptTreeModel) {

    constructor(crayonScriptProject: CrayonScriptProject):
            this(crayonScriptProject, CrayonScriptTreeModel(crayonScriptProject))

    init {
        this.isRootVisible = true
        this.showsRootHandles = true
        this.selectionModel = CrayonScriptTreeSelectionModel()
        this.cellRenderer = CrayonScriptTreeCellRenderer(crayonScriptProject)

        this.refreshTree()
    }

    private fun refreshTree() {
        crayonScriptProject.refreshTree()
        crayonScriptTreeModel.refreshTree()

        revalidate()
        repaint()
    }
}

class CrayonScriptTreeModel(
    private val crayonScriptProject: CrayonScriptProject,
    private val crayonScriptTreeNode: CrayonScriptTreeNode
) : DefaultTreeModel(crayonScriptTreeNode) {

    constructor(crayonScriptProject: CrayonScriptProject):
            this(crayonScriptProject, CrayonScriptTreeNode(crayonScriptProject))

    init {
        this.addTreeModelListener(CrayonScriptTreeModelListener())
    }

    fun refreshTree() {
        clearTreeNodes()
        setupTreeNodes()
    }

    private fun clearTreeNodes() {
        this.crayonScriptTreeNode.clearTreeNodes()
    }

    private fun setupTreeNodes() {
        this.crayonScriptTreeNode.setupTreeNodes()
    }
}

class CrayonScriptTreeNode(
    private val crayonScriptProject: CrayonScriptProject,
    private val crayonScriptObject: CrayonScriptUnityObjectNode
) : DefaultMutableTreeNode(crayonScriptObject) {

    constructor(crayonScriptProject: CrayonScriptProject) :
            this(crayonScriptProject, crayonScriptProject.rootObjectNode)

    fun clearTreeNodes() {
        if (this.childCount > 0) {
            for (childObject in this.children()) {
                if (childObject is CrayonScriptTreeNode) {
                    childObject.clearTreeNodes()
                }
            }
            this.children.clear()
        }
    }

    fun setupTreeNodes() {
        for (childObject in this.crayonScriptObject.getChildren()) {
            val childTreeNode = CrayonScriptTreeNode(crayonScriptProject, childObject!!)
            this.add(childTreeNode)
            childTreeNode.setupTreeNodes()
        }
    }
}

class CrayonScriptTreeCellRenderer(
    private val crayonScriptProject: CrayonScriptProject
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


