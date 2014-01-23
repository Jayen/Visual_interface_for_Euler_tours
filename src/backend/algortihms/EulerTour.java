package backend.algortihms;

import backend.internalgraph.LocationFixedSparseGraph;

/**
 * Jayen kumar Jaentilal k1189304
 */
public class EulerTour {

    public static boolean EulerTourCheck(LocationFixedSparseGraph graph) {
        ConnectivityChecker connectivityChecker = new ConnectivityChecker(graph);
        if(connectivityChecker.isConnected() && hasNoOddDegreeNodes()) {
            return true;
        }
        return false;
    }

    private static boolean hasNoOddDegreeNodes() {
        //TODO
        return true;
    }

}
