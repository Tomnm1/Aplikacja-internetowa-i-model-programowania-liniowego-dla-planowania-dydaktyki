package pl.poznan.put.planner_endpoints.planner.error_message;

import java.util.List;

public class PlanningValidationException extends RuntimeException {
    private final List<ErrorMessage> errorMessages;

    public PlanningValidationException(String message, List<ErrorMessage> errorMessages) {
        super(message);
        this.errorMessages = errorMessages;
    }

    public List<ErrorMessage> getErrorMessages() {
        return errorMessages;
    }
}
