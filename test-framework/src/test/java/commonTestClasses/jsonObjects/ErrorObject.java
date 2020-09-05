package commonTestClasses.jsonObjects;

import lombok.Getter;

@Getter
public class ErrorObject {
    private long timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;
    private String path;
}
