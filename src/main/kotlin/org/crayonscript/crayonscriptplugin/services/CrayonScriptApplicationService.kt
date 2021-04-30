package org.crayonscript.crayonscriptplugin.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.DumbAware
import org.crayonscript.crayonscriptplugin.CrayonScriptBundle

class CrayonScriptApplicationService : DumbAware, Disposable {

    init {
        println(CrayonScriptBundle.message("applicationService"))
    }

    /**
     * Usually not invoked directly, see class javadoc.
     */
    override fun dispose() {
        TODO("Not yet implemented")
    }
}
