package org.edgegallery.mecmNorth.utils.exception;

import java.util.ArrayList;
import java.util.List;

public class PermissionNotAllowedException extends DomainException {

    private static final long serialVersionUID = 6824743617068936088L;

    private ErrorMessage errMsg;

    public <T> PermissionNotAllowedException(String message) {
        super("Permission not allowed: " + message);
    }

    /**
     * Constructor to create PermissionNotAllowedException with retCode and params.
     *
     * @param ret retCode
     * @param args params of error message
     */
    public PermissionNotAllowedException(String msg, int ret, Object... args) {
        super("Permission not allowed: " + msg);
        List<String> params = new ArrayList<>();
        int length = args == null ? 0 : args.length;
        for (int i = 0; i < length; i++) {
            params.add(args[i].toString());
        }
        ErrorMessage errorMessage = new ErrorMessage(ret, params);
        errMsg = errorMessage;
    }

    /**
     * get error message.
     *
     */
    public ErrorMessage getErrMsg() {
        return errMsg;
    }
}
