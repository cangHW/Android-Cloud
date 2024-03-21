package com.proxy.service.buildsrc

/**
 * @author: cangHX
 * @data: 2024/3/21 13:58
 * @desc:
 */
enum class MavenConfig constructor(private val isLoadMaven: Boolean) {

    Cloud_Base(true),
    Cloud_Annotations(true),
    Cloud_Api(true),
    Cloud_Compiler(true),
    Cloud_Plugin(true),

    Service_Utils_Base(true),
    Service_Utils_Info(true),

    Service_Ui_Base(true),
    Service_Ui_Info(true),

    Service_Net_Base(true),
    Service_Net_Retrofit(true),

    Service_Media_Base(true),
    Service_Media_Info(true),;

    companion object {
        //总开关，控制使用本地依赖还是远程依赖
        private const val IsRelease = false
    }

    fun isLoadMaven(): Boolean {
        return IsRelease && isLoadMaven
    }

}