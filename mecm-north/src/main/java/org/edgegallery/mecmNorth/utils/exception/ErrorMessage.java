package org.edgegallery.mecmNorth.utils.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorMessage {

    private int retCode;

    private List<String> params;

    /**
     * construct.
     *
     */
    public ErrorMessage(int retCode, List<String> params) {
        this.retCode = retCode;
        this.params = params;
    }
}
