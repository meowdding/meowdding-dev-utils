package me.owdding.devutils.features.entity

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.suggestion.SuggestionProvider
import me.owdding.devutils.utils.sendWithPrefix
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.platform.save
import tech.thatgravyboat.skyblockapi.utils.json.Json.toJson
import tech.thatgravyboat.skyblockapi.utils.json.Json.toPrettyString
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.send
import kotlin.jvm.optionals.getOrNull


object EntityData {

    private val suggestions = SuggestionProvider<FabricClientCommandSource> { _, builder ->
        builder.suggest("*") // Suggest all entities
        SharedSuggestionProvider.suggestResource(BuiltInRegistries.ENTITY_TYPE.keySet(), builder)
    }

    const val DEFAULT_RADIUS = 10

    private fun getEntityFilter(input: String): ((EntityType<*>) -> Boolean) {
        if (input == "*") {
            return { true } // Match all entity types
        }
        val id = ResourceLocation.tryParse(input) ?: return { false }
        val type = BuiltInRegistries.ENTITY_TYPE.getOptional(id).getOrNull() ?: return { false }
        return { it == type }
    }

    private fun getHoveredEntity(): Entity? {
        val hoveredEntity = McClient.self.crosshairPickEntity
        if (hoveredEntity == null) {
            Text.of("No entity is currently hovered.").sendWithPrefix()
        }
        return hoveredEntity
    }

    @Subscription
    fun onCommandsRegistration(event: RegisterCommandsEvent) {
        event.register("devutils copy entities") {
            then("range", IntegerArgumentType.integer()) {
                then("filter", StringArgumentType.greedyString(), suggestions) {
                    callback {
                        val range = IntegerArgumentType.getInteger(this, "range")
                        val filter = StringArgumentType.getString(this, "filter")
                        //copyEntitiesToClipboard(filter, range)
                    }
                }
            }
        }

        event.register("devutils copy entity") {
            then("texture") {
                callback {
                    getHoveredEntity()?.let {
                        if (it is AbstractClientPlayer) {
                            McClient.clipboard = it.skin.textureUrl()!!
                            Text.of("Copied texture to clipboard.").send()
                        } else {
                            Text.of("Hovered entity is not a player, cannot copy texture.").send()
                        }
                    }
                }
            }

            callback {
                val hoveredEntity = McClient.self.crosshairPickEntity
                if (hoveredEntity == null) {
                    Text.of("No entity is currently hovered.").sendWithPrefix()
                } else {
                    val tag = CompoundTag()
                    tag.putString("id", EntityType.getKey(hoveredEntity.type).toString())
                    tag.put("data", hoveredEntity.save())
                    val json = tag.toJson(CompoundTag.CODEC).toPrettyString()
                    McClient.clipboard = json
                    Text.of("Copied entity ${hoveredEntity.name} to clipboard: $json").sendWithPrefix()
                }
            }
        }
    }

}