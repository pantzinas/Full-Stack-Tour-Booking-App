package gr.aueb.cf.tourapp.core.exceptions;

public class AppObjectInvalidArgumentException extends AppGenericException {
    private final static String DEFAULT_CODE = "InvalidArgument";

    public AppObjectInvalidArgumentException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }

}
