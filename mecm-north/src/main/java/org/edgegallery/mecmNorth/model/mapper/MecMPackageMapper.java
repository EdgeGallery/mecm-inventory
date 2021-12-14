package org.edgegallery.mecmNorth.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.edgegallery.mecmNorth.model.MecMPackageInfo;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface MecMPackageMapper {

    void insertMecmPkgInfo(MecMPackageInfo mecMPackageInfo);

    void deletePkgInfoByPkgId(String mecmPkgId);

    void updateMecmPkgInfo(MecMPackageInfo mecMPackageInfo);

    MecMPackageInfo getMecMPkgInfoByPkgId(String mecmPkgId);

}
