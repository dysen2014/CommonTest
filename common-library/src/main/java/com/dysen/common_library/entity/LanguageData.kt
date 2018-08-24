package com.dysen.common_library.entity

/**
 * xiezuofei
 * 2018-07-10 13:20
 * 793169940@qq.com
 * 语言切换类
 */
class LanguageData{
    var name = ""//名称
    var name_local = ""//名称
    var language = 0
    constructor() {
    }
    constructor(name: String, name_local: String,language:Int) {
        this.name = name
        this.name_local = name_local
        this.language = language
    }
    override fun toString(): String {
        return "LanguageData{" +
                "name='" + name + '\''.toString() +
                ", name_local='" + name_local + '\''.toString() +
                ", language='" + language + '\''.toString() +
                '}'.toString()
    }
}
