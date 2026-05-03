package solver.visualization.gui;

import solver.core.State;

import java.util.List;

public class PlaybackController {

    private List<State> history;
    private int index = 0;

    public void setHistory(List<State> history) {
        this.history = history;
        this.index = 0;
    }

    public State next() {
        if (index < history.size() - 1)
            index++;
        return history.get(index);
    }

    public State prev() {
        if (index > 0)
            index--;
        return history.get(index);
    }

    public State getCurrent() {
        return history.get(index);
    }
}