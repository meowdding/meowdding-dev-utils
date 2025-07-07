package me.owdding.devutils.serializer

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import java.util.*

object TagComponentSerializer {

    fun serialize(component: Component): String {
        val builder = StringBuilder()
        val styleStack = LinkedList<Style>()
        val openTags = LinkedList<Array<String>>()

        fun peek() = styleStack.peek() ?: Style.EMPTY
        fun pop() = if (styleStack.isNotEmpty()) styleStack.pop() else Style.EMPTY
        fun push(style: Style) = styleStack.push(style)

        fun popTags() = if (openTags.isNotEmpty()) openTags.pop() else emptyArray<String>()
        fun pushTags(vararg tags: String) = openTags.push(tags.toList().toTypedArray())
        fun applyCloseTags(tags: Iterable<String>) =
            tags.reversed().map { it.substringBefore(" ") }.forEach { builder.append("</$it>") }

        component.visualOrderText.accept { position, style, codepoint ->
            run {
                if (peek() == style) {
                    return@run
                }

                pop()
                val poppedTags = popTags().toList()
                if (peek() == style) {
                    applyCloseTags(poppedTags)
                    return@run
                } else {
                    val tags = style.toTags()
                    val duplicates = poppedTags.filter(tags::contains)
                    applyCloseTags(poppedTags.filterNot(duplicates::contains))
                    tags.filterNot(duplicates::contains).forEach { tag -> builder.append("<$tag>") }
                    pushTags(*tags.toTypedArray())
                    push(style)
                }
            }
            val chars = Character.toChars(codepoint)
            builder.append(chars)

            true
        }
        applyCloseTags(openTags.map { it.toList() }.flatten().distinct())

        return builder.toString()
    }

    private fun Style.toTags(): List<String> {
        val list = mutableListOf<String>()

        if (color != null) {
            val color = color!!.serialize()
            if (color.startsWith("#")) {
                list.add("color $color")
            } else {
                list.add("$color")
            }
        }

        if (isBold) list.add("bold")
        if (isItalic) list.add("italic")
        if (isObfuscated) list.add("obfuscated")
        if (isUnderlined) list.add("underlined")
        if (isStrikethrough) list.add("strikethrough")

        return list
    }

    fun serialize(components: Iterable<Component>) = components.map(::serialize)

}