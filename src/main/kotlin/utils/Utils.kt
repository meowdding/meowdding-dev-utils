package me.owdding.devutils.utils

import com.mojang.serialization.JsonOps
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.send
import tech.thatgravyboat.skyblockapi.utils.text.TextColor
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.color

val PREFIX = Text.of("[Dev Utils] ") { color = TextColor.DARK_AQUA }

fun Component.asAdventureComponent() =
    ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow().let {
        GsonComponentSerializer.gson().deserialize(it.toString())
    }

operator fun Item.contains(item: ItemStack): Boolean = item.`is`(this)

fun Component.sendWithPrefix() = Text.join(PREFIX, this).send()