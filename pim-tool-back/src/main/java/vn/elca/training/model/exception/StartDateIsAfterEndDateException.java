package vn.elca.training.model.exception;

import java.time.LocalDate;

public class StartDateIsAfterEndDateException extends Exception {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public StartDateIsAfterEndDateException(LocalDate startDate, LocalDate endDate) {
        super(String.format("The start date %s is after end date %s", startDate.toString(), endDate.toString()));
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
