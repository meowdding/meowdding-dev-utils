package me.owdding.misc.utils

import me.owdding.ktmodules.Module
import me.owdding.misc.utils.features.itemdata.ItemDataScreen
import me.owdding.skyocean.generated.MiscUtilsModules
import net.fabricmc.api.ClientModInitializer
import net.minecraft.world.item.Items
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient

@Module
class MiscUtils : ClientModInitializer {
    override fun onInitializeClient() {
        MiscUtilsModules.init { SkyBlockAPI.eventBus.register(it) }
    }

    @Subscription
    fun commands(event: RegisterCommandsEvent) {
        event.registerWithCallback("imgui") {
            McClient.setScreenAsync { ItemDataScreen(Items.DIAMOND.defaultInstance) }
        }
    }
}