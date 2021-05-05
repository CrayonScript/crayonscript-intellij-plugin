package org.crayonscript.crayonscriptplugin.editors

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class CrayonGraphEditorProvider : FileEditorProvider, DumbAware {

    override fun accept(project: Project, file: VirtualFile): Boolean {
        if (file.isValid) {
            if (file.extension == "crayongraph") {
               return true
            }
        }
        return false
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor = CrayonGraphEditor(project, file)

    override fun getEditorTypeId(): String {
        return "crayongraph"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR
    }

}