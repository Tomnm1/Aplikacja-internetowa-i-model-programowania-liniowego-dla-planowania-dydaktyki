package pl.poznan.put.planner_endpoints.Specialisation;

import pl.poznan.put.constans.Constants;

public enum Cycle implements Constants.EnumUtils.DisplayName {
    first("1st"),
    second("2st");

    private final String displayName;

    Cycle(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}