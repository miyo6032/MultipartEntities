package io.github.stuff_stuffs.multipart_entities.client;

import io.github.stuff_stuffs.multipart_entities.common.MultipartEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MultipartEntitiesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(MultipartEntities.TEST_ENTITY_TYPE, (entityRenderDispatcher, context) -> new EntityRenderer<Entity>(entityRenderDispatcher) {
            @Override
            public Identifier getTexture(final Entity entity) {
                return null;
            }
        });
    }
}
