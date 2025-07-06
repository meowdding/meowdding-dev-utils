package me.owdding.misc.utils.features.itemdata

import imgui.ImGuiIO
import imgui.flag.ImGuiInputTextFlags
import imgui.type.ImBoolean
import imgui.type.ImString
import me.owdding.misc.utils.imgui.ImPopupScreen
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.world.item.ItemStack
import parsers.HotfParser.asComponent
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.extentions.getLore
import tech.thatgravyboat.skyblockapi.utils.extentions.getRawLore
import tech.thatgravyboat.skyblockapi.utils.json.Json.toJson
import tech.thatgravyboat.skyblockapi.utils.json.Json.toPrettyString
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.send

class ItemDataScreen(val itemStack: ItemStack) : ImPopupScreen() {
    val meow = ImBoolean(false)
    val meow2 = ImBoolean(false)

    val customData by lazy {
        val tag = itemStack.get(DataComponents.CUSTOM_DATA)?.copyTag()
        val data = tag?.toJson(CompoundTag.CODEC)
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
                                .joinToString("\n") { MiniMessage.miniMessage().serialize(it.asComponent()) }
                            Text.of("Copied tag lore!").send()
                            println(data)
                            McClient.clipboard = data
                            onClose()
                        }
                    }
                    ImNewLine()
                    ImSameLine {
                        ImText("Custom Data")
                        ImButton("copy") {
                            println(customData.get())
                            Text.of("Copied custom data!").send()
                            McClient.clipboard = customData.get()
                            onClose()
                        }
                    }
                    ImMultilineTextInput("", customData, ImGuiInputTextFlags.ReadOnly)
                }
            }
        }
    }

}