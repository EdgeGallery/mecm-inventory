package org.edgegallery.mecmNorth.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
public class MecMPackageDeploymentInfo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "mecm_package_id")
    private String mecmPackageId;

    @Column(name = "host_ip")
    private String hostIp;

    @Column(name = "status")
    private String status;
}
