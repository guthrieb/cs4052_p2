package formula.stateFormula;

import formula.pathFormula.PathFormula;
import formula.pathFormula.PathFormulaHandler;
import model.Model;
import model.State;
import modelChecker.graphbuilding.PathTree;
import modelChecker.tracing.InvalidStateFormula;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateFormulaHandler {
    public static Set<State> getStates(Model model, Set<State> statesToCheck, StateFormula formula, PathTree pathTree, Map<StateFormula, PathTree> pathData) throws InvalidStateFormula {
        PathTree nextPathTree = new PathTree(formula, pathTree.depth + 1);

        Set<State> acceptingStates;
        if (formula instanceof AtomicProp) {
            acceptingStates = getAtomicPropStates(statesToCheck, (AtomicProp) formula);
        } else if (formula instanceof BoolProp) {
            acceptingStates = getBoolPropStates(statesToCheck, (BoolProp) formula);
        } else if (formula instanceof Not) {
            acceptingStates = getNotStates(model, statesToCheck, (Not) formula, nextPathTree, pathData);
        } else if (formula instanceof Or) {
            acceptingStates = getOrStates(model, statesToCheck, (Or) formula, nextPathTree, pathData);
        } else if (formula instanceof And) {
            acceptingStates = getAndStates(model, statesToCheck, (And) formula, nextPathTree, pathData);
        } else if (formula instanceof ThereExists) {
            acceptingStates = getThereExistsStates(model, statesToCheck, (ThereExists) formula, nextPathTree, pathData);
        } else {
            throw new InvalidStateFormula("Property must be in ENF form");
        }

        nextPathTree.setAcceptingStates(new HashSet<>(acceptingStates));
        pathTree.children.add(nextPathTree);
        pathData.put(formula, nextPathTree);

        return acceptingStates;
    }

    private static Set<State> getAndStates(Model model, Set<State> statesToCheck, And formula, PathTree pathTree, Map<StateFormula, PathTree> pathData) throws InvalidStateFormula {
        Set<State> leftAcceptingStates = getStates(model, statesToCheck, formula.left, pathTree, pathData);
        Set<State> rightAcceptingStates = getStates(model, statesToCheck, formula.right, pathTree, pathData);

        leftAcceptingStates.retainAll(rightAcceptingStates);

        return leftAcceptingStates;
    }

    private static Set<State> getOrStates(Model model, Set<State> stateSet, Or formula, PathTree pathTree, Map<StateFormula, PathTree> pathData) throws InvalidStateFormula {
        Set<State> leftAcceptingStates = getStates(model, stateSet, formula.left, pathTree, pathData);
        Set<State> rightAcceptingStates = getStates(model, stateSet, formula.right, pathTree, pathData);
        leftAcceptingStates.addAll(rightAcceptingStates);

        return leftAcceptingStates;
    }

    private static Set<State> getNotStates(Model model, Set<State> stateSet, Not formula, PathTree pathTree, Map<StateFormula, PathTree> pathData) throws InvalidStateFormula {
        Set<State> acceptingStates = new HashSet<>(stateSet);
        Set<State> subformulaStates = getStates(model, stateSet, formula.stateFormula, pathTree, pathData);

        acceptingStates.removeAll(subformulaStates);
        return acceptingStates;
    }

    private static Set<State> getBoolPropStates(Set<State> stateSet, BoolProp formula) {
        Set<State> acceptingStates = new HashSet<>();
        if (formula.value) {
            acceptingStates.addAll(stateSet);
        }


        return acceptingStates;
    }

    private static Set<State> getAtomicPropStates(Set<State> stateSet, AtomicProp prop) {

        Set<State> acceptingStates = new HashSet<>();
        for (State state : stateSet) {
            for (String label : state.getLabels()) {
                if (prop.label.equals(label)) {
                    acceptingStates.add(state);
                }
            }
        }

//        System.out.println("\nEvaluating Atomic Prop: " + prop);
//        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
    }

    private static Set<State> getThereExistsStates(Model model, Set<State> stateSet, ThereExists exists, PathTree pathTree, Map<StateFormula, PathTree> pathData) throws InvalidStateFormula {
        PathFormula pathFormula = exists.pathFormula;

        return PathFormulaHandler.getStates(model, stateSet, pathFormula, pathTree, pathData);
    }


}
