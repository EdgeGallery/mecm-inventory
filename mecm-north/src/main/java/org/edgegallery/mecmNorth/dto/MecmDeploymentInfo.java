package org.edgegallery.mecmNorth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MecmDeploymentInfo {
    private String mecmAppInstanceId;

    private String mecmOperationalStatus;

    private String mecmAppId;

    private String mecmAppPackageId;

}