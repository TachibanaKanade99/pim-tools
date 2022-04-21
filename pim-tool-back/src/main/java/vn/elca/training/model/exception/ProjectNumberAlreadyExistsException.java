package vn.elca.training.model.exception;

public class ProjectNumberAlreadyExistsException extends Exception {
    private final Integer currentProjectNumber;
    
    public ProjectNumberAlreadyExistsException(Integer currentProjectNumber) {
        super(String.format("This project number %s is already existed. Please select a different project number", currentProjectNumber));
        this.currentProjectNumber = currentProjectNumber;
    }

    public Integer getCurrentProjectNumber() {
        return currentProjectNumber;
    }
}
