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
    }
}