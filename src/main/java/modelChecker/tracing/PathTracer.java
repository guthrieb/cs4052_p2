package modelChecker.tracing;

import formula.pathFormula.Always;
import formula.pathFormula.Next;
import formula.pathFormula.PathFormula;
import formula.pathFormula.Until;
import formula.stateFormula.Or;
import formula.stateFormula.StateFormula;
import formula.stateFormula.ThereExists;
import model.Model;
import model.State;
import modelChecker.graphbuilding.PathTree;

import java.util.*;

public class PathTracer {


    public static List<State> generatePath(Model model, State initialState, Set<State> stateSet, Set<State> failedStates, StateFormula formula, PathTree tree, HashMap<StateFormula, PathTree> pathData) {

        if (formula instanceof ThereExists) {

            ThereExists thereExists = (ThereExists) formula;

            return handleThereExists(model, thereExists, stateSet, initialState, pathData);
        } else if (formula instanceof Or) {
            Or or = (Or) formula;
            StateFormula right = or.right;
            StateFormula left = or.left;

            System.out.println(left);
            System.out.println(right);
            System.out.println("Left accepting: " + pathData.get(left).getAcceptingStates());
            System.out.println("Right accepting: " + pathData.get(right).getAcceptingStates());

            if (pathData.get(left).getAcceptingStates().contains(initialState)) {
                return generatePath(model, initialState, stateSet, failedStates, left, tree, pathData);
            } else {
                return generatePath(model, initialState, stateSet, failedStates, right, tree, pathData);
            }
        }

        return new ArrayList<>();
    }

    private static List<State> handleThereExists(Model model, ThereExists thereExists, Set<State> stateSet, State initialState, HashMap<StateFormula, PathTree> pathData) {
        PathFormula pathFormula = thereExists.pathFormula;


        if (pathFormula instanceof Next) {
            List<State> states = handleNext(model, thereExists, stateSet, (Next) pathFormula, initialState, pathData);
            return states;
        } else if (pathFormula instanceof Always) {
            return handleAlways(model, thereExists, stateSet, (Always) pathFormula, initialState, pathData);
        } else if (pathFormula instanceof Until) {
            return handleUntil(model, thereExists, stateSet, (Until) pathFormula, initialState, pathData);
        }

        return new ArrayList<>();
    }

    private static List<State> handleUntil(Model model, ThereExists thereExists, Set<State> stateSet, Until pathFormula, State initialState, HashMap<StateFormula, PathTree> pathData) {

        PathTree pathTree = pathData.get(thereExists);
        Set<State> allAcceptingStatesOnUntil = pathTree.getAcceptingStates();
        StateFormula right = pathFormula.right;
        Set<State> rightAcceptingStates = pathData.get(right).getAcceptingStates();


        if (rightAcceptingStates.contains(initialState)) {
            return new ArrayList<>(Collections.singletonList(initialState));
        }
        depthFirstSearch(model, rightAcceptingStates, initialState, allAcceptingStatesOnUntil);

        return null;
    }

    private static List<State> handleNext(Model model, ThereExists parentFormula, Set<State> stateSet, Next pathFormula,
                                          State initialState, HashMap<StateFormula, PathTree> pathData) {
        Set<State> acceptingSubformulaOfNext = pathData.get(pathFormula.stateFormula).getAcceptingStates();

        Set<State> initialNeighbours = Model.getNextStates(model, stateSet, initialState);

        List<State> path = new ArrayList<>();
        for (State state : initialNeighbours) {
            if (acceptingSubformulaOfNext.contains(state)) {
                path.add(initialState);
                path.add(state);
                return path;
            }
        }
        return path;
    }

    private static List<State> handleAlways(Model model, ThereExists parentFormula, Set<State> stateSet, Always pathFormula,
                                            State initialState, HashMap<StateFormula, PathTree> pathData) {
        Set<State> acceptingSubformulaOfNext = pathData.get(parentFormula).getAcceptingStates();


        return recurseAlways(model, stateSet, initialState, new ArrayList<State>(Collections.singletonList(initialState)), acceptingSubformulaOfNext);
    }

    private static List<State> recurseAlways(Model model, Set<State> stateSet, State currentState, List<State> previousStates, Set<State> acceptingStates) {
        Set<State> nextStates = Model.getNextStates(model, stateSet, currentState);


        for (State neighbour : nextStates) {
            if (acceptingStates.contains(neighbour)) {
                if (previousStates.contains(neighbour)) {
                    previousStates.add(neighbour);
                    return previousStates;
                } else {
                    previousStates.add(neighbour);
                    return recurseAlways(model, stateSet, neighbour, previousStates, acceptingStates);
                }
            }
        }
        return null;
    }


    /**
     * @param acceptingStates     set of states that contain second half of until, accepting states of search
     * @param initialState        initial state
     * @param untilAcceptingState set of all states, i.e. search domain
     * @return path from initial state to state in accepting states
     */
    private static List<State> depthFirstSearch(Model model, Set<State> acceptingStates, State initialState, Set<State> untilAcceptingState) {
        List<State> currentSearch = new ArrayList<>();

        Set<State> visited = new HashSet<>();
        visited.add(initialState);

        Path path = recurseDFS(new Path(), model, initialState, visited, acceptingStates, untilAcceptingState);

        return path.getPath();
    }

    private static Path recurseDFS(Path currentPath, Model model, State initialState, Set<State> visited, Set<State> acceptingStates, Set<State> untilAcceptingState) {
        currentPath.add(initialState);
        Set<State> nextStates = Model.getNextStates(model, untilAcceptingState, initialState);

        System.out.println(initialState);
        System.out.println("Next States: " + nextStates);

        for (State state : nextStates) {
            if (acceptingStates.contains(state)) {
                currentPath.add(state);
                currentPath.setFinished(true);
                return currentPath;
            }
        }

        for (State state : nextStates) {
            if (!visited.contains(state)) {
                visited.add(state);
                currentPath = recurseDFS(currentPath, model, state, visited, acceptingStates, untilAcceptingState);
                if (currentPath.isFinished()) {
                    return currentPath;
                }
            }
        }

        currentPath.remove();
        return currentPath;
    }

}
