package me.owdding.devutils.features.itemdata

import com.google.gson.JsonObject
import imgui.ImGuiIO
import imgui.flag.ImGuiInputTextFlags
import imgui.type.ImString
import me.owdding.devutils.imgui.ImPopupScreen
import me.owdding.devutils.utils.asAdventureComponent
import me.owdding.devutils.utils.contains
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import tech.thatgravyboat.skyblockapi.api.datatype.DataTypes
import tech.thatgravyboat.skyblockapi.api.datatype.getData
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.extentions.getLore
import tech.thatgravyboat.skyblockapi.utils.extentions.getRawLore
import tech.thatgravyboat.skyblockapi.utils.extentions.getTexture
import tech.thatgravyboat.skyblockapi.utils.json.Json.readJson
import tech.thatgravyboat.skyblockapi.utils.json.Json.toJson
import tech.thatgravyboat.skyblockapi.utils.json.Json.toPrettyString
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.send
import java.util.*

class ItemDataScreen(val itemStack: ItemStack) : ImPopupScreen() {
    val customData by lazy {
        val tag = itemStack.get(DataComponents.CUSTOM_DATA)?.copyTag()
        val data = tag?.toJson(CompoundTag.CODEC)
        ImString(data.toPrettyString())
    }

    val itemData by lazy {
        val data = itemStack.toJson(ItemStack.OPTIONAL_CODEC)
        ImString(data.toPrettyString())
    }

    override fun draw(io: ImGuiIO) {
        ImWindow("Item") {
            ImTabBar("things") {
                ImTab("Data") {
                    ImSameLine {
                        ImText("Lore")
                        ImButton("Component") {
                            val data = itemStack.getLore().toJson(ComponentSerialization.CODEC.listOf())
                            Text.of("Copied component lore!").send()
                            McClient.clipboard = data.toPrettyString()
                            onClose()
                        }
                        ImButton("Raw") {
                            val data = itemStack.getRawLore().joinToString("\n")
                            Text.of("Copied raw lore!").send()
                            McClient.clipboard = data
                            onClose()
                        }
                        ImButton("Tags") {
                            val data = itemStack.getLore()
                                .joinToString("\n") { MiniMessage.miniMessage().serialize(it.asAdventureComponent()) }
                                .replace(Regex("<!.*?>|<(.+)></\\1>"), "")
                            Text.of("Copied tag lore!").send()
                            McClient.clipboard = data
                            onClose()
                        }
                    }
                    if (itemStack in Items.PLAYER_HEAD) {
                        ImSameLine {
                            ImButton("Copy Player Skin") {
                                Text.of("Copied player skin!").send()
                                McClient.clipboard = itemStack.getTexture()
                                onClose()
                            }
                            ImButton("Skin (URL)") {
                                runCatching {
                                    McClient.clipboard =
                                        Base64.getDecoder().decode(itemStack.getTexture()).decodeToString()
                                            .readJson<JsonObject>().getAsJsonObject("textures").getAsJsonObject("SKIN")
                                            .get("url").asString
                                    Text.of("Copied player skin (url)!").send()
                                }.getOrElse {
                                    Text.of("Failed json deserialization!").send()
                                    Text.of("Copied player skin (json fallback)!").send()
                                    McClient.clipboard =
                                        Base64.getDecoder().decode(itemStack.getTexture()).decodeToString()
                                }
                                onClose()
                            }
                        }
                    }
                    ImNewLine()
                    ImCollapse("Custom Data") {
                        ImSameLine {
                            ImText("Custom Data")
                            ImButton("copy") {
                                Text.of("Copied custom data!").send()
                                McClient.clipboard = customData.get()
                                onClose()
                            }
                            ImButton("Sb Id") {
                                Text.of("Copied skyblock id!").send()
                                McClient.clipboard = itemStack.getData(DataTypes.ID)
                                onClose()
                            }
                            ImButton("Api Id") {
                                Text.of("Copied skyblock id!").send()
                                McClient.clipboard = itemStack.getData(DataTypes.ID)
                                onClose()
                            }
                        }
                        ImMultilineTextInput("##customData", customData, ImGuiInputTextFlags.ReadOnly)
                    }
                    ImCollapse("Item") {
                        ImSameLine {
                            ImText("Item Data")
                            ImButton("copy") {
                                Text.of("Copied item data!").send()
                                McClient.clipboard = itemData.get()
                                onClose()
                            }
                        }
                        ImMultilineTextInput("##serializedItem", itemData, ImGuiInputTextFlags.ReadOnly, 0f, 200f)
                    }
                }
            }
        }
    }

}