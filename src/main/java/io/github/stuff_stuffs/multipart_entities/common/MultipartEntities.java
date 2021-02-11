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
    public static final EntityType<TestEntity> TEST_ENTITY_TYPE = FabricEntityTypeBuilder.createLiving().dimensions(EntityDimensions.fixed(1,1)).entityFactory(TestEntity::new).defaultAttributes(LivingEntity::createLivingAttributes).build();

    @Override
    public void onInitialize() {
        PlayerInteractMultipartReceiver.init();
        Registry.register(Registry.ENTITY_TYPE, new Identifier("test", "test"), TEST_ENTITY_TYPE);
    }
}
