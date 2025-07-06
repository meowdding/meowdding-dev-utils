package me.owdding.misc.utils.imgui

import imgui.ImGui
import imgui.ImGuiInputTextCallbackData
import imgui.ImVec2
import imgui.callback.ImGuiInputTextCallback
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiTreeNodeFlags
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import imgui.type.ImString
import org.intellij.lang.annotations.MagicConstant


sealed interface ImKotlin {
    val inline: Boolean

    fun ImSameLine(init: ImKotlin.() -> Unit) {
        copy(true).init()
    }

    fun ImNewLine() = ImGui.newLine()

    fun ImButton(label: String, size: ImVec2? = null, runnable: () -> Unit = {}) = modifiers {
        if (size != null) {
            if (ImGui.button(label, size)) runnable()
        } else {
            if (ImGui.button(label)) runnable()
        }
    }

    fun ImCollapse(
        label: String,
        state: ImBoolean? = null,
        @MagicConstant(flagsFromClass = ImGuiTreeNodeFlags::class) flags: Int = ImGuiTreeNodeFlags.None,
        init: ImKotlin.() -> Unit,
    ) = modifiers {
        if (when {
                state != null -> ImGui.collapsingHeader(label, state, flags)
                else -> ImGui.collapsingHeader(label, state, flags)
            }
        ) {
            ImGui.beginGroup()
            init()
            ImGui.endGroup()
        }
    }

    fun ImText(text: String) = modifiers {
        ImGui.text(text)
    }

    fun ImTextInput(
        label: String,
        text: ImString,
        @MagicConstant(valuesFromClass = ImGuiInputTextFlags::class) flags: Int = ImGuiInputTextFlags.None,
        callback: ((ImGuiInputTextCallbackData) -> Unit)?,
    ) = modifiers {
        if (callback != null) {
            ImGui.inputText(label, text, flags, ImGuiInputTextCallbackHandler(callback))
        } else {
            ImGui.inputText(label, text, flags)
        }
    }

    fun ImMultilineTextInput(
        label: String,
        text: ImString,
        @MagicConstant(valuesFromClass = ImGuiInputTextFlags::class) flags: Int = ImGuiInputTextFlags.None,
        width: Float? = null,
        height: Float? = null,
        callback: ((ImGuiInputTextCallbackData) -> Unit)? = null,
    ) = modifiers {
        when {
            width != null && height != null && callback != null -> {
                ImGui.inputTextMultiline(label, text, width, height, flags, ImGuiInputTextCallbackHandler(callback))
            }

            width != null && height != null -> {
                ImGui.inputTextMultiline(label, text, width, height, flags)
            }

            callback != null -> {
                ImGui.inputTextMultiline(label, text, flags, ImGuiInputTextCallbackHandler(callback))
            }

            else -> ImGui.inputTextMultiline(label, text, flags)
        }
    }

    private inline fun modifiers(runnable: () -> Unit) {
        if (inline) ImGui.sameLine()
        runnable()
    }

    private fun copy(
        inline: Boolean = false,
    ) = ImContext(inline)

    fun ImTabBar(id: String, init: ImKtTabBar.() -> Unit) {
        ImGui.beginTabBar(id)
        ImKtTabBar.init()
        ImGui.endTabBar()
    }

    fun ImWindow(
        title: String,
        @MagicConstant(valuesFromClass = ImGuiWindowFlags::class) flags: Int = ImGuiWindowFlags.None,
        init: ImKotlin.() -> Unit,
    ) {
        if (ImGui.begin(title, flags)) {
            ImContext.INSTANCE.init()
        }
        ImGui.end()
    }
}

private data class ImGuiInputTextCallbackHandler(val callback: (ImGuiInputTextCallbackData) -> Unit) :
    ImGuiInputTextCallback() {
    override fun accept(data: ImGuiInputTextCallbackData) = callback(data)
}

interface ImKotlinHelper {
    fun ImWindow(
        title: String,
        @MagicConstant(valuesFromClass = ImGuiWindowFlags::class) flags: Int = ImGuiWindowFlags.None,
        init: ImKotlin.() -> Unit,
    ) = ImContext.INSTANCE.ImWindow(title, flags, init)
}

object ImKtTabBar {
    fun ImTab(name: String, init: ImKotlin.() -> Unit) {
        if (ImGui.beginTabItem(name)) {
            ImContext.INSTANCE.init()
            ImGui.endTabItem()
        }
    }
}

private data class ImContext(
    val expectInline: Boolean = false,
) : ImKotlin {
    var hasPassedFirstInline = false
    override val inline: Boolean
        get() = if (!expectInline) false else {
            if (hasPassedFirstInline) true else {
                hasPassedFirstInline = true
                false
            }
        }


    companion object {
        val INSTANCE = ImContext()
    }
}