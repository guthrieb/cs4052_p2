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

    public void remove() {
        path.remove(path.size() - 1);
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<State> getPath() {
        return path;
    }
}
