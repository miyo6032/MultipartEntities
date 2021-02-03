package io.github.stuff_stuffs.multipart_entities.common.entity;

import io.github.stuff_stuffs.multipart_entities.common.util.CompoundOrientedBox;
import io.github.stuff_stuffs.multipart_entities.common.util.QuaternionD;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collections;

public class TestEntity extends LivingEntity implements MultipartAwareEntity {
    private static final EntityBounds.Factory ENTITY_BOUNDS_FACTORY;
    private final EntityBounds bounds;
    private final EntityPart body;
    private final EntityPart leftArm;
    private final EntityPart leftHand;
    private String nextAttackedPart;

    public TestEntity(final EntityType<? extends LivingEntity> entityType, final World world) {
        super(entityType, world);
        bounds = ENTITY_BOUNDS_FACTORY.create();
        body = bounds.getPart("body");
        leftArm = bounds.getPart("left_arm");
        leftHand = bounds.getPart("left_hand");
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getEquippedStack(final EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(final EquipmentSlot slot, final ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public void setBoundingBox(final Box boundingBox) {
        super.setBoundingBox(boundingBox);
    }

    @Override
    public void tick() {
        super.tick();
        body.setX(getX());
        body.setY(getY() + 0.5);
        body.setZ(getZ());
        //body.setRotation(QuaternionD.IDENTITY);
        body.rotate(new QuaternionD(new Vec3d(0, 1, 0), Math.PI / (4 * 20), false));
        leftArm.rotate(new QuaternionD(new Vec3d(1, 0, 0), Math.PI / (4 * 20), false));
    }

    @Override
    public CompoundOrientedBox getBoundingBox() {
        return bounds.getBox(super.getBoundingBox());
    }

    @Override
    public EntityBounds getBounds() {
        return bounds;
    }

    @Override
    public void setNextDamagedPart(final String part) {
        nextAttackedPart = part;
    }

    @Override
    public boolean handleAttack(final Entity attacker) {
        if (nextAttackedPart != null && attacker instanceof PlayerEntity) {
            ((PlayerEntity) attacker).sendMessage(new LiteralText(nextAttackedPart), true);
            return super.handleAttack(attacker);
        }
        return true;
    }

    @Override
    public ActionResult interact(final Entity entity, final Hand hand, final String part) {
        if (entity instanceof PlayerEntity) {
            ((PlayerEntity) entity).sendMessage(new LiteralText(part), true);
        }
        return ActionResult.SUCCESS;
    }

    static {
        ENTITY_BOUNDS_FACTORY = EntityBounds.builder().
                add("body").setBounds(1, 1, 1).build().
                add("left_arm").setParent("body").setBounds(0.125, 1, 0.125).setPos(0.5, 0.5, 0).setPivot(0.5, 0.5, 0).build().
                add("left_hand").setParent("left_arm").setBounds(0.125, 0.125, 0.125).setPos(0, 0.5, 0).build().
                getFactory();
    }
}
