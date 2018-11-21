package modelChecker.tracing;

import model.State;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<State> path = new ArrayList<>();
    private boolean finished = false;

    public void add(State toAdd) {
        path.add(toAdd);
    }

    void remove() {
        path.remove(path.size() - 1);
    }

    boolean isFinished() {
        return finished;
    }

    void setFinished() {
        this.finished = true;
    }

    List<State> getPath() {
        return path;
    }

}
