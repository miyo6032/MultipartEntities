package io.github.stuff_stuffs.multipart_entities.mixin.common;

import io.github.stuff_stuffs.multipart_entities.common.entity.MultipartAwareEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class MixinProjectileEntity extends Entity {

    public MixinProjectileEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onCollision", at = @At("HEAD"))
    private void onCollision(HitResult hitResult, CallbackInfo ci) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY && hitResult instanceof EntityHitResult) {
            Entity target = ((EntityHitResult) hitResult).getEntity();
            if (target instanceof MultipartAwareEntity) {
                MultipartAwareEntity multipartAwareEntity = (MultipartAwareEntity) target;
                String nextDamagedPart = multipartAwareEntity.getBounds().raycast(getPos(), getPos().add(getVelocity()));
                multipartAwareEntity.setNextDamagedPart(nextDamagedPart);
            }
        }
    }
}
