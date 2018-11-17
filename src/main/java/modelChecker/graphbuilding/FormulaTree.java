package modelChecker.graphbuilding;

import formula.pathFormula.*;
import formula.stateFormula.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FormulaTree {
    private static final String LABEL_OPEN = " [label = \"";
    private static final String LABEL_CLOSE = "\"]";
    private List<FormulaTree> stateChildren = new ArrayList<>();
    private StateFormula stateFormula;
    private PathFormula pathFormula;

    private boolean usesStateFormula;


    public FormulaTree(PathFormula pathFormula) {
        this.pathFormula = pathFormula;
        usesStateFormula = false;

        if(pathFormula instanceof Always) {
            Always always = (Always) pathFormula;

            stateChildren.add(new FormulaTree(always.stateFormula));
        } else if (pathFormula instanceof Eventually) {
            Eventually eventually = (Eventually) pathFormula;

            stateChildren.add(new FormulaTree(eventually.stateFormula));
        } else if (pathFormula instanceof Next) {
            Next next = (Next) pathFormula;

            stateChildren.add(new FormulaTree(next.stateFormula));
        } else if (pathFormula instanceof Until) {
            Until until = (Until) pathFormula;

            stateChildren.add(new FormulaTree(until.left));
            stateChildren.add(new FormulaTree(until.right));
        }
    }

    FormulaTree(StateFormula stateFormula) {
        usesStateFormula = true;
        this.stateFormula = stateFormula;
        if (stateFormula instanceof Not) {
            Not not = (Not) stateFormula;

            stateChildren.add(new FormulaTree(not.stateFormula));
        } else if (stateFormula instanceof And) {
            And and = (And) stateFormula;

            stateChildren.add(new FormulaTree(and.left));
            stateChildren.add(new FormulaTree(and.right));
        } else if (stateFormula instanceof AtomicProp || stateFormula instanceof BoolProp) {

        } else if (stateFormula instanceof Or) {
            Or or = (Or) stateFormula;

            stateChildren.add(new FormulaTree(or.left));
            stateChildren.add(new FormulaTree(or.right));
        } else if (stateFormula instanceof ThereExists) {
            ThereExists thereExists = (ThereExists) stateFormula;

            stateChildren.add(new FormulaTree(thereExists.pathFormula));
        } else if (stateFormula instanceof ForAll) {
            ForAll forAll = (ForAll) stateFormula;

            stateChildren.add(new FormulaTree(forAll.pathFormula));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();


        if(usesStateFormula) {
            if(stateFormula instanceof And) {
                builder.append("AND");
            } else if (stateFormula instanceof AtomicProp) {
                builder.append(((AtomicProp) stateFormula).label);
            } else if (stateFormula instanceof BoolProp) {
                builder.append(((BoolProp) stateFormula).value);
            } else if (stateFormula instanceof ForAll) {
                builder.append("ForAll");
            } else if (stateFormula instanceof Not) {
                builder.append("Not");
            } else if (stateFormula instanceof Or) {
                builder.append("Or");
            } else if (stateFormula instanceof ThereExists) {
                builder.append("ThereExists");
            }
        } else {
            if(pathFormula instanceof Always) {
                builder.append("Always");
            } else if (pathFormula instanceof Eventually) {
                builder.append("Eventually");
            } else if (pathFormula instanceof Next) {
                builder.append("Next");
            } else if (pathFormula instanceof Until) {
                builder.append("Until");
            }
        }


        return builder.toString();
    }

    public String inDotFormat(boolean isFirst, int index) {


        StringBuilder builder = new StringBuilder();
        if(isFirst) {
            builder.append("\ndigraph G1 {");
        }

        int i = 0;
        for(FormulaTree formula : stateChildren) {
            builder.append("\n\t").append(toString()).append("_").append(hashCode()).append("->").append(formula.toString()).append("_").append(formula.hashCode());
            if (!this.usesStateFormula) {
                if (pathFormula instanceof Always) {
                    builder.append(LABEL_OPEN).append(((Always) pathFormula).getActions());
                    builder.append(LABEL_CLOSE);

                } else if (pathFormula instanceof Next) {
                    builder.append(LABEL_OPEN).append(((Next) pathFormula).getActions());
                    builder.append(LABEL_CLOSE);

                } else if (pathFormula instanceof Until) {
                    Set<String> leftActions = ((Until) pathFormula).getLeftActions();
                    Set<String> rightActions = ((Until) pathFormula).getRightActions();
                    builder.append(LABEL_OPEN).append((i == 0) ? leftActions : rightActions);
                    i++;
                    builder.append(LABEL_CLOSE);

                } else if (pathFormula instanceof Eventually) {
                    Set<String> leftActions = ((Eventually) pathFormula).getLeftActions();
                    Set<String> rightActions = ((Eventually) pathFormula).getRightActions();
                    builder.append(LABEL_OPEN).append((i == 0) ? leftActions : rightActions);
                    i++;
                    builder.append(LABEL_CLOSE);
                }
            }
        }

        for(FormulaTree formulaTree : stateChildren) {
            index++;
            builder.append(formulaTree.inDotFormat(false, index));
        }

        if(isFirst) {
            builder.append("\n}");
        }
        return builder.toString();
    }

    String inDotFormat() {
        return inDotFormat(true, 0);
    }
}
