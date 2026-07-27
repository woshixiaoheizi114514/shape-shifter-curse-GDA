package net.onixary.shapeShifterCurseFabric.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import net.onixary.shapeShifterCurseFabric.additional_power.AlwaysSprintSwimmingPower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerEntity.class)
public class PlayerEntitySprintSwimmingMixin {
    @Unique
    private int argIndex = 0;

    @ModifyArgs(method = "addExhaustion(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;addExhaustion(F)V"))
    private void modifySwimmingHungerConsumption(Args args) {
        Object arg = args.get(argIndex);
        if (!(arg instanceof Float)) {
            argIndex = 1;
            arg = args.get(1);
        }
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.isSwimming()) {
            float finalArg = (float) arg;
            args.set(argIndex, PowerHolderComponent.getPowers(player, AlwaysSprintSwimmingPower.class).stream()
                    .map(power -> finalArg * power.getHungerMultiplier())
                    .findFirst()
                    .orElse(finalArg));
        }
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), index = 0)
    private Vec3d modifyVerticalVelocity(Vec3d originalVelocity) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (PowerHolderComponent.hasPower(player, AlwaysSprintSwimmingPower.class)){
            // 只有在冲刺时才修改Y轴速度
            if (player.isSprinting()) {
                return originalVelocity;
            } else {
                // 保持原有的X和Z速度，但不修改Y速度
                Vec3d currentVelocity = player.getVelocity();
                return new Vec3d(originalVelocity.x, currentVelocity.y, originalVelocity.z);
            }
        }
        return originalVelocity;
    }
}
