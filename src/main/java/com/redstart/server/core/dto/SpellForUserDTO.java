package com.redstart.server.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redstart.server.core.gamemechanics.spells.ArrowSpell;
import com.redstart.server.core.gamemechanics.spells.FireBallSpell;
import com.redstart.server.core.gamemechanics.spells.NextDamageProtectionSpell;
import com.redstart.server.core.gamemechanics.spells.StanSpell;
import com.redstart.server.core.gamemechanics.spells.interfaces.ISpell;
import com.redstart.server.database.entity.SpellForUserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpellForUserDTO {
    @JsonIgnore
    private int id;
    private String name;
    private int costMana;
    private int damage;
    private int durationTime;
    private int level;

    public static SpellForUserDTO of(SpellForUserEntity spellForUserEntity) {
        SpellForUserDTO spellForUser = new SpellForUserDTO();
        spellForUser.setId(spellForUserEntity.getId());
        spellForUser.setName(spellForUserEntity.getName());
        spellForUser.setCostMana(spellForUserEntity.getCostMana());
        spellForUser.setDamage(spellForUserEntity.getDamage());
        spellForUser.setDurationTime(spellForUserEntity.getDurationTime());
        spellForUser.setLevel(spellForUserEntity.getLevel());
        return spellForUser;
    }

    public static List<ISpell> convert(List<SpellForUserDTO> selectedSpellsDTO) {
        return selectedSpellsDTO
                .stream()
                .map(spellForUserDTO -> {
                    switch (spellForUserDTO.getName()) {
                        case "arrow":
                            return new ArrowSpell(spellForUserDTO.getCostMana(), spellForUserDTO.getDamage());
                        case "fireBall":
                            return new FireBallSpell(spellForUserDTO.getCostMana(), spellForUserDTO.getDamage());
                        case "nextDamageProtection":
                            return new NextDamageProtectionSpell(spellForUserDTO.getCostMana(), spellForUserDTO.getDamage());
                        case "stan":
                            return new StanSpell(spellForUserDTO.costMana, spellForUserDTO.durationTime, spellForUserDTO.getDamage());
                    }
                    throw new IllegalStateException("Unknown spell");
                })
                .collect(Collectors.toList());
    }
}
