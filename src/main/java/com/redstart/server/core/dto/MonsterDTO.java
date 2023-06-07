package com.redstart.server.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redstart.server.database.entity.MonsterEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonsterDTO {
    private String name;
    private int damage;
    private int hp;
    @JsonProperty("isBoss")
    private boolean isBoss;
    private int speed;

    public static MonsterDTO of(MonsterEntity monsterEntity) {
        MonsterDTO monsterDTO = new MonsterDTO();
        monsterDTO.setName(monsterEntity.getName());
        monsterDTO.setDamage(monsterEntity.getDamage());
        monsterDTO.setHp(monsterEntity.getHp());
        monsterDTO.setBoss(monsterEntity.isBoss());
        monsterDTO.setSpeed(monsterEntity.getSpeed());
        return monsterDTO;
    }
}
