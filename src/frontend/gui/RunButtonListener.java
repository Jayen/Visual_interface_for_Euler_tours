package frontend.gui;

import backend.algorithms.EulerTourAlgorithm;
import backend.algorithms.EulerTourChecker;
import backend.algorithms.FleurysAlgorithm;
import backend.algorithms.HierholzersAlgorithm;
import backend.internalgraph.Graph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Jayen
 * Date: 25/01/14, Time: 14:44
 */
public class RunButtonListener implements ActionListener {

    private ButtonGroup taskGroup;
    private JComboBox<String> algorithmJCB;
    private GraphVisualiserPanel graphVisualiserPanel;
    private EulerTourAlgorithm eulerTourAlgorithm;
    private FleurysAlgorithm fleurysAlgorithm;
    private HierholzersAlgorithm hierholzersAlgorithm;
    private AlgorithmVisualiser algorithmVisualiser;

    public RunButtonListener(ButtonGroup taskGroup, JComboBox<String> algorithmJCB,GraphVisualiserPanel graphVisualiserPanel) {
        this.taskGroup = taskGroup;
        this.algorithmJCB = algorithmJCB;
        this.graphVisualiserPanel = graphVisualiserPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Task task = getTask(taskGroup,algorithmJCB);
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
            case EuleriseGraph:
                System.out.println("case eulerise graph");
                break;
            case FleuryAlgorithm:
                eulerTourAlgorithm = new FleurysAlgorithm();
                algorithmVisualiser = new AlgorithmVisualiser(graphVisualiserPanel,eulerTourAlgorithm);
                System.out.println("case fleury algorithm");
                break;
            case HierholzersAlgorithm:
                eulerTourAlgorithm = new HierholzersAlgorithm();
                algorithmVisualiser = new AlgorithmVisualiser(graphVisualiserPanel,eulerTourAlgorithm);
                System.out.println("case hierholzers algorithm");
                break;
        }
    }

    private Task getTask(ButtonGroup taskGroup, JComboBox<String> algorithmJCB) {
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
                return Task.EuleriseGraph;
            }
        }
        catch (NullPointerException e) {
            //TODO add waring since user never selected task from radio buttons
        }
        return null;
    }
}
