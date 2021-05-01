package org.crayonscript.crayonscriptplugin.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.util.*

class CrayonScriptUtils {

    companion object {

        const val SCENE_FILE_TYPE:String = "Scene"

        const val CRAYONSCRIPT_FILE_TYPE:String = "CrayonScript"

        fun getUnityAssetFilesByType(project: Project, fileType:String ): List<VirtualFile> {
            // locate all scenes in Unity assets
            val projectDirVirtualFile = project.guessProjectDir()
            val assetsVirtualDir = projectDirVirtualFile!!.findChild("Assets")

            var mutableList = mutableListOf<VirtualFile>()

            VfsUtil.iterateChildrenRecursively(assetsVirtualDir!!, null) { fileOrDir ->
                if (!fileOrDir.exists()) false
                if (!fileOrDir.isValid) false
                if (fileType == SCENE_FILE_TYPE) {
                    val searchStringInYAML = "m_SceneGUID:"
                    if (!fileOrDir.isDirectory && fileOrDir.extension == "unity") {
                        val contentsAsByteArray = fileOrDir.contentsToByteArray()
                        if (contentsAsByteArray != null) {
                            val contents = String(contentsAsByteArray)
                            if (contents.contains(searchStringInYAML)) {
                                mutableList.add(fileOrDir)
                            }
                        }
                    }
                }
                else if (fileType == CRAYONSCRIPT_FILE_TYPE) {
                    if (!fileOrDir.isDirectory && fileOrDir.extension == "crayonscript") {
                        mutableList.add(fileOrDir)
                    }
                }

                true
            }

            return Collections.unmodifiableList(mutableList)
        }

        fun getSceneObject(sceneVirtualFile:VirtualFile):CrayonScriptUnityObjectNode {
            val content = String(sceneVirtualFile.contentsToByteArray())
            var linesOfCode = content.lines()

            val blockStartRegex = Regex("--- !u!(\\d+)\\s+&(\\d+)")

            val blockFileIdToNodeMap = mutableMapOf<Int, CrayonScriptUnityObjectNode>()

            var index = -1
            var blockFileId = -1

            while (++index < linesOfCode.size) {
                val line = linesOfCode[index]
                if (blockStartRegex.matches(line)) {
                    // new block
                    val matchResult = blockStartRegex.matchEntire(line)
                    val matchGroups = matchResult!!.groups
                    val objectId = matchGroups.elementAt(1)!!.value.toInt()
                    val fileId = matchGroups.elementAt(2)!!.value.toInt()

                    blockFileIdToNodeMap[fileId] = CrayonScriptUnityObjectNode(objectId, fileId)
                    blockFileIdToNodeMap[fileId]!!.setStartIndex(index)

                    // store the previous block
                    blockFileIdToNodeMap[blockFileId]?.setEndIndex(index)
                    // setup the new block start index
                    blockFileId = fileId
                }
            }

            // update end index for any final block info
            blockFileIdToNodeMap[blockFileId]?.setEndIndex(index)

            val sceneObject = CrayonScriptUnityObjectNode(
                CrayonScriptUnityObjectNode.SCENE_OBJECT_ID,
                CrayonScriptUnityObjectNode.SCENE_OBJECT_ID)

            val rootObjectNodes = mutableListOf<CrayonScriptUnityObjectNode>()

            // process the blocks
            for ((blockFileId, blockNode) in blockFileIdToNodeMap) {
                if (blockNode.objectId ==  CrayonScriptUnityObjectNode.GAMEOBJECT_OBJECT_ID) {
                    blockNode.processGameObjectNode(blockFileIdToNodeMap, linesOfCode)
                    if (blockNode.isRootNode(blockFileIdToNodeMap, linesOfCode)) {
                        rootObjectNodes.add(blockNode)
                    }
                }
            }

            sceneObject.processSceneNode(sceneVirtualFile)
            for (rootObjectNode in rootObjectNodes) {
                sceneObject.AddChild(rootObjectNode)
            }

            return sceneObject
        }
    }
}
