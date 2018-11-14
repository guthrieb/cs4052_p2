package modelChecker.tracing;

import formula.pathFormula.*;
import formula.stateFormula.*;

import java.util.HashSet;

public class EnfConverter {


    public StateFormula convertToEnf(StateFormula formula) {
        if (formula instanceof AtomicProp) {
            return formula;
        } else if (formula instanceof BoolProp) {
            if (!((BoolProp) formula).value) {
                return new Not(new BoolProp(true));
            } else {
                return formula;
            }
        } else if (formula instanceof Not) {
            return convertToEnf(new Not(((Not) formula).stateFormula));
        } else if (formula instanceof Or) {
            Or or = (Or) formula;

            StateFormula orLeft = convertToEnf(or.left);
            StateFormula orRight = convertToEnf(or.right);

            return new Or(orLeft, orRight);
        } else if (formula instanceof And) {
            And and = (And) formula;

            StateFormula andLeft = convertToEnf(and.left);
            StateFormula andRight = convertToEnf(and.right);

            return new And(andLeft, andRight);
        } else if (formula instanceof ThereExists) {
            return handleIfExists(formula);
        } else if (formula instanceof ForAll) {
            return handleForAll(formula);
        }

        return formula;
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
            //∀()Φ ≡ ¬∃()¬Φ
            Next next = (Next) pathFormula;
            return new Not(new ThereExists(new Next(new Not(convertToEnf(next.stateFormula)), next.getActions())));
        } else if (pathFormula instanceof Always) {
            //∀Φ ≡ ¬∃♦¬Φ = ¬∃(true U ¬Φ)
            Always always = (Always) pathFormula;
            Until until = new Until(new BoolProp(true), new Not(convertToEnf(always.stateFormula)), new HashSet<String>(), always.getActions());
            ThereExists thereExists = new ThereExists(until);

            return new Not(thereExists);
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
            Not notLeft = new Not(leftFormula);
            Not notRight = new Not(rightFormula);


            Not rightThereExists = new Not(new ThereExists(new Always(notRight, originalUntil.getRightActions())));


            And nestedAnd = new And(notLeft, notRight);
            Not leftThereExists = new Not(new ThereExists(new Until(notRight, nestedAnd, originalUntil.getLeftActions(), originalUntil.getRightActions())));


            return new And(leftThereExists, rightThereExists);
        }

        return null;
    }

}
