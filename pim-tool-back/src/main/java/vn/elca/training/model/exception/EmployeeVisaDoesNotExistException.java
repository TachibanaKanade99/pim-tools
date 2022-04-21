package vn.elca.training.model.exception;

public class EmployeeVisaDoesNotExistException extends Exception {
    private final String invalidVisas;

    public EmployeeVisaDoesNotExistException(String invalidVisas) {
        super(String.format("The following visas do not exist: { %s }", invalidVisas));
        this.invalidVisas = invalidVisas;
    }

    public String getInvalidVisas() {
        return invalidVisas;
    }
}
