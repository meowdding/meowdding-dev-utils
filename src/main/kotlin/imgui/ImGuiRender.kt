package me.owdding.misc.utils.imgui

import imgui.ImGuiIO

fun interface ImGuiRender {
    fun render(io: ImGuiIO)
    //operator fun invoke(io: ImGuiIO) { render(io) }
}