package org.crayonscript.crayonscriptplugin.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.crayonscript.crayonscriptplugin.util.CrayonScriptUtils

class CrayonScriptProject(private val project: Project)
{
    private val scenes:List<VirtualFile>
    private var crayonScripts:List<VirtualFile>

    init {
        scenes = getScenes()
        crayonScripts = getCrayonScripts()
    }

    fun getCrayonScripts():List<VirtualFile> {
        val virtualFiles = CrayonScriptUtils.getUnityAssetFilesByType(project, CrayonScriptUtils.CRAYONSCRIPT_FILE_TYPE)
        return virtualFiles
    }

    fun getScenes():List<VirtualFile> {
        var virtualFiles = CrayonScriptUtils.getUnityAssetFilesByType(project, CrayonScriptUtils.SCENE_FILE_TYPE)
        return virtualFiles
    }

}