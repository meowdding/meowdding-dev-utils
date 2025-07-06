package me.owdding.devutils

import me.owdding.devutils.generated.DevUtilsModules
import me.owdding.devutils.utils.DevUtilsKeybinding
import me.owdding.ktmodules.Module
import net.fabricmc.api.ClientModInitializer
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI

@Module
object DevUtils : ClientModInitializer {
    override fun onInitializeClient() {
        DevUtilsModules.init { SkyBlockAPI.eventBus.register(it) }
    }

    val ITEM_DEBUG = DevUtilsKeybinding("devutils.keybind.item_debug")
}