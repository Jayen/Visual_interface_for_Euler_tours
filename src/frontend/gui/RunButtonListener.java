package frontend.gui;

import backend.algorithms.EulerTourChecker;
import backend.algorithms.eulerTourAlgorithms.EulerTourAlgorithm;
import backend.algorithms.eulerTourAlgorithms.FleurysAlgorithm;
import backend.algorithms.eulerTourAlgorithms.HierholzersAlgorithm;
import backend.algorithms.eulerisationAlgorithm.EulerisationAlgorithm;
import backend.algorithms.eulerisationAlgorithm.NearestNeighbourAlgorithm;
import backend.algorithms.eulerisationAlgorithm.SimulatedAnnealing;
import backend.internalgraph.Graph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is the ActionListener for
 * the rub button. The class manages executing different
 * tasks that the user may select
 * @author Jayen kumar Jaentilal k1189304
 */
public class RunButtonListener implements ActionListener {

    private AppGUI appGUI;
    private ButtonGroup taskGroup;
    private JComboBox eulerisationAlgorithmJCB;
    private JComboBox<String> algorithmJCB;
    private GraphVisualiserPanel graphVisualiserPanel;
    private EulerTourAlgorithm eulerTourAlgorithm;
    private FleurysAlgorithm fleurysAlgorithm;
    private HierholzersAlgorithm hierholzersAlgorithm;
    private EulerisationAlgorithm eulerisationAlgorithm;
    private Thread eulerisationThread;
    private Graph graph;

    public RunButtonListener(AppGUI appGUI,ButtonGroup taskGroup, JComboBox<String> eulerisationAlgorithmJCB, JComboBox<String> algorithmJCB,GraphVisualiserPanel graphVisualiserPanel) {
        this.appGUI = appGUI;
        this.taskGroup = taskGroup;
        this.eulerisationAlgorithmJCB = eulerisationAlgorithmJCB;
        this.algorithmJCB = algorithmJCB;
        this.graphVisualiserPanel = graphVisualiserPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Task task = getSelectedTask(taskGroup, eulerisationAlgorithmJCB,algorithmJCB);
        if(task!=null) {
            graph = graphVisualiserPanel.getCurrentGraph();
            fleurysAlgorithm = new FleurysAlgorithm();
            hierholzersAlgorithm = new HierholzersAlgorithm();
            switch(task) {
                case EulerTourCheck:
                    if(EulerTourChecker.hasEulerTour(graph)) {
                        JOptionPane.showMessageDialog(null,"This graph has a Euler tour","Info",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"This graph has no Euler tour","Info",JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case NearestNeighbour:
                    appGUI.setStatus("Eulerising graph");
                    eulerisationThread = new Thread() {
                        @Override
                        public void run() {
                            eulerisationAlgorithm = new NearestNeighbourAlgorithm(graph);
                            eulerisationAlgorithm.euleriseGraph(true);
                        }
                    };
                    eulerisationThread.start();
                    break;
                case LocalSearch:
                    appGUI.setStatus("Eulerising graph");
                    eulerisationThread = new Thread() {
                        @Override
                        public void run() {
                            eulerisationAlgorithm = new SimulatedAnnealing(graph,true);
                            eulerisationAlgorithm.euleriseGraph(true);
                        }
                    };
                    eulerisationThread.start();
                    break;
                case SimulatedAnnealing:
                    appGUI.setStatus("Eulerising graph");
                    eulerisationThread = new Thread() {
                        @Override
                        public void run() {
                            eulerisationAlgorithm = new SimulatedAnnealing(graph,false);
                            eulerisationAlgorithm.euleriseGraph(true);
                        }
                    };
                    eulerisationThread.start();
                    break;
                case FleuryAlgorithm:
                    eulerTourAlgorithm = new FleurysAlgorithm();
                    AppGUI.algorithmVisualiser = new AlgorithmVisualiser(graphVisualiserPanel,eulerTourAlgorithm);
                    break;
                case HierholzersAlgorithm:
                    eulerTourAlgorithm = new HierholzersAlgorithm();
                    AppGUI.algorithmVisualiser = new AlgorithmVisualiser(graphVisualiserPanel,eulerTourAlgorithm);
                    break;
            }
        }
    }

    private Task getSelectedTask(ButtonGroup taskGroup, JComboBox eulerisationAlgorithmJCB, JComboBox<String> algorithmJCB) {
        try {
            String actionCommand = taskGroup.getSelection().getActionCommand();
            if(actionCommand.equals(Task.FindEulerTour.getName())) {
                if(algorithmJCB.getSelectedItem().equals(Task.FleuryAlgorithm.getName())) {
                    return Task.FleuryAlgorithm;
                }
                else if(algorithmJCB.getSelectedItem().equals(Task.HierholzersAlgorithm.getName())) {
                    return Task.HierholzersAlgorithm;
                }
            }

            else if(actionCommand.equals(Task.EulerTourCheck.getName())) {
                return Task.EulerTourCheck;
            }
            else if(actionCommand.equals(Task.EuleriseGraph.getName())) {
                if(eulerisationAlgorithmJCB.getSelectedItem().equals(Task.NearestNeighbour.getName())) {
                    return Task.NearestNeighbour;
                }
                else if(eulerisationAlgorithmJCB.getSelectedItem().equals(Task.LocalSearch.getName())) {
                    return Task.LocalSearch;
                }
                else if(eulerisationAlgorithmJCB.getSelectedItem().equals(Task.SimulatedAnnealing.getName())) {
                    return Task.SimulatedAnnealing;
                }
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"No task selected","No task",JOptionPane.INFORMATION_MESSAGE);

        }
        return null;
    }
}
