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
    private ButtonGroup algorithmGroup;
    private JComboBox<String> algorithmJCB;
    private GraphVisualiserPanel graphVisualiserPanel;
    private EulerTourAlgorithm eulerTourAlgorithm;
    private FleurysAlgorithm fleurysAlgorithm;
    private HierholzersAlgorithm hierholzersAlgorithm;
    private EulerisationAlgorithm eulerisationAlgorithm;

    public RunButtonListener(AppGUI appGUI,ButtonGroup taskGroup,ButtonGroup algorithmGroup, JComboBox<String> algorithmJCB,GraphVisualiserPanel graphVisualiserPanel) {
        this.appGUI = appGUI;
        this.taskGroup = taskGroup;
        this.algorithmGroup = algorithmGroup;
        this.algorithmJCB = algorithmJCB;
        this.graphVisualiserPanel = graphVisualiserPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Task task = getSelectedTask(taskGroup,algorithmGroup,algorithmJCB);
        if(task!=null) {
            Graph graph = graphVisualiserPanel.getCurrentGraph();
            fleurysAlgorithm = new FleurysAlgorithm();
            hierholzersAlgorithm = new HierholzersAlgorithm();
            switch(task) {
                case EulerTourCheck:
                    //TODO show result on GUI
                    System.out.println("case euler tour check");
                    if(EulerTourChecker.hasEulerTour(graph)) {
                        System.out.println("euler tour exists");
                    }
                    else {
                        System.out.println("euler tour does not exist");
                    }
                    break;
                case NearestNeighbour:
                    appGUI.setStatus("Eulerising graph");
                    eulerisationAlgorithm = new NearestNeighbourAlgorithm(graph);
                    eulerisationAlgorithm.euleriseGraph(true);
                    break;
                case LocalSearch:
                    appGUI.setStatus("Eulerising graph");
                    eulerisationAlgorithm = new SimulatedAnnealing(graph,true);
                    eulerisationAlgorithm.euleriseGraph(true);
                    break;
                case SimulatedAnnealing:
                    appGUI.setStatus("Eulerising graph");
                    eulerisationAlgorithm = new SimulatedAnnealing(graph,false);
                    eulerisationAlgorithm.euleriseGraph(true);
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

    private Task getSelectedTask(ButtonGroup taskGroup, ButtonGroup algorithmGroup, JComboBox<String> algorithmJCB) {
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
                String algorithmCommand = algorithmGroup.getSelection().getActionCommand();
                if(algorithmCommand.equals(Task.NearestNeighbour.getName())) {
                    return Task.NearestNeighbour;
                }
                else if(algorithmCommand.equals(Task.LocalSearch.getName())) {
                    return Task.LocalSearch;
                }
                else if(algorithmCommand.equals(Task.SimulatedAnnealing.getName())) {
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
