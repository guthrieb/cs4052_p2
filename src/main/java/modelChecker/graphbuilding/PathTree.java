package modelChecker.graphbuilding;

import formula.pathFormula.*;
import formula.stateFormula.ForAll;
import formula.stateFormula.StateFormula;
import formula.stateFormula.ThereExists;
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

        string.append("\n").append(depthString).append("**** EVALUATING ").append(depth).append(" ").append(formula).append(" -- ");
        if (formula instanceof ThereExists) {
            PathFormula pathFormula = ((ThereExists) formula).pathFormula;
            if (pathFormula instanceof Next) {
                string.append(((Next) pathFormula).getActions());
            } else if (pathFormula instanceof Always) {
                string.append(((Always) pathFormula).getActions());
            } else if (pathFormula instanceof Until) {
                string.append("Left actions: ").append(((Until) pathFormula).getLeftActions());
                string.append("Right actions: ").append(((Until) pathFormula).getRightActions());
            } else if (pathFormula instanceof Eventually) {
                string.append("Left actions: ").append(((Eventually) pathFormula).getLeftActions());
                string.append("Right actions: ").append(((Eventually) pathFormula).getLeftActions());
            }
        } else if (formula instanceof ForAll) {
            PathFormula pathFormula = ((ForAll) formula).pathFormula;
            if (pathFormula instanceof Next) {
                string.append(((Next) pathFormula).getActions());
            } else if (pathFormula instanceof Always) {
                string.append(((Always) pathFormula).getActions());
            } else if (pathFormula instanceof Until) {
                string.append("Left actions: ").append(((Until) pathFormula).getLeftActions());
                string.append("Right actions: ").append(((Until) pathFormula).getRightActions());
            } else if (pathFormula instanceof Eventually) {
                string.append("Left actions: ").append(((Eventually) pathFormula).getLeftActions());
                string.append("Right actions: ").append(((Eventually) pathFormula).getLeftActions());
            }
        }
        string.append(":").append(acceptingStates);
        for (PathTree child : children) {

            string.append(child);
        }


        string.append("\n");
        return string.toString();
    }

    public Set<State> getAcceptingStates() {
        return acceptingStates;
    }
}
