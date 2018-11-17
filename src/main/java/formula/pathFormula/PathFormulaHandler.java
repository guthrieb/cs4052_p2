package formula.pathFormula;

import formula.stateFormula.StateFormulaHandler;
import model.InvalidStateException;
import model.Model;
import model.State;
import model.Transition;
import modelChecker.tracing.InvalidStateFormula;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PathFormulaHandler {
    public static Set<State> getStates(Model model, PathFormula formula) throws InvalidStateFormula {
        if (formula instanceof Always) {
            return getAcceptingAlwaysStates(model, (Always) formula);
        } else if (formula instanceof Next) {
            return getAcceptingNextStates(model, (Next) formula);
        } else if (formula instanceof Until) {
            return getAcceptingUntilStates(model, (Until) formula);
        } else {
            throw new InvalidStateFormula("Formulas should be of ENF form");
        }
    }

    private static Set<State> getAcceptingAlwaysStates(Model model, Always formula) throws InvalidStateFormula {
        Set<State> acceptingStates = StateFormulaHandler.getStates(model, formula.stateFormula);

        boolean cycle = true;

        while(cycle) {
            cycle = false;
            Iterator<State> stateIterator = acceptingStates.iterator();
            while (stateIterator.hasNext()) {
                State state = stateIterator.next();
                Set<State> nextStates = getNextStates(model, state);

                for (State nextState : nextStates) {
                    //If next state is not an accepting state reachable via specified transitions
                    if (!acceptingStates.contains(nextState)) {
                        stateIterator.remove();
                        cycle = true;
                        break;
                    } else if (!model.stateReachableViaActions(state, nextState, formula.getActions())) {
                        stateIterator.remove();
                        cycle = true;
                        break;
                    }
                }
            }
        }

        System.out.println("\nAlways Evaluated: " + formula);
        System.out.println("Accepting States: " + acceptingStates);
        return acceptingStates;
    }

    private static Set<State> getAcceptingUntilStates(Model model, Until formula) throws InvalidStateFormula {
        //Conditions of right side met, regardless of transitions
        Set<State> rightAcceptingStates = StateFormulaHandler.getStates(model, formula.right);

        //Conditions of left side met, regardless of transitions
        Set<State> leftAcceptingStates = StateFormulaHandler.getStates(model, formula.left);
        Set<State> finalRightAcceptingStates = new HashSet<>();

        System.out.println("UNTIL");
        System.out.println("Right accepting: " + rightAcceptingStates);
        System.out.println("Left accepting: " + leftAcceptingStates);

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
                Set<State> nextStates = getNextStates(model, state);

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

    private static Set<State> getAcceptingNextStates(Model model, Next formula) throws InvalidStateFormula {

        Set<State> subformulaAcceptingStates = StateFormulaHandler.getStates(model, formula.stateFormula);


        Set<State> finalAcceptingStates = new HashSet<>();
        for (State state : model.getStates()) {
            Set<State> nextStates = getNextStates(model, state);

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

    private static Set<State> getNextStates(Model model, State state) {
        Set<State> next = new HashSet<>();

        Transition[] transitions = model.getTransitions();

        for (Transition transition : transitions) {
            try {
                State source = model.getState(transition.getSource());
                if(source.equals(state)) {
                    State targetState = model.getState(transition.getTarget());
                    next.add(targetState);
                }
            } catch (InvalidStateException e) {
                e.printStackTrace();
            }
        }

        return next;
    }

    private static Set<State> getNextStates(Model model, State state, Set<String> actions) {
        Set<State> next = new HashSet<>();

        Transition[] transitions = model.getTransitions();


        for (Transition transition : transitions) {
            try {
                if(state.equals(model.getState(transition.getSource()))) {
                    if (actions.size() == 0 || actions.containsAll(Arrays.asList(transition.getActions()))) {
                        State targetState = model.getState(transition.getTarget());
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
