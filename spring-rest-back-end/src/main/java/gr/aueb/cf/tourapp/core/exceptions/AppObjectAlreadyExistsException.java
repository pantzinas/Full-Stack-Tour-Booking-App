package gr.aueb.cf.tourapp.core.exceptions;

public class AppObjectAlreadyExistsException extends AppGenericException {
    private static final String DEFAULT_CODE = "AlreadyExists";

    public AppObjectAlreadyExistsException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
