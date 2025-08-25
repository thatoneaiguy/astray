package com.thatoneaiguy.archipelago.init;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.cca.PlayerSkillsComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public class ArchipelagoComponents implements EntityComponentInitializer {
    public static final ComponentKey<PlayerSkillsComponent> PLAYER_SKILLS_COMPONENT =
            ComponentRegistry.getOrCreate(Archipelago.id("player_skills"), PlayerSkillsComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PLAYER_SKILLS_COMPONENT, PlayerSkillsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
