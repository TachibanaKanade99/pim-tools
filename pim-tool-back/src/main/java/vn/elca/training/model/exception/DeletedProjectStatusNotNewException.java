package vn.elca.training.model.exception;

public class DeletedProjectStatusNotNewException extends Exception {
    private final String projectNames;

    public DeletedProjectStatusNotNewException(String projectNames) {
        super(String.format("projects: %s - status not new", projectNames));
        this.projectNames = projectNames;
    }

    public String getProjectNames() {
        return projectNames;
    }
}
