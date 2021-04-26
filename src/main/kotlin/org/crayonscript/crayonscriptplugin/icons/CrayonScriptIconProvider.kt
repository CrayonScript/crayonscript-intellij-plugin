package org.crayonscript.crayonscriptplugin.icons

import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import javax.swing.Icon

import org.crayonscript.crayonscriptplugin.editors.CrayonScriptIcon

class CrayonScriptIconProvider : DumbAware, IconProvider() {
    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        if (element !is PsiFile) return null;
        val file = element.virtualFile ?: return null;
        if (file.isDirectory || !file.exists()) {
            return null;
        }
        val extensions = arrayOf(".crayonscript")
        if (!extensions.any { ext -> file.name.endsWith(ext) }) {
            return null;
        }
        return CrayonScriptIcon.FILE
    }
}