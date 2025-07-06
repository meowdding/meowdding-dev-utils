package me.owdding.devutils

import me.owdding.devutils.generated.MiscUtilsModules
import me.owdding.devutils.utils.MiscUtilsKeybinding
import me.owdding.ktmodules.Module
import net.fabricmc.api.ClientModInitializer
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI

@Module
object DevUtils : ClientModInitializer {
    override fun onInitializeClient() {
        MiscUtilsModules.init { SkyBlockAPI.eventBus.register(it) }
    }

    val ITEM_DEBUG = MiscUtilsKeybinding("miscutils.keybind.item_debug")
}