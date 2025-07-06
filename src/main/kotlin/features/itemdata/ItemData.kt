package me.owdding.misc.utils.features.itemdata

import me.owdding.ktmodules.Module
import org.lwjgl.glfw.GLFW
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.screen.ScreenMouseClickEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.helpers.McScreen
import tech.thatgravyboat.skyblockapi.utils.extentions.getHoveredSlot

@Module
object ItemData {

    @Subscription
    private fun ScreenMouseClickEvent.Pre.onEvent() {
        val slot = McScreen.asMenu?.getHoveredSlot() ?: return
        if (button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) return
        McClient.setScreenAsync { ItemDataScreen(slot.item) }
        this.cancel()
    }

}