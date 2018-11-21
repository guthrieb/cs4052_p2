package modelChecker.validitychecking;

import formula.pathFormula.*;
import formula.stateFormula.*;
import model.Model;
import model.Transition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EnfConverter {
    private Set<String> allActions;

    public EnfConverter(Model model) {
        Set<String> allActions = new HashSet<>();
        for (Transition transition : model.getTransitions()) {
            String[] actions = transition.getActions();
            allActions.addAll(Arrays.asList(actions));
        }

        this.allActions = allActions;
    }


    public StateFormula convertToEnf(StateFormula formula) {
        StateFormula returnVal;
        if (formula instanceof AtomicProp) {
            returnVal = formula;
        } else if (formula instanceof BoolProp) {
            if (!((BoolProp) formula).value) {
                returnVal = new Not(new BoolProp(true));
            } else {
                returnVal = formula;
            }
        } else if (formula instanceof Not) {
            returnVal = new Not(convertToEnf(((Not) formula).stateFormula));

        } else if (formula instanceof Or) {
            Or or = (Or) formula;

            StateFormula orLeft = convertToEnf(or.left);
            StateFormula orRight = convertToEnf(or.right);


            returnVal = new Or(orLeft, orRight);
        } else if (formula instanceof And) {
            And and = (And) formula;

            StateFormula andLeft = convertToEnf(and.left);
            StateFormula andRight = convertToEnf(and.right);

            returnVal = new And(andLeft, andRight);
        } else if (formula instanceof ThereExists) {
            returnVal = handleIfExists(formula);
        } else if (formula instanceof ForAll) {
            returnVal = handleForAll(formula);
        } else {
            return null;
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

            return new ThereExists(new Until(
                    new BoolProp(true), convertToEnf(eventually.stateFormula), convertedLeftActions, convertedRightActions)
            );

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
            return new ThereExists(
                    new Until(convertToEnf(until.left), convertToEnf(until.right), leftActions, rightActions)
            );
        }

        return null;
    }

    private StateFormula handleForAll(StateFormula formula) {
        ForAll forAll = (ForAll) formula;

        PathFormula pathFormula = forAll.pathFormula;

        if (pathFormula instanceof Next) {
            Next next = (Next) pathFormula;

            //Standard CTL conversion:  ∀()Φ ≡ ¬∃()¬Φ AND ¬ EX(true)
            //Handle state conversion
            ThereExists disjunction1 = new ThereExists(new Next(new Not(convertToEnf(next.stateFormula)), allActions));

            //Handle actions conversion
            Set<String> nextActions = replaceActions(next.getActions());
            if (nextActions.size() > 0) {

                HashSet<String> notNextActions = getSubtractionOfActions(nextActions);

                ThereExists disjunction2 = new ThereExists(
                        new Next(new BoolProp(true), notNextActions)
                );

                return new Not(new Or(disjunction1, disjunction2));
            } else {
                return new Not(disjunction1);
            }

        } else if (pathFormula instanceof Always) {
            //∀Φ ≡ ¬∃♦¬Φ = ¬∃(true U ¬Φ)
            Always always = (Always) pathFormula;
            Set<String> actions = replaceActions(always.getActions());

            ThereExists disjunction2 = new ThereExists(
                    new Until(new BoolProp(true), new Not(convertToEnf(always.stateFormula)), allActions, allActions)
            );

            ThereExists disjunction1 = new ThereExists(new Until(
                    new BoolProp(true), new BoolProp(true), allActions, getSubtractionOfActions(actions)));



            if (actions.size() == 0) {
                return new Not(disjunction2);
            } else {
                return new Not(new Or(disjunction1, disjunction2));
            }
        } else if (pathFormula instanceof Eventually) {
            //∀♦Φ ≡ ¬∃¬Φ
            Eventually eventually = (Eventually) pathFormula;

            Until untilConversion = new Until(
                    new BoolProp(true), convertToEnf(eventually.stateFormula), eventually.getLeftActions(), eventually.getRightActions()
            );

            ForAll forAllUntilConversion = new ForAll(untilConversion);
            return convertToEnf(forAllUntilConversion);
        } else if (pathFormula instanceof Until) {
            // Standard ctl conversion: ∀(Φ1 U Φ2) ≡ ¬∃(¬Φ2 U (¬Φ1 ∧ ¬Φ2)) ∧ ¬∃¬Φ2
            Until originalUntil = (Until) pathFormula;
            StateFormula leftFormula = convertToEnf(originalUntil.left);
            StateFormula rightFormula = convertToEnf(originalUntil.right);


            //State contents enf conversion
            Not notLeft = new Not(leftFormula);
            Not notRight = new Not(rightFormula);
            And nestedAnd = new And(notLeft, notRight);

            ThereExists ctlDisjunction1 = new ThereExists(new Always(notRight, allActions));
            ThereExists ctlDisjunction2 = new ThereExists(new Until(notRight, nestedAnd, allActions, allActions));

            Or standardCtlFormula = new Or(ctlDisjunction2, ctlDisjunction1);

            //State transitions enf conversion

            Set<String> newUntilLeftActions = replaceActions(originalUntil.getLeftActions());
            Set<String> newUntilRightActions = replaceActions(originalUntil.getRightActions());

            StateFormula asCtlFormula;

            asCtlFormula = getAsCtlFormula(originalUntil, leftFormula, rightFormula, newUntilLeftActions, newUntilRightActions);

            if (asCtlFormula instanceof BoolProp) {
                return new Not(standardCtlFormula);
            } else {
                Or stateFormula = new Or(asCtlFormula, standardCtlFormula);
                return new Not(stateFormula);
            }
        } else {
            return null;
        }
    }

    private StateFormula getAsCtlFormula(Until originalUntil, StateFormula leftFormula, StateFormula rightFormula,
                                         Set<String> newUntilLeftActions, Set<String> newUntilRightActions) {
        StateFormula asCtlFormula;
        if (originalUntil.getLeftActions().size() > 0 || originalUntil.getRightActions().size() > 0) {
            HashSet<String> rightSubLeftActions = new HashSet<>(newUntilRightActions);
            rightSubLeftActions.removeAll(newUntilLeftActions);


            HashSet<String> leftSubRightActions = new HashSet<>(newUntilLeftActions);
            leftSubRightActions.removeAll(newUntilRightActions);


            HashSet<String> rightAddLeftActions = new HashSet<>(newUntilRightActions);
            rightAddLeftActions.addAll(newUntilLeftActions);

            HashSet<String> allActionsNotInSet = getSubtractionOfActions(rightAddLeftActions);


            ThereExists disjunction1 = new ThereExists(
                    new Always(new BoolProp(true), getSubtractionOfActions(newUntilRightActions))
            );
            ThereExists disjunction2 = new ThereExists(
                    new Until(new BoolProp(true), new And(rightFormula, new Not(leftFormula)), allActions, leftSubRightActions)
            );
            ThereExists disjunction3 = new ThereExists(
                    new Until(new BoolProp(true), new And(leftFormula, new Not(rightFormula)), allActions, rightSubLeftActions)
            );
            ThereExists disjunction4 = new ThereExists(
                    new Until(leftFormula, new BoolProp(true), newUntilLeftActions, allActionsNotInSet)
            );
            ThereExists disjunction5 = new ThereExists(
                    new Until(new BoolProp(true), new And(new Not(leftFormula), new Not(rightFormula)), allActions, rightAddLeftActions)
            );


            Or or1 = new Or(disjunction5, disjunction1);
            Or or2 = new Or(disjunction2, disjunction3);
            asCtlFormula = new Or(or1, or2);
            asCtlFormula = new Or(asCtlFormula, disjunction4);
        } else {
            asCtlFormula = new BoolProp(false);
        }
        return asCtlFormula;
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
