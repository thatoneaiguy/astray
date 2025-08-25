package com.thatoneaiguy.archipelago.cca;

import com.thatoneaiguy.archipelago.init.ArchipelagoComponents;
import com.thatoneaiguy.archipelago.util.skills.Skill;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.HashSet;
import java.util.Set;

public class PlayerSkillsComponent implements AutoSyncedComponent {
    private PlayerEntity player;

    private int dvolti = 0;
    private final Set<Skill> unlocked = new HashSet<>();
    private final Set<Skill> active = new HashSet<>();

    // ? ACTIVE SKILLS
    // ? INACTIVE SKILLS
    // ? AMOUNT OF DVOLTI
    // ? UNLOCKED SKILLS

    public PlayerSkillsComponent(PlayerEntity player) {
        this.player = player;
    }

    public int getDvolti() {
        return dvolti;
    }

    public void setDvolti(int dvolti) {
        this.dvolti = dvolti;
    }

    public void addDvolti(int dvolti) {
        this.dvolti += dvolti;
    }

    public void removeDvolti(int dvolti) {
        this.dvolti -= dvolti;
    }

    public boolean canSpendDvolti(int amount) {
        return dvolti >= amount;
    }

    public void unlockSkill(Skill skill) {
        unlocked.add(skill);
    }

    public void activateSkill(Skill skill) {
        if (unlocked.contains(skill) && !active.contains(skill)) active.add(skill);
    }

    public void deactiveateSkill(Skill skill) {
        if (unlocked.contains(skill) && active.contains(skill)) active.remove(skill);
    }

    public Set<Skill> getUnlockedSkills() {
        return unlocked;
    }

    public Set<Skill> getActiveSkills() {
        return active;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        dvolti = tag.getInt("Dvolti");

        unlocked.clear();
        NbtList unlockedList = tag.getList("Unlocked", NbtElement.STRING_TYPE);
        for (int i = 0; i < unlockedList.size(); i++) {
            unlocked.add(Skill.valueOf(unlockedList.getString(i)));
        }

        active.clear();
        NbtList activeList = tag.getList("Active", NbtElement.STRING_TYPE);
        for (int i = 0; i < activeList.size(); i++) {
            active.add(Skill.valueOf(activeList.getString(i)));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("Dvolti", dvolti);

        NbtList unlockedList = new NbtList();
        for (Skill id : unlocked) {
            unlockedList.add(NbtString.of(id.name()));
        }
        tag.put("Unlocked", unlockedList);

        NbtList activeList = new NbtList();
        for (Skill id : active) {
            activeList.add(NbtString.of(id.name()));
        }
        tag.put("Active", activeList);
    }


    public void sync() {
        ArchipelagoComponents.PLAYER_SKILLS_COMPONENT.sync(this.player);
    }
}
