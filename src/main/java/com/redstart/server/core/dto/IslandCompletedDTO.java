package com.redstart.server.core.dto;

import com.redstart.server.database.entity.IslandCompletedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IslandCompletedDTO {
    private String island;

    public static IslandCompletedDTO of(IslandCompletedEntity entity) {
        IslandCompletedDTO dto = new IslandCompletedDTO();
        dto.setIsland(entity.getIsland());
        return dto;
    }
}
