package frontend.gui;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.Graph;
import com.alee.extended.filechooser.FilesSelectionListener;
import com.alee.extended.filechooser.WebFileChooserField;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.progressbar.WebProgressBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Main class for GUI,
 * This class sets up the GUI frontend
 * @author Jayen kumar Jaentilal k1189304
 */

public class AppGUI extends JFrame {

    public static File currentFile;
    public static GraphVisualiserPanel graphVisualiserPanel;
    public static AlgorithmVisualiser algorithmVisualiser;
    private JButton runJB;

    private Graph graph;
    private JPanel mainPanel;
    private WebProgressBar progressBar;
    private static JLabel edgesCost;
    private static JLabel numberOfEdges;

    public AppGUI() {
        super("Euler Tours Visual Interface");
        try {
            UIManager.setLookAndFeel(new WebLookAndFeel());
        }
        catch (Exception e) {
            //some problem occurred with setting the WebLookAndFeel defaults to native look
        }
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(700,700);
        graphVisualiserPanel = new GraphVisualiserPanel(this);
        this.setupMainGUI();
    }

    private void setupMainGUI() {
        BorderLayout mainLayout = new BorderLayout();
        this.setLayout(mainLayout);
        mainPanel = new JPanel(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Algorithm", createAlgorithmTab());
        tabbedPane.add("Help", createHelpTab());
        mainPanel.add(tabbedPane, BorderLayout.NORTH);
        mainPanel.add(createCostPanel(), BorderLayout.CENTER);
        this.setupGraphVisualiser();
        this.add(mainPanel, BorderLayout.WEST);
    }

    private Component createCostPanel() {
        JPanel costPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(2,0,2,0);
        JLabel edgesCostLabel = new JLabel("Edges Added Cost: ");
        constraints.gridx++;
        edgesCost = new JLabel();
        constraints.gridx--;
        constraints.gridy++;
        JLabel edgesAddedLabel = new JLabel("Edges Added: ");
        constraints.gridx++;
        numberOfEdges = new JLabel();
        costPanel.add(edgesCostLabel);
        costPanel.add(edgesCost);
        costPanel.add(edgesAddedLabel);
        costPanel.add(numberOfEdges);
        return costPanel;
    }

    /**
     * Method to update the cost panel
     * @param cost cost of the edges added
     * @param numberOfEdgesAdded the number of edges added to the system
     */
    public static void updateCostPanel(double cost, int numberOfEdgesAdded) {
        edgesCost.setText(Double.toString(cost));
        numberOfEdges.setText(Integer.toString(numberOfEdgesAdded));
    }

    /**
     * Setup the graph visualiser
     * part of the GUI
     */
    private void setupGraphVisualiser() {
        JPanel graphViewPanel = new JPanel(new BorderLayout());

        JScrollPane jsp = new JScrollPane(graphVisualiserPanel);
        HandScrollListener handScrollListener = new HandScrollListener(graphVisualiserPanel);
        jsp.getViewport().addMouseMotionListener(handScrollListener);
        jsp.getViewport().addMouseListener(handScrollListener);
        jsp.getViewport().addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if(notches<0) {
                    graphVisualiserPanel.incrementScaleFactor();
                }
                else if(notches>0) {
                    if(graphVisualiserPanel.getScaleFactor()!=1.0) {
                        graphVisualiserPanel.decrementScaleFactor();
                    }
                    revalidate();
                    repaint();
                }
            }
        });
        graphViewPanel.add(jsp,BorderLayout.CENTER);

        WebButton clearButton = new WebButton("Reset");
        WebButton backButton = new WebButton("Back");
        WebButton pausePlayButton = new WebButton("Pause/Play");
        WebButton nextButton = new WebButton("Next");

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                algorithmVisualiser.resetVisualisation();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                algorithmVisualiser.undoStep();
            }
        });

        pausePlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                algorithmVisualiser.pausePlay();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                algorithmVisualiser.nextStep();
            }
        });

        JPanel visualisationButtonPanel = new JPanel(new GridLayout(1,4));
        visualisationButtonPanel.add(clearButton);
        visualisationButtonPanel.add(backButton);
        visualisationButtonPanel.add(pausePlayButton);
        visualisationButtonPanel.add(nextButton);
        graphViewPanel.add(visualisationButtonPanel,BorderLayout.SOUTH);

        this.add(graphViewPanel,BorderLayout.CENTER);
    }

    /**
     * Create the algorithm tab panel
     * @return JPanel
     */
    private Component createAlgorithmTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(2,0,2,0);


        JPanel filePanel = new JPanel();
        JLabel fileChooserLabel = new JLabel("Graph file: ");
        filePanel.add(fileChooserLabel);
        final WebFileChooserField fileChooserField = new WebFileChooserField(this);
        fileChooserField.setPreferredSize(new Dimension(210,25));
        fileChooserField.setMultiSelectionEnabled(false);
        fileChooserField.getWebFileChooser().setFileFilter(new FileNameExtensionFilter("TEXT FILES","txt","text"));
        fileChooserField.addSelectedFilesListener(new FilesSelectionListener() {
            @Override
            public void selectionChanged(List<File> files) {
                try {
                    currentFile = files.get(0);
                    AppGUI.this.setStatus("loading graph...");
                    graph = GraphParser.createGraphFromFile(currentFile);
                    graphVisualiserPanel.drawNewGraph(graph);
                } catch (IncorrectFileFormatException ignored) {
                } catch (IOException ignored) {
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        });
        filePanel.add(fileChooserField,constraints);
        panel.add(filePanel);

        constraints.gridy++;
        JLabel taskLabel = new JLabel("Select Task:");
        panel.add(taskLabel,constraints);

        JRadioButton eulerTourCheckRB = new JRadioButton(Task.EulerTourCheck.getName());
        final JRadioButton euleriseGraphRB = new JRadioButton(Task.EuleriseGraph.getName());

        JLabel eulerisationAlgoJL = new JLabel("Algorithm: ");
        final JRadioButton nearestNeighbourRB = new JRadioButton(Task.NearestNeighbour.getName());
        final JRadioButton localSearchRB = new JRadioButton(Task.LocalSearch.getName());
        final JRadioButton simulatedAnnealing = new JRadioButton(Task.SimulatedAnnealing.getName());

        nearestNeighbourRB.setActionCommand(Task.NearestNeighbour.getName());
        localSearchRB.setActionCommand(Task.LocalSearch.getName());
        simulatedAnnealing.setActionCommand(Task.SimulatedAnnealing.getName());

        euleriseGraphRB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(euleriseGraphRB.isSelected()) {
                    nearestNeighbourRB.setEnabled(true);
                    localSearchRB.setEnabled(true);
                    simulatedAnnealing.setEnabled(true);
                }
                else {
                    nearestNeighbourRB.setEnabled(false);
                    localSearchRB.setEnabled(false);
                    simulatedAnnealing.setEnabled(false);
                }
            }
        });

        ButtonGroup eulerisationAlgorithmGroup = new ButtonGroup();
        eulerisationAlgorithmGroup.add(nearestNeighbourRB);
        eulerisationAlgorithmGroup.add(localSearchRB);
        eulerisationAlgorithmGroup.add(simulatedAnnealing);

        nearestNeighbourRB.setEnabled(false);
        localSearchRB.setEnabled(false);
        simulatedAnnealing.setEnabled(false);

        JRadioButton findEulerTourRB = new JRadioButton(Task.FindEulerTour.getName());
        eulerTourCheckRB.setActionCommand(Task.EulerTourCheck.getName());
        euleriseGraphRB.setActionCommand(Task.EuleriseGraph.getName());
        findEulerTourRB.setActionCommand(Task.FindEulerTour.getName());

        ButtonGroup taskGroup = new ButtonGroup();
        taskGroup.add(eulerTourCheckRB);
        taskGroup.add(euleriseGraphRB);
        taskGroup.add(findEulerTourRB);


        constraints.gridy++;
        panel.add(taskLabel,constraints);
        constraints.gridy++;
        panel.add(eulerTourCheckRB,constraints);
        constraints.gridy++;
        panel.add(euleriseGraphRB,constraints);

        constraints.gridy++;
        panel.add(eulerisationAlgoJL,constraints);
        constraints.gridy++;
        panel.add(nearestNeighbourRB,constraints);
        constraints.gridy++;
        panel.add(localSearchRB,constraints);
        constraints.gridy++;
        panel.add(simulatedAnnealing,constraints);

        constraints.gridy++;
        panel.add(findEulerTourRB,constraints);

        JPanel algorithmPanel = new JPanel();
        JLabel algorithmLabel = new JLabel("Algorithm: ");
        JComboBox<String> algorithmJCB = new JComboBox<String>();
        algorithmJCB.addItem(Task.FleuryAlgorithm.getName());
        algorithmJCB.addItem(Task.HierholzersAlgorithm.getName());

        algorithmPanel.add(algorithmLabel);
        algorithmPanel.add(algorithmJCB);
        constraints.gridy++;
        panel.add(algorithmPanel,constraints);

        runJB = new JButton("Run Task");
        runJB.addActionListener(new RunButtonListener(this,taskGroup,eulerisationAlgorithmGroup, algorithmJCB, graphVisualiserPanel));
        constraints.gridy++;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(runJB,constraints);

        return panel;
    }

    /**
     * Set the status of the application
     * Used to indicate to the user what is happening
     * through a progress bar
     * @param status
     */
    public void setStatus(String status) {
        progressBar = new WebProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(status);
        mainPanel.add(progressBar, BorderLayout.SOUTH);
        this.revalidate();
        this.repaint();
    }

    public void clearStatus() {
        mainPanel.remove(progressBar);
        this.revalidate();
        this.repaint();
    }

    /**
     * Create the help tab panel
     * @return JPanel
     */
    private Component createHelpTab() {
        return new JPanel();
    }
}