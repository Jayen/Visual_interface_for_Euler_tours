package frontend.gui;

import backend.fileparser.GraphParser;
import backend.fileparser.IncorrectFileFormatException;
import backend.internalgraph.LocationFixedSparseGraph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * @author Jayen kumar Jaentilal k1189304
 */
public class OpenFileActionListener implements ActionListener {

    public static File currentFile;
    private AppGUI appGUI;
    private GraphVisualiser graphVisualiser;


    public  OpenFileActionListener(AppGUI appGUI,GraphVisualiser graphVisualiser) {
        this.appGUI = appGUI;
        this.graphVisualiser = graphVisualiser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt","text");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(appGUI);
        currentFile = fileChooser.getSelectedFile();
        if(currentFile!=null && returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                LocationFixedSparseGraph graph = GraphParser.createGraphFromFile(currentFile);
                graphVisualiser.drawNewGraph(graph);
            } catch (IncorrectFileFormatException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
