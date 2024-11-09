package pl.poznan.put.planner_endpoints.Specialisation;

public enum Cycle {
    first("1st"),
    second("2st");

    private final String displayName;

    Cycle(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString(){
        return displayName;
    }
}
