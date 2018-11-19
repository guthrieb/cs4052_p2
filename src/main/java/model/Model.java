package model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A model is consist of states and transitions
 */
public class Model {
    private State[] states;
    private Transition[] transitions;

    public static Model parseModel(String filePath) throws IOException {
        Gson gson = new Gson();
        Model model = gson.fromJson(new FileReader(filePath), Model.class);
        for (Transition t : model.transitions) {
            System.out.println(t);
        }
        return model;
    }

    /**
     * Returns the list of the states
     * 
     * @return list of state for the given model
     */
    public State[] getStates() {
        return states;
    }

    /**
     * Returns the list of transitions
     * 
     * @return list of transition for the given model
     */
    public Transition[] getTransitions() {
        return transitions;
    }

    public State getState(String stateName) throws InvalidStateException {
        for(State state : states) {
            if(state.getName().equals(stateName)) {
                return state;
            }
        }

        throw new InvalidStateException("State not found: " + stateName);
    }

    public boolean stateReachableViaActions(State source, State target, Set<String> actions) {

        Transition transition = getTransition(source.getName(), target.getName());

        if (transition == null) {
            return false;
        }

        if (actions.size() == 0) {
            return true;
        }

        HashSet<String> thisActions = new HashSet<>(Arrays.asList(transition.getActions()));

        return actions.containsAll(thisActions);
    }

    private Transition getTransition(String source, String target) {
        for (Transition transition : transitions) {
            if (source.equals(transition.getSource()) && target.equals(transition.getTarget())) {
                return transition;
            }
        }
        return null;
    }
}
