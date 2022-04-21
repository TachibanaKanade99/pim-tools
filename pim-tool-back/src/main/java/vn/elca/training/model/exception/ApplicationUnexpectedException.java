package vn.elca.training.model.exception;

public class ApplicationUnexpectedException extends RuntimeException {
    public ApplicationUnexpectedException(Throwable e) {
        super(String.format("Unexpected error occur %s. Please contact your administrator", e.getMessage()), e);
    }
}
