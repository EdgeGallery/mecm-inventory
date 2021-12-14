package org.edgegallery.mecmNorth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
@Builder
public class MecMPackageDeploymentInfo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "mecm_package_id")
    private String mecmPackageId;

    @Column(name = "mecm_pkg_name")
    private String mecmPkgName;

    @Column(name = "app_id_from_apm")
    private String appIdFromApm;

    @Column(name = "app_pkg_id_from_apm")
    private String appPkgIdFromApm;

    @Column(name = "app_instance_id")
    private String appInstanceId;

    @Column(name = "host_ip")
    private String hostIp;

    @Column(name = "status_code")
    private int statusCode;

    @Column(name = "status")
    private String status;


}
