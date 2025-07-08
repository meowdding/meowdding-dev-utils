package me.owdding.devutils.imgui

import imgui.ImGui
import imgui.ImGuiIO
import me.owdding.devutils.utils.PopupScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.helpers.McScreen

abstract class ImScreen : Screen(CommonComponents.EMPTY), ImKotlinHelper {

    override fun init() {
        ImGui.getIO().clearInputKeys()
        ImGui.getIO().clearInputCharacters()
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        ImGuiHelper.draw(::draw)
        guiGraphics.fill(0, 0, 10, 10, 0)
    }

    abstract fun draw(io: ImGuiIO)

    override fun isPauseScreen(): Boolean {
        return false
    }
}

abstract class ImPopupScreen(val parent: Screen? = McScreen.self) : ImScreen(), PopupScreen {

    override fun resize(minecraft: Minecraft, width: Int, height: Int) {
        super.resize(minecraft, width, height)
        parent?.resize(minecraft, width, height)
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        parent?.render(guiGraphics, -1, -1, partialTick)
        guiGraphics.fill(0, 0, 1, 1, 0)
        parent?.let { renderBlurredBackground() }

        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    override fun removed() {
        super.removed()
        McClient.setScreenAsync { parent }
    }

    override fun onClose() {
        super.onClose()
        McClient.setScreenAsync { parent }
    }

    override fun shouldCloseOnEsc() = true

}