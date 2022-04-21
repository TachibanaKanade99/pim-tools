package vn.elca.training.model.exception;

import java.util.List;

public class ProjectNotFoundException extends Exception {
    private final List<Long> notFoundIds;
    public ProjectNotFoundException(List<Long> notFoundIds) {
        super("project not found - could be because of deleting before");
        this.notFoundIds = notFoundIds;
    }

    public List<Long> getNotFoundIds() {
        return notFoundIds;
    }
}
