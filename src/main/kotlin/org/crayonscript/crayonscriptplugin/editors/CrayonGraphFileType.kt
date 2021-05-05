package org.crayonscript.crayonscriptplugin.editors

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import org.crayonscript.crayonscriptplugin.CrayonGraphLanguage
import javax.swing.Icon

object CrayonGraphFileType : LanguageFileType(CrayonGraphLanguage){
    override fun getName() = "CrayonGraph"
    override fun getDescription() = "CrayonGraph File"
    override fun getDefaultExtension() = "crayongraph"
    override fun getIcon(): Icon = CrayonGraphIcon.FILE
}

object CrayonGraphIcon {
    val FILE = IconLoader.getIcon("/icons/crayongraphicon.svg", this.javaClass)
}