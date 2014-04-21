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
        mainPanel.add(createCostPanel(), BorderLayout.WEST);
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
        edgesCost = new JLabel();
        JLabel edgesAddedLabel = new JLabel("Edges Added: ");
        numberOfEdges = new JLabel();

        costPanel.add(edgesCostLabel,constraints);
        constraints.gridx++;
        costPanel.add(edgesCost,constraints);
        constraints.gridx--;
        constraints.gridy++;
        costPanel.add(edgesAddedLabel,constraints);
        constraints.gridx++;
        costPanel.add(numberOfEdges,constraints);
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
        graphViewPanel.setBorder(BorderFactory.createTitledBorder("Graph visualiser"));
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
                if(algorithmVisualiser!=null) {
                    algorithmVisualiser.resetVisualisation();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(algorithmVisualiser!=null) {
                    algorithmVisualiser.undoStep();
                }
            }
        });

        pausePlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(algorithmVisualiser!=null) {
                    algorithmVisualiser.pausePlay();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(algorithmVisualiser!=null) {
                    algorithmVisualiser.nextStep();
                }
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
                } catch (IncorrectFileFormatException e) {
                    JOptionPane.showMessageDialog(null,"The files content is not correctly formatted","Incorrect file",JOptionPane.INFORMATION_MESSAGE);
                    AppGUI.this.clearStatus();
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

        JPanel eulerisationAlgoPanel = new JPanel();
        JLabel eulerisationAlgoJL = new JLabel("Algorithm: ");
        JComboBox<String> eulerisationAlgoJCB = new JComboBox<String>();
        eulerisationAlgoJCB.addItem(Task.NearestNeighbour.getName());
        eulerisationAlgoJCB.addItem(Task.LocalSearch.getName());
        eulerisationAlgoJCB.addItem(Task.SimulatedAnnealing.getName());
        eulerisationAlgoPanel.add(eulerisationAlgoJL);
        eulerisationAlgoPanel.add(eulerisationAlgoJCB);

        constraints.gridy++;
        panel.add(eulerisationAlgoPanel,constraints);

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
        runJB.addActionListener(new RunButtonListener(this,taskGroup,eulerisationAlgoJCB, algorithmJCB, graphVisualiserPanel));
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
        JPanel helpPanel = new JPanel();
        JLabel label = new JLabel("<html>The file format is:<br>" +
                "#nodes (special identifier for nodes)<br>" +
                "list of nodes in format: Node name[space](x, y)<br>" +
                "(where x, y are the Cartesian coordinates).<br>" +
                "[New line]\n<br>" +
                "#edges (special identifier for edges)<br>" +
                "list of edges in the format: Node name–Node name<br>" +
                "<br>example file<br>"+
                "#nodes<br>" +
                "A (0,0)<br>" +
                "B (12,10)<br>" +
                "C (5,5)<br>" +
                "<br>" +
                "#edges<br>" +
                "A—B<br>" +
                "B—C<br>");
        helpPanel.add(label);
        return helpPanel;
    }
}