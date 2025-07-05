package me.owdding.misc.utils.imgui

import imgui.ImGuiIO
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents

abstract class ImScreen : Screen(CommonComponents.EMPTY) {

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        ImGuiHelper.draw(::draw)
        guiGraphics.fill(0, 0, 10, 10, 0)
    }

    abstract fun draw(io: ImGuiIO)

    override fun isPauseScreen(): Boolean {
        return false
    }
}