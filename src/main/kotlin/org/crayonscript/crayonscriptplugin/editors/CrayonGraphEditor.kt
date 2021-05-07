package org.crayonscript.crayonscriptplugin.editors

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.mxgraph.io.mxCodec
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxUtils
import com.mxgraph.view.mxGraph
import java.awt.BorderLayout
import java.awt.Color
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

class CrayonGraphEditor(private val project: Project, private val file: VirtualFile) : FileEditor, DumbAware {

    private val jGraphEditor:CrayonScriptJGraphEditor = CrayonScriptJGraphEditor(project, file)

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

class CrayonScriptJGraphEditor(private val project: Project, private val file: VirtualFile) : JPanel() {

    private val graphView:CrayonScriptJGraphView = CrayonScriptJGraphView(project, file, CrayonScriptJGraph())

    init {
        this.layout = BorderLayout()
        this.add(this.graphView, BorderLayout.CENTER)
    }

}

class CrayonScriptJGraphView(
    private val project: Project,
    private val file: VirtualFile,
    private val crayonScriptGraph: CrayonScriptJGraph) : mxGraphComponent(crayonScriptGraph){

        init {
            // Sets switches typically used in an editor

            // Sets switches typically used in an editor
            isPageVisible = true
            isGridVisible = true
            setToolTips(true)
            getConnectionHandler().isCreateTarget = true

            // Loads the defalt stylesheet from an external file

            // Loads the defalt stylesheet from an external file
            val codec = mxCodec()
            val doc = mxUtils.loadDocument(
                mxGraph::class.java.getResource(
                    "/com/mxgraph/swing/resources/default-style.xml"
                )
                    .toString()
            )
            codec.decode(doc.documentElement, crayonScriptGraph.stylesheet)

            // Sets the background to white

            // Sets the background to white
            getViewport().isOpaque = true
            getViewport().background = Color.WHITE
        }
}

class CrayonScriptJGraph : mxGraph() {

}