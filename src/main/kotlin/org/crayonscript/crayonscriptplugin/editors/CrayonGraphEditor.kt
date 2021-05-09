package org.crayonscript.crayonscriptplugin.editors

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.mxgraph.editorutils.EditorPalette
import com.mxgraph.io.mxCodec
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.swing.util.mxGraphTransferable
import com.mxgraph.util.mxEventSource.mxIEventListener
import com.mxgraph.util.mxResources
import com.mxgraph.util.mxUtils
import com.mxgraph.view.mxGraph
import org.crayonscript.crayonscriptplugin.project.CrayonScriptProject
import org.crayonscript.crayonscriptplugin.project.CrayonScriptUnityToolWindow
import org.crayonscript.crayonscriptplugin.services.CrayonScriptProjectService
import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.print.PageFormat
import java.awt.print.Paper
import java.beans.PropertyChangeListener
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSplitPane


class CrayonGraphEditor(private val project: Project, private val file: VirtualFile) : FileEditor, DumbAware {

    private val jGraphEditor:CrayonScriptJGraphEditor

    init {
        val crayonScriptProject:CrayonScriptProject = project.service<CrayonScriptProjectService>().crayonScriptProject
        jGraphEditor = CrayonScriptJGraphEditor(crayonScriptProject, file)
    }

    override fun getFile() = file

    override fun <T : Any?> getUserData(key: Key<T>): T? {
        return null;
    }

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {
        // todo
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

    override fun getComponent(): JComponent {
        return jGraphEditor
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return jGraphEditor
    }

    override fun getName(): String = "CrayonGraph Editor"

    override fun setState(state: FileEditorState) {
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun getCurrentLocation(): FileEditorLocation? {
        return null;
    }
}

class CrayonScriptJGraphEditor(private val crayonScriptProject: CrayonScriptProject, private val file: VirtualFile) : JPanel() {

    private val graphView:CrayonScriptJGraphView = CrayonScriptJGraphView(crayonScriptProject, file, CrayonScriptJGraph())
    private val crayonScriptUnityToolWindow:CrayonScriptUnityToolWindow = CrayonScriptUnityToolWindow(crayonScriptProject);
    private val libraryView:JBTabbedPane = JBTabbedPane()
    private val shapesPalette:EditorPalette?

    init {
        this.layout = BorderLayout()

        val crayonScriptToolWindowContent = this.crayonScriptUnityToolWindow.createContent()

        val innerSplitPane = JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            this.graphView,
            crayonScriptToolWindowContent
        )
        innerSplitPane.isOneTouchExpandable = true
        innerSplitPane.dividerLocation = 800
        innerSplitPane.dividerSize = 2
        innerSplitPane.border = null

        val graph = this.graphView.graph;

        shapesPalette = insertPalette(mxResources.get("shapes"));
        shapesPalette.addListener("select", mxIEventListener { _, var2 ->
            val transferable = var2.getProperty("transferable")
            if (transferable is mxGraphTransferable) {
                val mxGraphTransferable = transferable as mxGraphTransferable
                val cell = mxGraphTransferable.cells[0]
                if (graph.model.isEdge(cell)) {
                    // TODO: (graph as CrayonScriptJGraph).setEdgeTemplate(var5)
                }
            }
        })
        val clsLoader = mxGraphTransferable::class.java.classLoader
        Thread.currentThread().contextClassLoader = clsLoader
        shapesPalette.addTemplate(
                "Container",
                ImageIcon(
                    shapesPalette::class.java
                        .getResource("/com/mxgraph/assets/images/swimlane.png")
                ),
                "swimlane", 280, 280, "Container"
            )

        val outerSplitPane = JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            libraryView,
            innerSplitPane
        )

        this.add(outerSplitPane, BorderLayout.CENTER)
    }

    /**
     *
     */
    private fun insertPalette(title: String?): EditorPalette {
        val palette = EditorPalette()
        val scrollPane = JBScrollPane(palette)
        scrollPane.verticalScrollBarPolicy = mxGraphComponent.VERTICAL_SCROLLBAR_ALWAYS
        scrollPane.horizontalScrollBarPolicy = mxGraphComponent.HORIZONTAL_SCROLLBAR_NEVER
        libraryView.add(title, scrollPane)

        // Updates the widths of the palettes if the container size changes
        libraryView.addComponentListener(object : ComponentAdapter() {
            /**
             *
             */
            override fun componentResized(e: ComponentEvent) {
                val w = (scrollPane.width
                        - scrollPane.verticalScrollBar.width)
                palette.setPreferredWidth(w)
            }
        })
        return palette
    }

    companion object {

        init {
            try {
                mxResources.add("com/mxgraph/swing/resources/editor")
            } catch (e: Exception) {
                // ignore
            }
        }
    }

}

class CrayonScriptJGraphView(
    private val crayonScriptProject: CrayonScriptProject,
    private val file: VirtualFile,
    private val crayonScriptGraph: CrayonScriptJGraph) : mxGraphComponent(crayonScriptGraph){

    init {

        // Sets switches typically used in an editor
        isPageVisible = true
        isGridVisible = true
        isPreferPageSize = true

        setToolTips(true)
        getConnectionHandler().isCreateTarget = true

        // Loads the default stylesheet from an external file
        val codec = mxCodec()
        val doc = mxUtils.loadDocument(
            mxGraph::class.java.getResource(
                "/com/mxgraph/swing/resources/default-style.xml"
            )
                .toString()
        )
        codec.decode(doc.documentElement, crayonScriptGraph.stylesheet)

        // Sets the background to white
        getViewport().isOpaque = true
        getViewport().background = Color.WHITE

        var pageFormat = PageFormat()
        var paper:Paper = pageFormat.paper
        paper.setSize(65536.0, 65536.0)
        pageFormat.paper = paper
        this.setPageFormat(pageFormat)

        graphControl.updatePreferredSize()
    }

}

class CrayonScriptJGraph : mxGraph() {

}