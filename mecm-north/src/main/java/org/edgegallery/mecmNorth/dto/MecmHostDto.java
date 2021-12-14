package org.edgegallery.mecmNorth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class MecmHostDto {
    private String mechostIp;

    private String mechostName;

    private String mechostCity;

    /**
     * convert from data map.
     *
     * @param mecHostInfoMap mec host data map
     * @return dto object
     */
    public static MecmHostDto fromMap(Map<String, Object> mecHostInfoMap) {
        return new MecmHostDto((String) mecHostInfoMap.get("mechostIp"),
                (String) mecHostInfoMap.get("mechostName"), (String) mecHostInfoMap.get("city"));
    }
}
