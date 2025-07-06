package me.owdding.devutils.utils

import com.mojang.blaze3d.platform.InputConstants
import me.owdding.ktmodules.Module
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.screen.ScreenKeyPressedEvent
import tech.thatgravyboat.skyblockapi.api.events.screen.ScreenKeyReleasedEvent
import tech.thatgravyboat.skyblockapi.api.events.time.TickEvent

data class DevUtilsKeybinding(
    private val translationKey: String,
    private val keyCode: Int = InputConstants.UNKNOWN.value,
    private val allowMultipleExecutions: Boolean = false,
    private val runnable: (() -> Unit)? = null,
) {
    init {
        if (runnable != null) {
            knownKeybinds.add(this)
        }
    }

    val key: KeyMapping = KeyBindingHelper.registerKeyBinding(KeyMapping(translationKey, keyCode, "devutils"))

    val isDown get() = key.isDown

    fun matches(keyCode: Int, scancode: Int) = key.matches(keyCode, scancode)
    fun matches(event: ScreenKeyReleasedEvent) = matches(event.key, event.scanCode)
    fun matches(event: ScreenKeyPressedEvent) = matches(event.key, event.scanCode)

    @Module
    companion object {
        private val knownKeybinds = mutableListOf<DevUtilsKeybinding>()

        @Subscription(event = [TickEvent::class])
        fun onTick() {
            knownKeybinds.forEach { keybind ->
                if (keybind.allowMultipleExecutions && keybind.isDown) {
                    keybind.runnable?.invoke()
                } else if (keybind.key.consumeClick()) {
                    keybind.runnable?.invoke()
                }
            }
        }
    }
}