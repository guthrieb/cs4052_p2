package formula.pathFormula;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;

import java.util.Set;

public class Until extends PathFormula {
    public final StateFormula left;
    public final StateFormula right;
    private Set<String> leftActions;
    private Set<String> rightActions;

    public Until(StateFormula left, StateFormula right, Set<String> leftActions, Set<String> rightActions) {
        super();
        this.left = left;
        this.right = right;
        this.leftActions = leftActions;
        this.rightActions = rightActions;
    }

    public Set<String> getLeftActions() {
        return leftActions;
    }

    public Set<String> getRightActions() {
        return rightActions;
    }

    @Override
    public void writeToBuffer(StringBuilder buffer) {
        buffer.append("(");
        left.writeToBuffer(buffer);
        buffer.append(leftActions);
        buffer.append(" " + FormulaParser.UNTIL_TOKEN + " ");
        buffer.append(rightActions);
        right.writeToBuffer(buffer);
        buffer.append(")");
    }

}
