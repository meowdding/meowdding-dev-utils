package me.owdding.misc.utils.mixins;

import com.mojang.blaze3d.platform.Window;
import me.owdding.misc.utils.imgui.ImGuiHelper;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Final private Window window;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        ImGuiHelper.INSTANCE.init(window.getWindow());
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void shutdown(CallbackInfo ci) {
        ImGuiHelper.INSTANCE.dispose();
    }

}
