package formula.pathFormula;

import formula.stateFormula.StateFormulaHandler;
import model.InvalidStateException;
import model.Model;
import model.State;
import model.Transition;
import modelChecker.graphbuilding.PathTree;
import modelChecker.tracing.InvalidStateFormula;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PathFormulaHandler {
    public static Set<State> getStates(Model model, Set<State> stateSet, PathFormula formula, PathTree pathTree) throws InvalidStateFormula {
        if (formula instanceof Always) {
            return getAcceptingAlwaysStates(model, stateSet, (Always) formula, pathTree);
        } else if (formula instanceof Next) {
            return getAcceptingNextStates(model, stateSet, (Next) formula, pathTree);
        } else if (formula instanceof Until) {
            return getAcceptingUntilStates(model, stateSet, (Until) formula, pathTree);
        } else {
            throw new InvalidStateFormula("Formulas should be of ENF form");
        }
    }

    private static Set<State> getAcceptingAlwaysStates(Model model, Set<State> stateSet, Always formula, PathTree pathTree) throws InvalidStateFormula {
        Set<State> acceptingStates = new HashSet<>(StateFormulaHandler.getStates(model, stateSet, formula.stateFormula, pathTree));

        boolean cycle = true;

        while(cycle) {
            cycle = false;
            Iterator<State> stateIterator = acceptingStates.iterator();
            while (stateIterator.hasNext()) {
                State state = stateIterator.next();
                Set<State> nextStates = getNextStates(model, stateSet, state);

                boolean maintain = false;
                for (State nextState : nextStates) {
                    //If next state is not an accepting state reachable via specified transitions
                    if (acceptingStates.contains(nextState)) {
                        if (model.stateReachableViaActions(state, nextState, formula.getActions())) {
                            maintain = true;
                            break;
                        }
                    }


//                    if (!acceptingStates.contains(nextState)) {
//                        System.out.println("REMOVING: " + state);
//                        stateIterator.remove();
//                        cycle = true;
//                        break;
//                    } else if (!model.stateReachableViaActions(state, nextState, formula.getActions())) {
//                        stateIterator.remove();
//                        cycle = true;
//                        break;
//                    }
                }

                if (!maintain) {
                    stateIterator.remove();
                    cycle = true;
                }
            }
        }

        System.out.println("\nAlways Evaluated: " + formula);
        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
    }

    private static Set<State> getAcceptingUntilStates(Model model, Set<State> stateSet, Until formula, PathTree pathTree) throws InvalidStateFormula {
        //Conditions of right side met, regardless of transitions
        Set<State> rightAcceptingStates = StateFormulaHandler.getStates(model, stateSet, formula.right, pathTree);

        //Conditions of left side met, regardless of transitions
        Set<State> leftAcceptingStates = StateFormulaHandler.getStates(model, stateSet, formula.left, pathTree);
        Set<State> finalRightAcceptingStates = new HashSet<>();

        //Add all right states that share a connection to the left states via right actions
        for(State state : rightAcceptingStates) {
            Transition[] transitions = model.getTransitions();
            for(Transition transition : transitions) {
                try {

                    String sourceState1 = transition.getSource();
                    State targetState = model.getState(transition.getTarget());
                    State sourceState = model.getState(sourceState1);
                    if(targetState.equals(state) && leftAcceptingStates.contains(sourceState)) {
                        if (model.stateReachableViaActions(sourceState, targetState, formula.getRightActions())) {
                            finalRightAcceptingStates.add(targetState);
                        }
                    }
                } catch (InvalidStateException e) {
                    e.printStackTrace();
                }
            }
        }

        Set<State> finalLeftAcceptingStates = new HashSet<>();


        boolean cycle = true;
        while(cycle) {
            cycle = false;
            for(State state : leftAcceptingStates) {
                Set<State> nextStates = getNextStates(model, stateSet, state);

                for(State nextState : nextStates) {
                    //If reachable via right actions and in right accepting states
                    if (finalRightAcceptingStates.contains(nextState)
                            && model.stateReachableViaActions(state, nextState, formula.getRightActions())
                            && !finalLeftAcceptingStates.contains(state)) {
                        finalLeftAcceptingStates.add(state);
                        cycle = true;
                    }

                    //If reachable via left actions and in left accepting states
                    if (finalLeftAcceptingStates.contains(nextState)
                            && model.stateReachableViaActions(state, nextState, formula.getLeftActions())
                            && !finalLeftAcceptingStates.contains(state)) {

                        finalLeftAcceptingStates.add(state);
                        cycle = true;
                    }
                }
            }
        }

        Set<State> finalAcceptingStates = new HashSet<>();
        finalAcceptingStates.addAll(finalLeftAcceptingStates);
        finalAcceptingStates.addAll(finalRightAcceptingStates);


        System.out.println("\nUntil Evaluated: " + formula);
        System.out.println("Accepting States: " + finalAcceptingStates);

        return finalAcceptingStates;
    }

    private static Set<State> getAcceptingNextStates(Model model, Set<State> stateSet, Next formula, PathTree pathTree) throws InvalidStateFormula {

        Set<State> subformulaAcceptingStates = StateFormulaHandler.getStates(model, stateSet, formula.stateFormula, pathTree);


        Set<State> finalAcceptingStates = new HashSet<>();
        for (State state : stateSet) {
            Set<State> nextStates = getNextStates(model, stateSet, state);

            for (State nextState : nextStates) {
                if (subformulaAcceptingStates.contains(nextState)) {
                    if (model.stateReachableViaActions(state, nextState, formula.getActions())) {
                        finalAcceptingStates.add(state);
                        break;
                    }
                }
            }
        }

        System.out.println("\nNext Evaluated: " + formula);
        System.out.println("Accepting States: " + finalAcceptingStates);

        return finalAcceptingStates;
    }

    private static Set<State> getNextStates(Model model, Set<State> stateSet, State state) {
        Set<State> next = new HashSet<>();

        Transition[] transitions = model.getTransitions();

        for (Transition transition : transitions) {
            try {
                State source = model.getState(transition.getSource());
                if(source.equals(state)) {
                    State targetState = model.getState(transition.getTarget());
                    if (stateSet.contains(targetState)) {
                        next.add(targetState);
                    }
                }
            } catch (InvalidStateException e) {
                e.printStackTrace();
            }
        }

        return next;
    }
}
