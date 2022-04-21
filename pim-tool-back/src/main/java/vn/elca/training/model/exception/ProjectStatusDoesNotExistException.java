package vn.elca.training.model.exception;

public class ProjectStatusDoesNotExistException extends Exception {
    private final String status;

    public ProjectStatusDoesNotExistException(String status) {
        super(String.format("Project status %s does not exist. Please select another project status", status));
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
