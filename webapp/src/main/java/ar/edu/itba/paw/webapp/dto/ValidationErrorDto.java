package ar.edu.itba.paw.webapp.dto;



import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;

public class ValidationErrorDto {

    private String message;
    private String path;

    public static ValidationErrorDto fromValidationException(final ConstraintViolation<?> violation) {
        final ValidationErrorDto dto = new ValidationErrorDto();
        dto.message = violation.getMessage();
        PathImpl pathImpl = (PathImpl) violation.getPropertyPath();
        dto.path = pathImpl.getLeafNode().toString();
        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

