package me.owdding.misc.utils.utils

import com.mojang.serialization.JsonOps
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack


fun Component.asAdventureComponent() =
    ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow().let {
        GsonComponentSerializer.gson().deserialize(it.toString())
    }

operator fun Item.contains(item: ItemStack): Boolean = item.`is`(this)