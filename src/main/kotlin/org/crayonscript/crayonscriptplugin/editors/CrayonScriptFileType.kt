package org.crayonscript.crayonscriptplugin.editors

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import org.crayonscript.crayonscriptplugin.CrayonScriptLanguage
import javax.swing.Icon

object CrayonScriptFileType : LanguageFileType(CrayonScriptLanguage){
    override fun getName() = "CrayonScript"
    override fun getDescription() = "CrayonScript File"
    override fun getDefaultExtension() = "crayonscript"
    override fun getIcon(): Icon = CrayonScriptIcon.FILE
}

object CrayonScriptIcon {
    val FILE = IconLoader.getIcon("/icons/crayonscripticon.svg", this.javaClass)
}