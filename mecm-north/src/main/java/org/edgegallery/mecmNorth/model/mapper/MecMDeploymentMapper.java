package org.edgegallery.mecmNorth.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.edgegallery.mecmNorth.model.MecMPackageDeploymentInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MecMDeploymentMapper {

    void insertPkgDeploymentInfo(MecMPackageDeploymentInfo mecMPackageInfo);

    void deletePkgDeploymentInfoByPkgId(String mecmPkgId);

    void updateMecmPkgDeploymentInfo(MecMPackageDeploymentInfo mecMPackageInfo);

    MecMPackageDeploymentInfo getMecMPkgDeploymentInfoById(String id);

    List<String> getMecMPkgDeploymentInfoByPkgId(String mecmPkgId);

}
