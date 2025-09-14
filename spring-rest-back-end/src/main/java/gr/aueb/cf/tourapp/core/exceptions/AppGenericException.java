package gr.aueb.cf.tourapp.core.exceptions;

import lombok.Getter;

@Getter
public class AppGenericException extends RuntimeException {
    private final String code;

    public AppGenericException(String code, String message) {
        super(message);
        this.code = code;
    }
}
