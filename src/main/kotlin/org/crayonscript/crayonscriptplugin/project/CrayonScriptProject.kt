package org.crayonscript.crayonscriptplugin.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.crayonscript.crayonscriptplugin.util.CrayonScriptUnityObjectNode
import org.crayonscript.crayonscriptplugin.util.CrayonScriptUtils

class CrayonScriptProject(private val project: Project)
{
    private val scenes:List<VirtualFile>
    private val crayonScripts:List<VirtualFile>

    val rootObjectNode:CrayonScriptUnityObjectNode
    val name:String

    init {
        scenes = getScenes()
        crayonScripts = getCrayonScripts()
        name = project.name

        rootObjectNode = CrayonScriptUnityObjectNode(
            CrayonScriptUnityObjectNode.ROOT_OBJECT_ID,
            CrayonScriptUnityObjectNode.ROOT_FILE_ID
        )
        rootObjectNode.processTopNode(this)
    }

    fun setupNodeObjects() {
        rootObjectNode.clearChildren()
        var sceneObjects = getSceneObjectNodes()
        for (sceneObject in sceneObjects) {
            rootObjectNode.addChild(sceneObject)
        }
    }

    private fun getCrayonScripts(): List<VirtualFile> {
        return CrayonScriptUtils.getUnityAssetFilesByType(project, CrayonScriptUtils.CRAYONSCRIPT_FILE_TYPE)
    }

    private fun getScenes(): List<VirtualFile> {
        return CrayonScriptUtils.getUnityAssetFilesByType(project, CrayonScriptUtils.SCENE_FILE_TYPE)
    }

    private fun getSceneObjectNodes():List<CrayonScriptUnityObjectNode> {
        var sceneObjects = mutableListOf<CrayonScriptUnityObjectNode>()
        var scenes = getScenes()
        for (scene in scenes) {
            var sceneObject = CrayonScriptUtils.getSceneObject(scene)
            sceneObjects.add(sceneObject)
        }
        return sceneObjects
    }

}