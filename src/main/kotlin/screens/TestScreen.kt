package me.owdding.misc.utils.screens

import imgui.ImGui
import imgui.ImGuiIO
import imgui.extension.implot.ImPlot
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import me.owdding.misc.utils.imgui.ImScreen

class TestScreen : ImScreen() {
    val meow = ImBoolean(false)

    override fun draw(io: ImGuiIO) {
        if (ImGui.begin("meow", ImGuiWindowFlags.None)) {
            ImGui.setWindowSize(800f, 600f)
            meow.set(ImGui.checkbox("test", meow))
        }
        ImGui.end()

        ImPlot.showDemoWindow(meow)
        ImGui.showDemoWindow(meow)
    }

}