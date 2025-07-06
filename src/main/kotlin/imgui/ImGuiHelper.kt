package me.owdding.misc.utils.imgui

import com.mojang.blaze3d.opengl.GlDevice
import com.mojang.blaze3d.opengl.GlStateManager
import com.mojang.blaze3d.opengl.GlTexture
import com.mojang.blaze3d.systems.RenderSystem
import imgui.ImGui
import imgui.extension.imnodes.ImNodes
import imgui.extension.implot.ImPlot
import imgui.flag.ImGuiConfigFlags
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

object ImGuiHelper {

    val glfw = ImGuiImplGlfw()
    val gl3 = ImGuiImplGl3()

    fun init(handle: Long) {
        ImGui.createContext()
        ImPlot.createContext()
        ImNodes.createContext()

        val data = ImGui.getIO()
        data.iniFilename = "meow.ini"
        data.fontGlobalScale = 1F

        data.configFlags = ImGuiConfigFlags.DockingEnable

        glfw.init(handle, true)
        gl3.init()
    }

    fun draw(runnable: ImGuiRender) {
        val framebuffer = Minecraft.getInstance().mainRenderTarget
        val fbo = (framebuffer.colorTexture as? GlTexture)?.getFbo(
            (RenderSystem.getDevice() as GlDevice).directStateAccess(),
            null
        ) ?: return
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo)
        GL11.glViewport(0, 0, framebuffer.viewWidth, framebuffer.viewHeight)

        gl3.newFrame()
        glfw.newFrame()
        ImGui.newFrame()

        runnable.render(ImGui.getIO())

        ImGui.render()
        gl3.renderDrawData(ImGui.getDrawData())

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo)
    }

    fun dispose() {
        glfw.shutdown()
        gl3.shutdown()

        ImPlot.destroyContext()
        ImNodes.destroyContext()
        ImGui.destroyContext()
    }

}