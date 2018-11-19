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
    private Set<String> allActions = new HashSet<>();

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

            return new ThereExists(new Until(new BoolProp(true), convertToEnf(eventually.stateFormula), eventually.getLeftActions(), eventually.getRightActions()));
        } else if (pathFormula instanceof Next) {
            Next next = (Next) pathFormula;

            return new ThereExists(new Next(convertToEnf(next.stateFormula), next.getActions()));
        } else if (pathFormula instanceof Always) {
            Always always = (Always) pathFormula;

            return new ThereExists(new Always(convertToEnf(always.stateFormula), always.getActions()));
        } else if (pathFormula instanceof Until) {
            Until until = (Until) pathFormula;

            return new ThereExists(new Until(convertToEnf(until.left), convertToEnf(until.right), until.getLeftActions(), until.getRightActions()));
        }

        return null;
    }

    private StateFormula handleForAll(StateFormula formula) {
        ForAll forAll = (ForAll) formula;

        PathFormula pathFormula = forAll.pathFormula;

        if (pathFormula instanceof Next) {
            //∀()Φ ≡ ¬∃()¬Φ AND ¬ EX(true)
            Next next = (Next) pathFormula;

            Not left = new Not(new ThereExists(new Next(new Not(convertToEnf(next.stateFormula)), new HashSet<String>())));

            if (next.getActions().size() > 0) {
                HashSet<String> addActions;
                addActions = getSubtractionOfActions(next.getActions());
                Not right = new Not(new ThereExists(new Next(new BoolProp(true), addActions)));
                return new And(left, right);
            } else {
                return left;
            }
//            return new Not(new ThereExists(new Next(new Not(convertToEnf(next.stateFormula)), next.getActions())));
        } else if (pathFormula instanceof Always) {
            //∀Φ ≡ ¬∃♦¬Φ = ¬∃(true U ¬Φ)
            Always always = (Always) pathFormula;
            Until until = new Until(new BoolProp(true), new Not(convertToEnf(always.stateFormula)), new HashSet<String>(), always.getActions());
            ThereExists thereExists = new ThereExists(until);

            Not left = new Not(new ThereExists(new Until(
                    new BoolProp(true), new BoolProp(true), new HashSet<String>(), getSubtractionOfActions(always.getActions()))));

            Not right = new Not(new ThereExists(new Until(
                    new BoolProp(true), new Not(convertToEnf(always.stateFormula)), new HashSet<String>(), new HashSet<String>()
            )));

            if (always.getActions().size() == 0) {
                return right;
            } else {
                return new And(left, right);
            }

//            return new Not(thereExists);
        } else if (pathFormula instanceof Eventually) {
            //∀♦Φ ≡ ¬∃¬Φ
            Eventually eventually = (Eventually) pathFormula;

            Not not = new Not(convertToEnf(eventually.stateFormula));
            Always always = new Always(not, eventually.getRightActions());
            ThereExists thereExists = new ThereExists(always);
            return new Not(thereExists);
        } else if (pathFormula instanceof Until) {
            //∀(Φ1 U Φ2) ≡ ¬∃(¬Φ2 U (¬Φ1 ∧ ¬Φ2)) ∧ ¬∃¬Φ2
            Until originalUntil = (Until) pathFormula;
            StateFormula leftFormula = convertToEnf(originalUntil.left);
            StateFormula rightFormula = convertToEnf(originalUntil.right);


            // STATE CONTENTS
            Not notLeft = new Not(leftFormula);
            Not notRight = new Not(rightFormula);

            Not rightThereExists = new Not(new ThereExists(new Always(notRight, new HashSet<String>())));

            And nestedAnd = new And(notLeft, notRight);
            Not leftThereExists = new Not(new ThereExists(new Until(notRight, nestedAnd, new HashSet<String>(), new HashSet<String>())));


            //ACTIONS
            Set<String> notYActions = getSubtractionOfActions(originalUntil.getRightActions());
            Set<String> notXActions = getSubtractionOfActions(originalUntil.getLeftActions());


            Set<String> notXAndYActions = new HashSet<>(notXActions);
            notXAndYActions.addAll(notYActions);

//            Not leftActions = new Not(new ThereExists(new Until(new BoolProp(true), new BoolProp(true), notYActions, notXAndYActions)));
//            Not rightActions = new Not(new ThereExists(new Always(new BoolProp(true), notYActions)));


//            StateFormula leftActions;
//            if(originalUntil.getLeftActions().size() > 0) {
//                leftActions = new Not(new ThereExists(new Until(new And(leftFormula, new Not(rightFormula)), rightFormula, new HashSet<String>(), getSubtractionOfActions(originalUntil.getRightActions()))));
//            }
//            else {
//                leftActions = new BoolProp(true);
//            }
//
//            StateFormula rightActions;
//            if(originalUntil.getRightActions().size() > 0) {
//                rightActions = new Not(new ThereExists(new Until(leftFormula, new And(leftFormula, new Not(rightFormula)), new HashSet<String>(), getSubtractionOfActions(originalUntil.getLeftActions()))));
//            } else {
//                rightActions = new BoolProp(true);
//            }
//
            StateFormula leftActions;
            if (originalUntil.getLeftActions().size() > 0) {
                leftActions = new Not(new ThereExists(new Until(new BoolProp(true), rightFormula, originalUntil.getLeftActions(), getSubtractionOfActions(originalUntil.getRightActions()))));
            } else {
                leftActions = new BoolProp(true);
            }
//
            StateFormula rightActions;
            if (originalUntil.getRightActions().size() > 0) {
                rightActions = new Not(new ThereExists(new Until(new BoolProp(true), rightFormula, getSubtractionOfActions(originalUntil.getLeftActions()), originalUntil.getRightActions())));
            } else {
                rightActions = new BoolProp(true);
            }


            And leftSatisfyingStates = new And(leftActions, rightActions);
            return new And(leftSatisfyingStates, new And(leftThereExists, rightThereExists));
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

}
