package vn.elca.training.model.dto;

import java.util.List;

public class ProjectPageDto {
    private List<ProjectDto> projects;
    private int totalPages;

    public List<ProjectDto> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDto> projects) {
        this.projects = projects;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
