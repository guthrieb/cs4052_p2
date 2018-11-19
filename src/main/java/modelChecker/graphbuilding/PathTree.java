package modelChecker.graphbuilding;

import formula.stateFormula.StateFormula;
import model.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PathTree {
    public StateFormula formula;
    public int depth = 0;
    public List<PathTree> children = new ArrayList<>();
    private Set<State> acceptingStates;

    public PathTree(StateFormula enfQuery, int depth) {
        this.formula = enfQuery;
        this.depth = depth;
    }

    public void setAcceptingStates(Set<State> acceptingStates) {
        System.out.println("Adding accepting states " + acceptingStates + " to " + formula);
        this.acceptingStates = acceptingStates;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        StringBuilder depthString = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            depthString.append("    ");
        }

        depthString.append("|");

        string.append("\n").append(depthString).append("******** EVALUATING " + depth + " ").append(formula).append(" -- ");
        string.append(":").append(acceptingStates);
        for (PathTree child : children) {

            string.append(child);
        }


        string.append("\n");
        return string.toString();
    }

}
