package frontend.gui;

import backend.algorithms.*;
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

    private ButtonGroup taskGroup;
    private JComboBox<String> algorithmJCB;
    private GraphVisualiserPanel graphVisualiserPanel;
    private EulerTourAlgorithm eulerTourAlgorithm;
    private FleurysAlgorithm fleurysAlgorithm;
    private HierholzersAlgorithm hierholzersAlgorithm;
    private EulerisationAlgorithm eulerisationAlgorithm;

    public RunButtonListener(ButtonGroup taskGroup, JComboBox<String> algorithmJCB,GraphVisualiserPanel graphVisualiserPanel) {
        this.taskGroup = taskGroup;
        this.algorithmJCB = algorithmJCB;
        this.graphVisualiserPanel = graphVisualiserPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Task task = getSelectedTask(taskGroup, algorithmJCB);
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
                case EuleriseGraph:
                    System.out.println("case eulerise graph");
                    eulerisationAlgorithm = new NearestNeighbourAlgorithm(graph);
                    break;
                case FleuryAlgorithm:
                    eulerTourAlgorithm = new FleurysAlgorithm();
                    AppGUI.algorithmVisualiser = new AlgorithmVisualiser(graphVisualiserPanel,eulerTourAlgorithm);
                    System.out.println("case fleury algorithm");
                    break;
                case HierholzersAlgorithm:
                    eulerTourAlgorithm = new HierholzersAlgorithm();
                    AppGUI.algorithmVisualiser = new AlgorithmVisualiser(graphVisualiserPanel,eulerTourAlgorithm);
                    System.out.println("case hierholzers algorithm");
                    break;
            }
        }
    }

    private Task getSelectedTask(ButtonGroup taskGroup, JComboBox<String> algorithmJCB) {
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
            JOptionPane.showMessageDialog(null,"No task selected","No task",JOptionPane.INFORMATION_MESSAGE);

        }
        return null;
    }
}
