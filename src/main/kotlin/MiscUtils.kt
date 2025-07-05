package me.owdding.misc.utils

import me.owdding.ktmodules.Module
import me.owdding.misc.utils.screens.TestScreen
import me.owdding.skyocean.generated.MiscUtilsModules
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.InvalidateRenderStateCallback
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.api.events.render.RenderScreenBackgroundEvent
import tech.thatgravyboat.skyblockapi.api.events.render.RenderWorldEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient

@Module
class MiscUtils : ClientModInitializer {
    override fun onInitializeClient() {
        MiscUtilsModules.init { SkyBlockAPI.eventBus.register(it) }
    }

    @Subscription
    fun commands(event: RegisterCommandsEvent) {
        event.registerWithCallback("imgui") {
            McClient.setScreenAsync { TestScreen() }
        }
    }
}