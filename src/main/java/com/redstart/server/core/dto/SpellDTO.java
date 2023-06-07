package com.redstart.server.core.dto;

import com.redstart.server.database.entity.SpellDefaultEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpellDTO { //todo подумать над наследованием от availableSpell
    private String name;
    private int costMana;
    private int damage;
    private int durationTime;

    public static SpellDTO of(SpellDefaultEntity entity) {
        SpellDTO dto = new SpellDTO();
        dto.setName(entity.getName());
        dto.setCostMana(entity.getCostMana());
        dto.setDamage(entity.getDamage());
        dto.setDurationTime(entity.getDurationTime());
        return dto;
    }
}
