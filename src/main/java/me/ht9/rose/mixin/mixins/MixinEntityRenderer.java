package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.FOVModifierEvent;
import me.ht9.rose.event.events.RenderWorldEvent;
import me.ht9.rose.event.events.RenderWorldPassEvent;
import me.ht9.rose.feature.module.modules.render.cameratweaks.CameraTweaks;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRenderer.class)
public class MixinEntityRenderer
{
    @Shadow private float field_22227_s;

    @Shadow private float field_22228_r;

    @Inject(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/EntityRenderer;func_4135_b(FI)V",
                    shift = At.Shift.AFTER
            )
    )
    public void renderWorld(float partialTicks, long idk, CallbackInfo ci)
    {
        RenderWorldPassEvent event = new RenderWorldPassEvent(partialTicks);
        Rose.bus().post(event);
    }

    @Inject(
            method = "setupFog",
            at = @At(
                    value = "TAIL"
            )
    )
    public void setupFog(int startCoords, float partialTicks, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.FOG);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            GL11.glFogi(2916, 9729);
        }
    }

    @Inject(
            method = "hurtCameraEffect",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void hurtCameraEffect(float partialTicks, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.HURTCAM);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }

    @Inject(
            method = "addRainParticles",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void addRainParticles(CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.RAIN);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }

    @Inject(
            method = "renderRainSnow",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void renderRainSnow(float partialTicks, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.RAIN);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }

    @Inject(
            method = "setupViewBobbing",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void setupViewBobbing(float partialTicks, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.VIEWBOB);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }

    @Inject(
            method = "getFOVModifier",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    public void getFOVModifier(float fov, CallbackInfoReturnable<Float> cir)
    {
        FOVModifierEvent event = new FOVModifierEvent();
        Rose.bus().post(event);
        if (event.fov() > -1)
        {
            cir.setReturnValue(event.fov());
            cir.cancel();
        }
    }

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;rayTraceBlocks(Lnet/minecraft/src/Vec3D;Lnet/minecraft/src/Vec3D;)Lnet/minecraft/src/MovingObjectPosition;"))
    public MovingObjectPosition orientCamera$fixClip(World instance, Vec3D vec3D1, Vec3D vec3D2)
    {
        if (CameraTweaks.instance().enabled() && CameraTweaks.instance().clip.value()) return null;
        return instance.rayTraceBlocks(vec3D1, vec3D2);
    }
}
