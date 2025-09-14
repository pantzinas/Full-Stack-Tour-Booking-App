package gr.aueb.cf.tourapp.core.exceptions;

public class AppServerException extends AppGenericException {

    public AppServerException(String code, String message) {
        super(code, message);
    }
}
