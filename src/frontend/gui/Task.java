package frontend.gui;

/**
 * Enum holds all the different Tasks
 * that the user can execute
 * @author Jayen kumar Jaentilal k1189304
 */
public enum Task {

    EulerTourCheck("Check if Euler Tour exists"),
    EuleriseGraph("Eulerise the graph"),
    FindEulerTour("Find Euler Tour"),
    FleuryAlgorithm("Fleury's Algorithm"),
    HierholzersAlgorithm("Hierholzer's Algorithm");

    private final String name;

    Task(String taskString) {
        this.name = taskString;
    }

    public String getName() {
        return name;
    }

    public Task getValue(String name) {
        for(Task e: Task.values()) {
            if(e.name.equals(name)) {
                return e;
            }
        }
        return null;// not found
    }
}

