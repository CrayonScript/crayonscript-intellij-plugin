package org.crayonscript.crayonscriptplugin.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.NonEmptyInputValidator
import com.intellij.psi.PsiDirectory
import org.crayonscript.crayonscriptplugin.editors.CrayonGraphFileType
import org.crayonscript.crayonscriptplugin.editors.CrayonGraphIcon
import org.crayonscript.crayonscriptplugin.editors.CrayonScriptFileType
import org.crayonscript.crayonscriptplugin.editors.CrayonScriptIcon

class CrayonScriptCreateFileAction : CreateFileFromTemplateAction(
    "CrayonScript File",
    "Create new CrayonScript File",
    CrayonScriptIcon.FILE
), DumbAware {

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder
            .setTitle("New CrayonScript File") // add templates to filer src/main/resources/fileTemplates.internal
            .addKind("CrayonScript File", CrayonScriptIcon.FILE, "CrayonScriptFileTemplate")
            .addKind("CrayonGraph File", CrayonGraphIcon.FILE, "CrayonGraphFileTemplate")
            .setValidator(NonEmptyInputValidator())
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return "CrayonScript File"
    }
}