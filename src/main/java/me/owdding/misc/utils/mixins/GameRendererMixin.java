package me.owdding.misc.utils.mixins;

import me.owdding.misc.utils.imgui.ImGuiHelper;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void render(CallbackInfo ci) {
        //ImGuiHelper.INSTANCE.update();
    }

}
