package pl.poznan.put.planner_endpoints.planner.error_message;

public enum ErrorType {
    CRITICAL, // Nie pozwalają na rozpoczęcie planowania
    IMPORTANT, // Błędy, które mogą spowodować niewygenerowanie rozwiązania, ale nie blokują rozpoczęcia planowania
}
