package pl.poznan.put.planner_endpoints.planner.error_message;

public class ErrorMessage {
    private ErrorType errorType;
    private String message;

    public ErrorMessage(ErrorType errorType, String message){
        this.errorType = errorType;
        this.message = message;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getMessage() {
        return message;
    }
}

