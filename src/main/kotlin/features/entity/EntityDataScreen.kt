package me.owdding.devutils.features.entity

import imgui.ImGuiIO
import me.owdding.devutils.imgui.ImKotlin
import me.owdding.devutils.imgui.ImScreen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity

class EntityDataScreen(val entities: List<Entity>) : ImScreen() {
    override fun draw(io: ImGuiIO) {
        if (entities.size == 1) {

        }
    }
}

interface BaseEntityVisualizer<T : Entity> {

    fun visualize(entity: T): ImKotlin.() -> Unit = {
        ImText("Entity Type: ${BuiltInRegistries.ENTITY_TYPE.getKey(entity.type)}")
    }

}