package org.crayonscript.crayonscriptplugin.editors

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

@Deprecated("use default editor provider")
class CrayonScriptEditorProvider : FileEditorProvider, DumbAware {

    override fun accept(project: Project, file: VirtualFile): Boolean {
        if (file.isValid) {
            if (file.extension == "crayonscript") {
               return true
            }
        }
        return false
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor = CrayonScriptEditor(project, file)

    override fun getEditorTypeId(): String {
        return "crayonscript"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR
    }

}