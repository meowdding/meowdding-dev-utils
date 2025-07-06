package me.owdding.devutils.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.Window;
import me.owdding.devutils.imgui.ImGuiHelper;
import me.owdding.devutils.utils.PopupScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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

    @WrapOperation(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;removed()V"))
    public void meow(Screen instance, Operation<Void> original) {
        if (instance instanceof PopupScreen) {
            return;
        }
        original.call(instance);
    }
}
