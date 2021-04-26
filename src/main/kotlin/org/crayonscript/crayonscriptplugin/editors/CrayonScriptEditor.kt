package org.crayonscript.crayonscriptplugin.editors

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import java.beans.PropertyChangeListener
import javax.swing.JComponent

class CrayonScriptEditor(private val project: Project, private val file: VirtualFile) : FileEditor, DumbAware {

    override fun getFile() = file

    override fun <T : Any?> getUserData(key: Key<T>): T? {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

    override fun getComponent(): JComponent {
        TODO("Not yet implemented")
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        TODO("Not yet implemented")
    }

    override fun getName(): String = "CrayonScript Editor"

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