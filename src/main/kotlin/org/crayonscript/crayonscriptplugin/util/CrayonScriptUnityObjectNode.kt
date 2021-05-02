package org.crayonscript.crayonscriptplugin.util

import com.intellij.openapi.vfs.VirtualFile
import org.crayonscript.crayonscriptplugin.project.CrayonScriptProject

class CrayonScriptUnityObjectNode(
    val objectId:Int,
    val fileId:Int
) {

    private var startIndex:Int = 0
    private var endIndex:Int = 0
    private var name:String = ""

    private val children:MutableList<CrayonScriptUnityObjectNode> = mutableListOf<CrayonScriptUnityObjectNode>()

    fun getName():String {
        return name
    }

    override fun toString(): String {
        return name
    }

    fun setStartIndex(startIndex:Int) {
        this.startIndex = startIndex
    }

    fun setEndIndex(endIndex:Int) {
        this.endIndex = endIndex
    }

    fun getChildren():List<CrayonScriptUnityObjectNode> {
        return this.children
    }

    fun addChild(child:CrayonScriptUnityObjectNode) {
        this.children.add(child)
    }

    fun clearChildren() {
        if (this.children != null && this.children.size > 0) {
            for (child in this.children) {
                child.clearChildren()
            }
            this.children.clear()
        }
    }

    fun processTopNode(crayonScriptProject:CrayonScriptProject) {
        this.name = crayonScriptProject.name
    }

    fun processSceneNode(sceneVirtualFile:VirtualFile) {
        this.name = sceneVirtualFile.nameWithoutExtension
    }

    fun processGameObjectNode(blockFileIdToNodeMap:Map<Int, CrayonScriptUnityObjectNode>, linesOfCode:List<String>) {
        // get the child nodes
        val childNodes = getChildNodes(blockFileIdToNodeMap, linesOfCode)
        for (childNode in childNodes) {
            children.add(childNode)
        }
        var nameFieldInfo = getFieldInfo("Name", linesOfCode)
        var nameFieldValue = nameFieldInfo!!.second
        this.name = nameFieldValue
    }

    fun isRootNode(blockFileIdToNodeMap: Map<Int, CrayonScriptUnityObjectNode>, linesOfCode: List<String>): Boolean {
        val transformNode = getTransformNode(blockFileIdToNodeMap, linesOfCode)
        val parentFieldInfo = transformNode!!.getFieldInfo("Father", linesOfCode)
        val parentFieldValue = parentFieldInfo!!.second
        var parentFieldMap = parseAsIntMap(parentFieldValue)
        var parentFileId = parentFieldMap["fileID"]
        return parentFileId == 0
    }

    private fun getFieldInfo(fieldName:String, linesOfCode: List<String>):Pair<Int, String>? {
        for (index in startIndex until endIndex) {
            if (fieldRegex.matches(linesOfCode[index])) {
                val matchResult = fieldRegex.matchEntire(linesOfCode[index])!!.destructured
                var matchedFieldName = matchResult.component1()
                matchedFieldName = matchedFieldName.trim()
                var matchedFieldValue = matchResult.component2()
                matchedFieldValue = matchedFieldValue.trim()
                if (matchedFieldName == fieldName) {
                    return Pair(index, matchedFieldValue)
                }
            }
        }
        return null
    }

    private fun getChildNodes(blockFileIdToNodeMap:Map<Int, CrayonScriptUnityObjectNode>,
                              linesOfCode: List<String>): List<CrayonScriptUnityObjectNode> {
        val transformNode = getTransformNode(blockFileIdToNodeMap, linesOfCode)
        var childTransformNodes = transformNode!!.getChildTransformNodes(blockFileIdToNodeMap, linesOfCode)
        var childGameObjectNodes = mutableListOf<CrayonScriptUnityObjectNode>()
        for (childTransformNode in childTransformNodes) {
            val childGameObjectFieldInfo = childTransformNode.getFieldInfo("GameObject", linesOfCode)
            val childGameObjectFieldValue = childGameObjectFieldInfo!!.second
            val childGameObjectFieldIdMap = parseAsIntMap(childGameObjectFieldValue)
            val childGameObjectFileId = childGameObjectFieldIdMap["fileID"]
            val childGameObjectNode = blockFileIdToNodeMap[childGameObjectFileId]
            childGameObjectNodes.add(childGameObjectNode!!)
        }
        return childGameObjectNodes
    }

    private fun getTransformNode(blockFileIdToNodeMap:Map<Int,
            CrayonScriptUnityObjectNode>, linesOfCode:List<String>): CrayonScriptUnityObjectNode? {
        var componentNodes:List<CrayonScriptUnityObjectNode> = getComponentNodes(blockFileIdToNodeMap, linesOfCode)
        for (componentNode in componentNodes) {
            if (componentNode.objectId == TRANSFORM_OBJECT_ID) {
                return componentNode
            }
        }

        return null
    }

    private fun getComponentNodes(
            blockFileIdToNodeMap:Map<Int, CrayonScriptUnityObjectNode>,
            linesOfCode: List<String>): List<CrayonScriptUnityObjectNode> {
        val componentNodes = mutableListOf<CrayonScriptUnityObjectNode>()
        val blockMap = getBlockMap("Component", blockFileIdToNodeMap, linesOfCode)
        var componentList = blockMap["component"]
        for (componentStr in componentList!!) {
            val componentMap = parseAsIntMap(componentStr)
            var componentFileId = componentMap["fileID"]
            var componentNode = blockFileIdToNodeMap[componentFileId]
            componentNodes.add(componentNode!!)
        }
        return componentNodes
    }

    private fun getChildTransformNodes(
        blockFileIdToNodeMap:Map<Int, CrayonScriptUnityObjectNode>,
        linesOfCode: List<String>): List<CrayonScriptUnityObjectNode> {
        val childNodes = mutableListOf<CrayonScriptUnityObjectNode>()
        val fieldInfo = getFieldInfo("Children", linesOfCode) ?: return childNodes
        val fieldValue = fieldInfo.second
        if (fieldValue.isNotEmpty()) {
            val childFileIds = parseAsIntArr(fieldValue)
            for (childFileId in childFileIds) {
                val childNode = blockFileIdToNodeMap[childFileId]
                childNodes.add(childNode!!)
            }
        } else {
            val childFileMapList = getBlockList("Children", blockFileIdToNodeMap, linesOfCode)
            for (childFileIdMapStr in childFileMapList) {
                val childFieldMap = parseAsIntMap(childFileIdMapStr)
                val childFileId = childFieldMap["fileID"]
                val childNode = blockFileIdToNodeMap[childFileId]
                childNodes.add(childNode!!)
            }
        }
        return childNodes
    }

    private fun getBlockMap(fieldName:String,
                            blockFileIdToNodeMap:Map<Int, CrayonScriptUnityObjectNode>,
                            linesOfCode:List<String>): Map<String, List<String>> {
        val blockMap = mutableMapOf<String, MutableList<String>>()
        val fieldInfo = getFieldInfo(fieldName, linesOfCode) ?: return blockMap
        val blockFieldStartIndex = fieldInfo.first + 1
        if (blockFieldStartIndex >= startIndex) {
            for (index in blockFieldStartIndex until endIndex) {
                if (!blockMapRegex.matches(linesOfCode[index])) {
                    break
                }
                val matchResults = blockMapRegex.matchEntire(linesOfCode[index])!!.destructured
                var fieldName = matchResults.component1()
                fieldName = fieldName.trim()
                var fieldValue = matchResults.component2()
                fieldValue = fieldValue.trim()
                if (!blockMap.containsKey(fieldName)) {
                    blockMap[fieldName] = mutableListOf()
                }
                blockMap[fieldName]!!.add(fieldValue)
            }
        }
        return blockMap
    }

    private fun getBlockList(fieldName:String,
                            blockFileIdToNodeMap:Map<Int, CrayonScriptUnityObjectNode>,
                            linesOfCode:List<String>): List<String> {
        val blockList = mutableListOf<String>()
        val fieldInfo = getFieldInfo(fieldName, linesOfCode) ?: return blockList
        val blockFieldStartIndex = fieldInfo.first + 1
        if (blockFieldStartIndex >= startIndex) {
            for (index in blockFieldStartIndex until endIndex) {
                if (!blockListRegex.matches(linesOfCode[index])) {
                    break
                }
                val matchResults = blockListRegex.matchEntire(linesOfCode[index])!!.destructured
                var fieldValue = matchResults.component1()
                fieldValue = fieldValue.trim()
                blockList.add(fieldValue)
            }
        }
        return blockList
    }

    // References
    // https://docs.unity3d.com/Manual/ClassIDReference.html
    // 1 = GameObject
    // 4 = Transform

    companion object {

        const val ROOT_OBJECT_ID = -2
        const val GAMEOBJECT_OBJECT_ID = 1
        const val TRANSFORM_OBJECT_ID = 4
        const val SCENE_OBJECT_ID = 1032

        const val ROOT_FILE_ID = -2
        const val SCENE_FILE_ID = 0

        val fieldRegex = Regex("\\s*m_([^:]+):\\s*(.*)")
        val blockMapRegex = Regex("\\s+-\\s+([^:]+):\\s+(.*)")
        val blockListRegex = Regex("\\s+-\\s+(.*)")

        fun parseAsInt(str:String):Int {
            return str.toInt()
        }

        fun parseAsFloat(str:String):Float {
            return str.toFloat()
        }

        fun parseAsIntArr(str:String):List<Int> {
            var fieldList = mutableListOf<Int>()

            var listStart = false
            var listEnd = false
            var fieldStart = false
            var fieldEnd = false

            var field:String = ""

            for (i in str.indices) {
                when {
                    str[i] == '[' -> {
                        listStart = true
                        listEnd = false
                        fieldStart = true
                        fieldEnd = false
                    }
                    str[i] == ']' -> {
                        listStart = false
                        listEnd = true
                        fieldStart = false
                        fieldEnd = true

                        if (field.isNotEmpty()) {
                            field = field.trim()
                            fieldList.add(field.toInt())
                        }
                    }
                    str[i] == ',' -> {
                        fieldStart = true
                        fieldEnd = false

                        if (field.isNotEmpty()) {
                            field = field.trim()
                            fieldList.add(field.toInt())
                        }
                    }
                    else -> {
                        if (fieldStart && !fieldEnd) {
                            field += str[i]
                        }
                    }
                }
            }
            return fieldList
        }

        fun parseAsIntMap(str:String):Map<String, Int> {

            var fieldMap = mutableMapOf<String, Int>()

            var mapStart = false
            var mapEnd = false
            var fieldStart = false
            var fieldEnd = false
            var keyStart = false
            var keyEnd = false
            var valueStart = false
            var valueEnd = false

            var key:String = ""
            var value:String = ""

            for (i in str.indices) {
                when {
                    str[i] == '{' -> {
                        mapStart = true
                        mapEnd = false
                        fieldStart = true
                        fieldEnd = false
                        keyStart = true
                        keyEnd = false
                        valueStart = false
                        valueEnd = false
                    }
                    str[i] == '}' -> {
                        mapStart = false
                        mapEnd = true
                        fieldStart = false
                        fieldEnd = true
                        keyStart = false
                        keyEnd = true
                        valueStart = false
                        valueEnd = true

                        if (key.isNotEmpty()) {
                            key = key.trim()
                            value = value.trim()
                            fieldMap[key] = value.toInt()
                        }

                        key = ""
                        value = ""
                    }
                    str[i] == ',' -> {
                        fieldStart = true
                        fieldEnd = false
                        keyStart = true
                        keyEnd = false
                        valueStart = false
                        valueEnd = false

                        if (key.isNotEmpty()) {
                            key = key.trim()
                            value = value.trim()
                            fieldMap[key] = value.toInt()
                        }

                        key = ""
                        value = ""
                    }
                    str[i] == ':' -> {
                        keyStart = false
                        keyEnd = true
                        valueStart = true
                        valueEnd = false
                    }
                    else -> {
                        if (keyStart && !keyEnd) {
                            key += str[i]
                        }
                        if (valueStart && !valueEnd) {
                            value += str[i]
                        }
                    }
                }
            }
            return fieldMap
        }
    }
}