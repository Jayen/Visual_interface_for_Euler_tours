package frontend.gui;

import backend.algorithms.EulerTourChecker;
import backend.algorithms.FleurysAlgorithm;
import backend.algorithms.HierholzersAlgorithm;
import backend.internalgraph.Graph;
import backend.internalgraph.LocationFixedSparseGraph;

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
    private GraphVisualiser graphVisualiser;
    private FleurysAlgorithm fleurysAlgorithm;
    private HierholzersAlgorithm hierholzersAlgorithm;

    public RunButtonListener(ButtonGroup taskGroup, JComboBox<String> algorithmJCB,GraphVisualiser graphVisualiser) {
        this.taskGroup = taskGroup;
        this.algorithmJCB = algorithmJCB;
        this.graphVisualiser = graphVisualiser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Task task = getTask(taskGroup,algorithmJCB);
        Graph graph = graphVisualiser.getCurrentGraph();
        fleurysAlgorithm = new FleurysAlgorithm();
        hierholzersAlgorithm = new HierholzersAlgorithm();
        switch(task) {
            case EulerTourCheck:
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
                System.out.println("case fleury algorithm");
                fleurysAlgorithm.getEulerTour();
                break;
            case HierholzersAlgorithm:
                System.out.println("case hierholzers algorithm");
                hierholzersAlgorithm.getEulerTour();
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
