package org.crayonscript.crayonscriptplugin.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.crayonscript.crayonscriptplugin.util.CrayonScriptUtils

class CrayonScriptProject(private val project: Project)
{
    private val scenes:List<VirtualFile>
    private var crayonScripts:List<VirtualFile>
    val name:String

    init {
        scenes = getScenes()
        crayonScripts = getCrayonScripts()
        name = project.name
    }

    fun getCrayonScripts(): List<VirtualFile> {
        return CrayonScriptUtils.getUnityAssetFilesByType(project, CrayonScriptUtils.CRAYONSCRIPT_FILE_TYPE)
    }

    fun getScenes(): List<VirtualFile> {
        return CrayonScriptUtils.getUnityAssetFilesByType(project, CrayonScriptUtils.SCENE_FILE_TYPE)
    }

}