package me.owdding.misc.utils.features.itemdata

import me.owdding.ktmodules.Module
import me.owdding.misc.utils.MiscUtils
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.screen.ScreenKeyPressedEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.helpers.McScreen
import tech.thatgravyboat.skyblockapi.utils.extentions.getHoveredSlot

@Module
object ItemData {

    @Subscription
    private fun ScreenKeyPressedEvent.Pre.onEvent() {
        if (!MiscUtils.ITEM_DEBUG.matches(this)) return
        val slot = McScreen.asMenu?.getHoveredSlot() ?: return
        McClient.setScreenAsync { ItemDataScreen(slot.item) }
        this.cancel()
    }

}