package model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Each transition may have a set of actions to be performed. 
 * 
 **/
public class  Transition {
    private String source;
    private String target;
    private String [] actions;
	
    /**
     * Returns the source state of a transition.
     * @return the id of the source state
     * */
    public String getSource() {
	return source;
    }
    /**
     * Returns the target state of a transition.
     * @return the id of the target state
     * */
    public String getTarget() {
	return target;
    }
    /**
     * Returns the set of actions in a transition.
     * @return a set of actions.
     * */
    public String[] getActions() {
	return actions;
    }
	
    @Override
    public String toString() {
        return this.source + "-" +
                Arrays.toString(this.actions) + "-" +
                this.target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return Objects.equals(source, that.source) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }



}
