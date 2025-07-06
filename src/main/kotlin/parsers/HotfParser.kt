package parsers

import me.owdding.ktcodecs.GenerateCodec
import me.owdding.ktmodules.Module
import me.owdding.misc.utils.generated.CodecUtils
import me.owdding.misc.utils.generated.MiscUtilsCodecs
import me.owdding.misc.utils.utils.asAdventureComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.network.chat.ClickEvent
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.InventoryTitle
import tech.thatgravyboat.skyblockapi.api.events.misc.RegisterCommandsEvent
import tech.thatgravyboat.skyblockapi.api.events.screen.InventoryChangeEvent
import tech.thatgravyboat.skyblockapi.impl.tagkey.ItemModelTag
import tech.thatgravyboat.skyblockapi.utils.extentions.getLore
import tech.thatgravyboat.skyblockapi.utils.json.Json.toJson
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.Text.send
import tech.thatgravyboat.skyblockapi.utils.text.TextStyle.style

@Module
object HotfParser {

    private val perks: MutableSet<HotfPerk> = mutableSetOf()

    @GenerateCodec
    data class HotfPerk(
        val name: String,
        val tooltip: List<String>,
    )

    @Subscription
    @InventoryTitle("Heart of the Forest")
    private fun InventoryChangeEvent.onChange() {
        if (item !in ItemModelTag.HOTF_PERK_ITEMS) return

        perks += HotfPerk(
            MiniMessage.miniMessage().serialize(item.hoverName.asAdventureComponent()),
            item.getLore().map { MiniMessage.miniMessage().serialize(it.asAdventureComponent()) }
        )
    }

    @Subscription
    private fun RegisterCommandsEvent.register() {
        registerWithCallback("meowdding save") {
            Text.of("Click to copy") {
                this.style {
                    withClickEvent(
                        ClickEvent.CopyToClipboard(
                            perks.toJson(
                                CodecUtils.mutableSet(
                                    MiscUtilsCodecs.HotfPerkCodec.codec()
                                )
                            ).toString()
                        )
                    )
                }
            }.send()
        }
    }

}