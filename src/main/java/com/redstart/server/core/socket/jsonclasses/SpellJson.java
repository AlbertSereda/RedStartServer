package com.redstart.server.core.socket.jsonclasses;

import com.redstart.server.core.gamemechanics.spells.interfaces.ISpell;
import com.redstart.server.core.gamemechanics.spells.interfaces.WithTimeSpell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpellJson {
    private String name;
    private int costMana;
    private int damage;
    private long durationTime;
    private long timeToEnd;


    public static SpellJson spellJsonConverter(ISpell spell) {
        SpellJson spellJson = new SpellJson();
        spellJson.setName(spell.getNameSpell());
        spellJson.setCostMana(spell.getCost());
        spellJson.setDamage(spell.getDamage());

        if (spell instanceof WithTimeSpell withTimeSpell) {
            spellJson.setDurationTime(withTimeSpell.getDurationTime());
            spellJson.setTimeToEnd(withTimeSpell.getTimeToEnd());
        } else {
            spellJson.setDurationTime(0);
            spellJson.setTimeToEnd(0L);
        }
        return spellJson;
    }
}
