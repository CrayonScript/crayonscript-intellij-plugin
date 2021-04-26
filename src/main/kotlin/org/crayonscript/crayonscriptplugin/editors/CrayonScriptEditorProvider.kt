package org.crayonscript.crayonscriptplugin.editors

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class CrayonScriptEditorProvider : FileEditorProvider, DumbAware {

    override fun accept(project: Project, file: VirtualFile): Boolean {
        TODO("Not yet implemented")
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor = CrayonScriptEditor(project, file)

    override fun getEditorTypeId(): String {
        TODO("Not yet implemented")
    }

    override fun getPolicy(): FileEditorPolicy {
        TODO("Not yet implemented")
    }

}