package org.edgegallery.mecmNorth.domain;

public class ResponseConst {

    /**
     * the success code.
     */
    public static final int RET_SUCCESS = 0;

    /**
     * the fail code.
     */
    public static final int RET_FAIL = 1;


    /**
     * app param is invalid.
     */
    public static final int RET_PARAM_INVALID = 10001;

    /**
     * get mec host info failed.
     */
    public static final int RET_GET_MECMHOST_FAILED = 16007;

    /**
     * Permission not allowed to delete application package.
     */
    public static final int RET_NO_ACCESS_DELETE_PACKAGE = 18002;

    /**
     * the content of manifest file is incorrect.
     */
    public static final int RET_MF_CONTENT_INVALID = 10029;

    /**
     * An exception occurred while getting the file from application package.
     */
    public static final int RET_PARSE_FILE_EXCEPTION = 10016;

    /**
     * sign package failed.
     */
    public static final int RET_SIGN_PACKAGE_FAILED = 10030;
}
