package io.github.stuff_stuffs.multipart_entities.common;

import io.github.stuff_stuffs.multipart_entities.common.entity.TestEntity;
import io.github.stuff_stuffs.multipart_entities.common.network.PlayerInteractMultipartReceiver;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MultipartEntities implements ModInitializer {
    public static final EntityType<TestEntity> TEST_ENTITY_TYPE = FabricEntityTypeBuilder.createLiving().defaultAttributes(LivingEntity::createLivingAttributes).entityFactory(TestEntity::new).dimensions(EntityDimensions.fixed(2, 2)).build();

    @Override
    public void onInitialize() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier("multipart_entities", "test_entity"), TEST_ENTITY_TYPE);
        PlayerInteractMultipartReceiver.init();
    }
}
