package modelChecker.tracing;

import formula.pathFormula.*;
import formula.stateFormula.*;
import model.Model;
import model.Transition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EnfConverter {
    private Model model;
    private Set<String> allActions;

    public EnfConverter(Model model) {
        this.model = model;
        Set<String> allActions = new HashSet<>();
        for (Transition transition : model.getTransitions()) {
            String[] actions = transition.getActions();
            allActions.addAll(Arrays.asList(actions));
        }

        this.allActions = allActions;
    }


    public StateFormula convertToEnf(StateFormula formula) {
        StateFormula returnVal = formula;
        if (formula instanceof AtomicProp) {
        } else if (formula instanceof BoolProp) {
            if (!((BoolProp) formula).value) {
                returnVal = new Not(new BoolProp(true));
            }
        } else if (formula instanceof Not) {
            returnVal = new Not(convertToEnf(((Not) formula).stateFormula));

//            return convertToEnf(((Not) formula).stateFormula);
        } else if (formula instanceof Or) {
            Or or = (Or) formula;

            StateFormula orLeft = convertToEnf(or.left);
            StateFormula orRight = convertToEnf(or.right);


            returnVal = new Or(orLeft, orRight);
//            return new Or(orLeft, orRight);
        } else if (formula instanceof And) {
            And and = (And) formula;

            StateFormula andLeft = convertToEnf(and.left);
            StateFormula andRight = convertToEnf(and.right);

//            return new And(andLeft, andRight);
            returnVal = new And(andLeft, andRight);
        } else if (formula instanceof ThereExists) {
//            return handleIfExists(formula);
            returnVal = handleIfExists(formula);
        } else if (formula instanceof ForAll) {
//            return handleForAll(formula);
            returnVal = handleForAll(formula);
        }

        return returnVal;
    }

    private StateFormula handleIfExists(StateFormula formula) {
        ThereExists thereExists = (ThereExists) formula;

        PathFormula pathFormula = thereExists.pathFormula;

        if (pathFormula instanceof Eventually) {
            Eventually eventually = (Eventually) pathFormula;

            Set<String> convertedLeftActions = replaceActions(eventually.getLeftActions());
            Set<String> convertedRightActions = replaceActions(eventually.getRightActions());

            return new ThereExists(new Until(new BoolProp(true), convertToEnf(eventually.stateFormula), convertedLeftActions, convertedRightActions));
        } else if (pathFormula instanceof Next) {
            Next next = (Next) pathFormula;

            Set<String> convertedActions = replaceActions(next.getActions());

            return new ThereExists(new Next(convertToEnf(next.stateFormula), convertedActions));
        } else if (pathFormula instanceof Always) {
            Always always = (Always) pathFormula;

            Set<String> actions = replaceActions(always.getActions());
            return new ThereExists(new Always(convertToEnf(always.stateFormula), actions));
        } else if (pathFormula instanceof Until) {
            Until until = (Until) pathFormula;

            Set<String> leftActions = replaceActions(until.getLeftActions());
            Set<String> rightActions = replaceActions(until.getRightActions());
            return new ThereExists(new Until(convertToEnf(until.left), convertToEnf(until.right), leftActions, rightActions));
        }

        return null;
    }

    private StateFormula handleForAll(StateFormula formula) {
        ForAll forAll = (ForAll) formula;

        PathFormula pathFormula = forAll.pathFormula;

        if (pathFormula instanceof Next) {
            //∀()Φ ≡ ¬∃()¬Φ AND ¬ EX(true)
            Next next = (Next) pathFormula;

//            Not left = new Not(new ThereExists(new Next(new Not(convertToEnf(next.stateFormula)), new HashSet<String>())));
            ThereExists left = new ThereExists(new Next(new Not(convertToEnf(next.stateFormula)), allActions));

            Set<String> nextActions = replaceActions(next.getActions());
            if (nextActions.size() > 0) {
                HashSet<String> addActions;
                addActions = getSubtractionOfActions(nextActions);
//                Not right = new Not(new ThereExists(new Next(new BoolProp(true), addActions)));
                ThereExists right = new ThereExists(new Next(new BoolProp(true), addActions));
                return new Not(new Or(left, right));
            } else {
                return new Not(left);
            }
//            return new Not(new ThereExists(new Next(new Not(convertToEnf(next.stateFormula)), next.getActions())));
        } else if (pathFormula instanceof Always) {
            //∀Φ ≡ ¬∃♦¬Φ = ¬∃(true U ¬Φ)
            Always always = (Always) pathFormula;
            Set<String> actions = replaceActions(always.getActions());
            Until until = new Until(new BoolProp(true), new Not(convertToEnf(always.stateFormula)), allActions, actions);
            ThereExists thereExists = new ThereExists(until);

            ThereExists left = new ThereExists(new Until(
                    new BoolProp(true), new BoolProp(true), allActions, getSubtractionOfActions(actions)));

//            Not right = new Not(new ThereExists(new Until(
            ThereExists right = new ThereExists(new Until(
                    new BoolProp(true), new Not(convertToEnf(always.stateFormula)), allActions, allActions
            ));


            if (actions.size() == 0) {
                return new Not(right);
//                return right;
            } else {
//                return new And(left, right);
                return new Not(new Or(left, right));
            }

//            return new Not(thereExists);
        } else if (pathFormula instanceof Eventually) {
            //∀♦Φ ≡ ¬∃¬Φ
            Eventually eventually = (Eventually) pathFormula;

            Until untilConversion = new Until(new BoolProp(true), convertToEnf(eventually.stateFormula), eventually.getLeftActions(), eventually.getRightActions());
            System.out.println(untilConversion);

            ForAll forAllUntilConversion = new ForAll(untilConversion);
            return convertToEnf(forAllUntilConversion);
        } else if (pathFormula instanceof Until) {
            //∀(Φ1 U Φ2) ≡ ¬∃(¬Φ2 U (¬Φ1 ∧ ¬Φ2)) ∧ ¬∃¬Φ2
            Until originalUntil = (Until) pathFormula;
            StateFormula leftFormula = convertToEnf(originalUntil.left);
            StateFormula rightFormula = convertToEnf(originalUntil.right);


            // STATE CONTENTS
            Not notLeft = new Not(leftFormula);
            Not notRight = new Not(rightFormula);

            ThereExists rightThereExists = new ThereExists(new Always(notRight, allActions));
//            Not rightThereExists = new Not(new ThereExists(new Always(notRight, originalUntil.getRightActions())));

            And nestedAnd = new And(notLeft, notRight);
//            Not leftThereExists = new Not(new ThereExists(new Until(notRight, nestedAnd, originalUntil.getLeftActions(), originalUntil.getRightActions())));
            ThereExists leftThereExists = new ThereExists(new Until(notRight, nestedAnd, allActions, allActions));


//            Not formula2 = new Not(new ThereExists(new Until(new BoolProp(true), new BoolProp(true), notYActions, notXAndYActions)));
//            Not thereExists2 = new Not(new ThereExists(new Always(new BoolProp(true), notYActions)));


//            StateFormula formula2;
//            if(originalUntil.getLeftActions().size() > 0) {
//                formula2 = new Not(new ThereExists(new Until(new And(leftFormula, new Not(rightFormula)), rightFormula, new HashSet<String>(), getSubtractionOfActions(originalUntil.getRightActions()))));
//            }
//            else {
//                formula2 = new BoolProp(true);
//            }
//
//            StateFormula thereExists2;
//            if(originalUntil.getRightActions().size() > 0) {
//                thereExists2 = new Not(new ThereExists(new Until(leftFormula, new And(leftFormula, new Not(rightFormula)), new HashSet<String>(), getSubtractionOfActions(originalUntil.getLeftActions()))));
//            } else {
//                thereExists2 = new BoolProp(true);
//            }

            StateFormula formula2;
            Set<String> newUntilLeftActions = replaceActions(originalUntil.getLeftActions());
            Set<String> newUntilRightActions = replaceActions(originalUntil.getRightActions());
            StateFormula thereExists2;
            if (originalUntil.getLeftActions().size() > 0 || originalUntil.getRightActions().size() > 0) {
                HashSet<String> rightSubLeftActions = new HashSet<>(newUntilRightActions);
                rightSubLeftActions.removeAll(newUntilLeftActions);


                HashSet<String> leftSubRightActions = new HashSet<>(newUntilLeftActions);
                leftSubRightActions.removeAll(newUntilRightActions);


                HashSet<String> rightAddLeftActions = new HashSet<>(newUntilRightActions);
                rightAddLeftActions.addAll(newUntilLeftActions);


//                formula2 = new ThereExists(new Until(new BoolProp(true), new And(new Not(leftFormula), new Not(rightFormula)), allActions, rightSubLeftActions));
                formula2 = new ThereExists(new Until(new BoolProp(true), new And(new Not(leftFormula), new Not(rightFormula)), allActions, rightAddLeftActions));

//                formula2 = new Not(new ThereExists(new Until(new BoolProp(true), rightFormula, originalUntil.getLeftActions(), getSubtractionOfActions(originalUntil.getRightActions()))));
//                Always alwaysClause = new Always(new Not(rightFormula), getSubtractionOfActions(newUntilRightActions));
                Always alwaysClause = new Always(new Not(rightFormula), getSubtractionOfActions(newUntilRightActions));
                Or formula1 = new Or(formula2, new ThereExists(alwaysClause));


                StateFormula formula3 = new ThereExists(new Until(new BoolProp(true), new And(rightFormula, new Not(leftFormula)), allActions, leftSubRightActions));
                StateFormula formula4 = new ThereExists(new Until(new BoolProp(true), new And(leftFormula, new Not(rightFormula)), allActions, rightSubLeftActions));

                Or or4 = new Or(formula3, formula4);

                thereExists2 = new Or(formula1, or4);

                HashSet<String> subtractionOfActions = getSubtractionOfActions(rightAddLeftActions);
                System.out.println("All actions not in x or y: " + subtractionOfActions);
                thereExists2 = new Or(thereExists2, new ThereExists(new Until(leftFormula, new BoolProp(true), newUntilLeftActions, subtractionOfActions)));
            } else {
                formula2 = new BoolProp(false);
                thereExists2 = new BoolProp(false);
            }


//                thereExists2 = new Not(new ThereExists(new Until(new BoolProp(true), rightFormula, getSubtractionOfActions(originalUntil.getLeftActions()), originalUntil.getRightActions())));


            if (thereExists2 instanceof BoolProp) {
                return new Not(new Or(leftThereExists, rightThereExists));
            }

            Or leftSatisfyingStates = new Or(formula2, thereExists2);
            return new Not(new Or(leftSatisfyingStates, new Or(leftThereExists, rightThereExists)));
//            return new And(leftThereExists, rightThereExists);
        } else {
            return null;
        }
    }

    private HashSet<String> getSubtractionOfActions(Set<String> actions) {
        HashSet<String> addActions = new HashSet<>();
        for (String action : allActions) {
            if (!actions.contains(action)) {
                addActions.add(action);
            }
        }

        return addActions;
    }

    private Set<String> replaceActions(Set<String> actions) {
        if (actions.size() == 0) {
            return allActions;
        } else {
            return actions;
        }
    }
}
