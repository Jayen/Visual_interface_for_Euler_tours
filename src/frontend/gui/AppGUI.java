package frontend.gui;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.Graph;
import com.alee.laf.WebLookAndFeel;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Main class for GUI,
 * This class sets up the GUI frontend
 *@author Jayen kumar Jaentilal k1189304
 */

public class AppGUI extends JFrame {

    public AppGUI() {

        super("Euler Tours Visual Interface");
        try {
            UIManager.setLookAndFeel(new WebLookAndFeel());
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
        }
        catch (Exception e) { }
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(400,400);
        this.setupMenuBar();
        this.setupMainGUI();
    }


    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open...");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt","text");
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(AppGUI.this);
                File file = fileChooser.getSelectedFile();
                if(file!=null && returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        Graph graph = GraphParser.createGraphFromFile(file);
                    } catch (IncorrectFileFormatException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

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

        this.add(inputPanel, BorderLayout.LINE_END);

        SimpleGraphView sgv = new SimpleGraphView(); //We create our graph in here
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Integer, String> layout = new CircleLayout(sgv.getGraph());
        layout.setSize(new Dimension(300, 300)); // sets the initial size of the space
        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        BasicVisualizationServer<Integer,String> vv =
                new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(350, 350)); //Sets the viewing area size
        this.add(vv);
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

        JLabel taskLabel = new JLabel("Select Task:");
        panel.add(taskLabel,constraints);

        JRadioButton eulerTourCheckRB = new JRadioButton("Check if Euler Tour Exists");
        JRadioButton euleriseGraphRB = new JRadioButton("Eulerise the graph");
        JRadioButton findEulerTourRB = new JRadioButton("Find Euler Tour ");

        ButtonGroup taskGroup = new ButtonGroup();
        taskGroup.add(eulerTourCheckRB);
        taskGroup.add(euleriseGraphRB);
        taskGroup.add(findEulerTourRB);

        constraints.gridy = 1;
        panel.add(taskLabel,constraints);
        constraints.gridy = 2;
        panel.add(eulerTourCheckRB,constraints);
        constraints.gridy = 3;
        panel.add(euleriseGraphRB,constraints);
        constraints.gridy = 4;
        panel.add(findEulerTourRB,constraints);

        JPanel algorithmPanel = new JPanel();
        JLabel algorithmLabel = new JLabel("Algorithm: ");
        JComboBox<String> algorithmJCB = new JComboBox<String>();
        algorithmJCB.addItem("Fleury's Algorithm");
        algorithmJCB.addItem("Huffman code: Tree Algorithm");
        algorithmPanel.add(algorithmLabel);
        algorithmPanel.add(algorithmJCB);
        constraints.gridy = 5;
        panel.add(algorithmPanel,constraints);

        JButton runJB = new JButton("Run Task");
        constraints.gridy = 6;
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
