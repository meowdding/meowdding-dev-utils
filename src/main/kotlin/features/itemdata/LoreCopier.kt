package me.owdding.devutils.features.itemdata

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import tech.thatgravyboat.skyblockapi.utils.json.JsonArray

enum class LoreCopier {

    STRING {
        override fun copy(lines: List<String>) = lines.joinToString("\n")
    },
    JSON_ARRAY {
        val gson: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

        override fun copy(lines: List<String>): String = gson.toJson(JsonArray { lines.forEach(::add) })
    },
    ;

    abstract fun copy(lines: List<String>): String

}