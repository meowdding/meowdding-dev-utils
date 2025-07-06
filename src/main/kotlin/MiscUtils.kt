package me.owdding.misc.utils

import me.owdding.ktmodules.Module
import me.owdding.misc.utils.generated.MiscUtilsModules
import me.owdding.misc.utils.utils.MiscUtilsKeybinding
import net.fabricmc.api.ClientModInitializer
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI

@Module
object MiscUtils : ClientModInitializer {
    override fun onInitializeClient() {
        MiscUtilsModules.init { SkyBlockAPI.eventBus.register(it) }
    }

    val ITEM_DEBUG = MiscUtilsKeybinding("miscutils.keybind.item_debug")
}