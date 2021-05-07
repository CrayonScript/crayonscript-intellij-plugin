package org.crayonscript.crayonscriptplugin.editors

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import java.awt.BorderLayout
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

    init {
        this.layout = BorderLayout()
        //val graphView = CrayonScriptJGraphView(project, file, CrayonScriptJGraph())
    }

}

class CrayonScriptJGraphView(
    private val project: Project,
    private val file: VirtualFile,
    private val graph: CrayonScriptJGraph){

}

class CrayonScriptJGraph {

}