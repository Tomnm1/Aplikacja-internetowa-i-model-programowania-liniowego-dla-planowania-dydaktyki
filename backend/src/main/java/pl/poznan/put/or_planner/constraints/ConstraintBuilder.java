package pl.poznan.put.or_planner.constraints;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class ConstraintBuilder {
    private final MPConstraint constraint;

    public ConstraintBuilder(MPSolver solver, String name, double lowerBound, double upperBound) {
        this.constraint = solver.makeConstraint(lowerBound, upperBound, name);
    }

    public ConstraintBuilder(MPSolver solver, String name, double upperBound) {
        this.constraint = solver.makeConstraint(name);
        this.constraint.setUb(upperBound);
    }

    public void setCoefficient(MPVariable variable, double coefficient) {
        this.constraint.setCoefficient(variable, coefficient);
    }
}
