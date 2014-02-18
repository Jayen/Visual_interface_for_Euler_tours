package frontend.gui;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.Graph;
import com.alee.extended.filechooser.FilesSelectionListener;
import com.alee.extended.filechooser.WebFileChooserField;
import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Main class for GUI,
 * This class sets up the GUI frontend
 *@author Jayen kumar Jaentilal k1189304
 */

public class AppGUI extends JFrame {

    public static File currentFile;
    public static Graph graph;

    private JButton runJB;
    private GraphVisualiser graphVisualiser;

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
        graphVisualiser = new GraphVisualiser(this);
        this.setupMainGUI();
    }


    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open...");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");

        open.addActionListener(new OpenFileActionListener(this,graphVisualiser));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppGUI.this.dispose();
            }
        });

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(exit);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem fileFormat = new JMenuItem("File format");

        helpMenu.add(fileFormat);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);
    }

    private void setupMainGUI() {
        BorderLayout mainLayout = new BorderLayout();
        this.setLayout(mainLayout);
        JPanel inputPanel = new JPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Algorithm",createAlgorithmTab());
        tabbedPane.add("Help", createHelpTab());
        inputPanel.add(tabbedPane);

        this.add(inputPanel, BorderLayout.WEST);
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
                    graph = GraphParser.createGraphFromFile(currentFile);
                    graphVisualiser.drawNewGraph(graph);
                } catch (IncorrectFileFormatException e1) {
                } catch (IOException e1) {
                } catch (IndexOutOfBoundsException e1) {
                }
            }
    });
    filePanel.add(fileChooserField,constraints);
    panel.add(filePanel);

    constraints.gridy++;
    JLabel taskLabel = new JLabel("Select Task:");
    panel.add(taskLabel,constraints);

    JRadioButton eulerTourCheckRB = new JRadioButton(Task.EulerTourCheck.getName());
    JRadioButton euleriseGraphRB = new JRadioButton(Task.EuleriseGraph.getName());
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
    runJB.addActionListener(new RunButtonListener(taskGroup, algorithmJCB,graphVisualiser));
    constraints.gridy++;
    constraints.anchor = GridBagConstraints.CENTER;
    panel.add(runJB,constraints);

    return panel;
}

    /**
     * Create the help tab panel
     * @return JPanel
     */
    private Component createHelpTab() {
        return new JPanel();
    }
}