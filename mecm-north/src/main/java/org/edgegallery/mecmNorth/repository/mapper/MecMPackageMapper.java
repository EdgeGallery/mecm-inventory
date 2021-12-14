package org.edgegallery.mecmNorth.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.edgegallery.mecmNorth.repository.MecMPackageInfo;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface MecMPackageMapper {

    void insertMecmPkgInfo(MecMPackageInfo mecMPackageInfo);

    void deletePkgInfoByPkgId(String mecmPkgId);

    void updateMecmPkgInfo(MecMPackageInfo mecMPackageInfo);

    MecMPackageInfo getMecMPkgInfoByPkgId(String mecmPkgId);

}