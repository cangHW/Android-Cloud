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

    Service_Utils_Base(false),
    Service_Utils_Info(false),

    Service_Ui_Base(false),
    Service_Ui_Info(false),

    Service_Net_Base(false),
    Service_Net_Retrofit(false),

    Service_Media_Base(false),
    Service_Media_Info(false),;

    companion object {
        //总开关，控制使用本地依赖还是远程依赖
        private const val IsRelease = true
    }

    fun isLoadMaven(): Boolean {
        return IsRelease && isLoadMaven
    }

}