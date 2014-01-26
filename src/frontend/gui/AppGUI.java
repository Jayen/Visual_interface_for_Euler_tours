package frontend.gui;

import backend.internalgraph.LocationFixedSparseGraph;
import backend.internalgraph.VertexLabeller;
import com.alee.laf.WebLookAndFeel;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;

/**
 * Main class for GUI,
 * This class sets up the GUI frontend
 *@author Jayen kumar Jaentilal k1189304
 */

public class AppGUI extends JFrame {

    private LocationFixedSparseGraph<String,String> graph;
    private VisualizationViewer<String,String> visualizationServer;
    private Layout<String,String> graphLayout;
    private PluggableGraphMouse graphMouse;
    private JButton runJB;

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
        this.setupMenuBar();
        this.setupMainGUI();
    }


    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open...");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");

        open.addActionListener(new OpenFileActionListener());

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
        algorithmJCB.addItem(Task.FleuryAlgorithm.getName());
        algorithmJCB.addItem(Task.HuffmanCodeTreeAlgorithm.getName());

        algorithmPanel.add(algorithmLabel);
        algorithmPanel.add(algorithmJCB);
        constraints.gridy = 5;
        panel.add(algorithmPanel,constraints);

        runJB = new JButton("Run Task");
        runJB.addActionListener(new RunButtonListener(taskGroup, algorithmJCB));
        constraints.gridy = 6;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(runJB,constraints);

        return panel;
    }

    private void createGraphView(LocationFixedSparseGraph<String,String> graph) {
        runJB.putClientProperty("graph",graph);
        Map<String,Point2D> vertexLocationsMap = graph.getVertexLocations();
        Transformer<String, Point2D> vertexLocations = TransformerUtils.mapTransformer(vertexLocationsMap);

        if(graphLayout==null) {
            graphLayout = new StaticLayout<String, String>(graph,vertexLocations);
        }
        else {
            graphLayout.setGraph(graph);
            graphLayout.setInitializer(vertexLocations);
        }
        if(visualizationServer==null) {
            visualizationServer = new VisualizationViewer<String, String>(graphLayout);
        }
        else {
            visualizationServer.setGraphLayout(graphLayout);
        }

        visualizationServer.getRenderContext().setVertexLabelTransformer(new VertexLabeller<String>(graph));
//        visualizationServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
//        visualizationServer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        visualizationServer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<String, String>());
        AbstractEdgeShapeTransformer<String,String> edgeShapeTransformer =
                (AbstractEdgeShapeTransformer<String,String>) visualizationServer.getRenderContext().getEdgeShapeTransformer();
        edgeShapeTransformer.setControlOffsetIncrement(35);

        if(graphMouse==null) {
            graphMouse = new PluggableGraphMouse();
            graphMouse.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1_MASK));
            graphMouse.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));
            visualizationServer.setGraphMouse(graphMouse);
        }
        this.add(visualizationServer);
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