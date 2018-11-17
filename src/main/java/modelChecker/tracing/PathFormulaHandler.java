package modelChecker.tracing;

import formula.pathFormula.Always;
import formula.pathFormula.Next;
import formula.pathFormula.PathFormula;
import formula.pathFormula.Until;
import model.InvalidStateException;
import model.Model;
import model.State;
import model.Transition;

import java.util.*;

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
                    if (!acceptingStates.contains(nextState)) {
                        stateIterator.remove();
                        cycle = true;
                        break;
                    }
                }
            }
        }

        return acceptingStates;
    }

    private static Set<State> getAcceptingUntilStates(Model model, Until formula) throws InvalidStateFormula {
        //Conditions of right side met, regardless of transitions
        Set<State> rightAcceptingStates = StateFormulaHandler.getStates(model, formula.right);

        //Conditions of left side met, regardless of transitions
        Set<State> leftAcceptingStates = StateFormulaHandler.getStates(model, formula.left);

        Set<State> totalAcceptingStates = new HashSet<>(rightAcceptingStates);

        //Add all right states that share a connection to the left states
        //TODO include specific action considerations
        for(State state : rightAcceptingStates) {
            Transition[] transitions = model.getTransitions();
            for(Transition transition : transitions) {
                try {

                    String sourceState1 = transition.getSource();
                    State targetState = model.getState(transition.getTarget());
                    State sourceState = model.getState(sourceState1);
                    if(targetState.equals(state) && leftAcceptingStates.contains(sourceState)) {
                        totalAcceptingStates.add(targetState);
                    }
                } catch (InvalidStateException e) {
                    e.printStackTrace();
                }
            }
        }


        boolean cycle = true;
        while(cycle) {
            cycle = false;
            for(State state : leftAcceptingStates) {
                Set<State> nextStates = getNextStates(model, state, formula.getLeftActions());
                for(State nextState : nextStates) {
                    if(totalAcceptingStates.contains(nextState)) {
                        totalAcceptingStates.add(state);
                        cycle = true;
                    }
                }
            }
        }

        return totalAcceptingStates;
    }

    private static Set<State> getAcceptingNextStates(Model model, Next formula) throws InvalidStateFormula {
        Set<State> acceptingStates = StateFormulaHandler.getStates(model, formula.stateFormula);
        Set<State> nextStates = getNextStates(model, formula.getActions());

        acceptingStates.retainAll(nextStates);
        return acceptingStates;
    }

    private static Set<State> getNextStates(Model model, Set<String> actions) {
        Set<State> next = new HashSet<>();

        Transition[] transitions = model.getTransitions();

        for (Transition transition : transitions) {
            try {
                if(actions.containsAll(Arrays.asList(transition.getActions()))) {
                    State targetState = model.getState(transition.getTarget());
                    next.add(targetState);
                }
            } catch (InvalidStateException e) {
                e.printStackTrace();
            }
        }

        return next;
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
                    if (actions.containsAll(Arrays.asList(transition.getActions()))) {
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
