package modelChecker;

import formula.stateFormula.StateFormula;
import model.Model;
import modelChecker.graphbuilding.InvalidTracingException;
import modelChecker.tracing.InvalidStateFormula;

/**
 * Defines the interface to model checker.
 *
 */
public interface ModelChecker {
    /**
     * verifies whether the model satisfies the query under the given
     * constraint.
     * 
     * @param model
     *            - model to verify
     * @param constraint
     *            - the constraint applied to the model before verification
     *            against the query.
     * @param query
     *            - the state formula to verify the model against.
     * @return - true if the model satisfies the query under the applied
     *         constraint.
     */
    boolean check(Model model, StateFormula constraint, StateFormula query) throws InvalidStateFormula;

    // Returns a trace of the previous check attempt if it failed.
    String[] getTrace() throws InvalidTracingException, InvalidStateFormula;
}
