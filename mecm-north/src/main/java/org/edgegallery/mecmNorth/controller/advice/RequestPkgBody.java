package org.edgegallery.mecmNorth.controller.advice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Getter
@Setter
@Builder
public class RequestPkgBody {

    String appPkgName;

    String appPkgVersion;

    MultipartFile file;

    String[] hostList;

    Map<String, String> paramsMap;

    String tenantId;
}
