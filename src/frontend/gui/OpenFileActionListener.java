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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jayen kumar Jaentilal k1189304
 */
public class OpenFileActionListener implements ActionListener {

    AppGUI appGUI;
    GraphVisualiser graphVisualiser;

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
        File file = fileChooser.getSelectedFile();
        if(file!=null && returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                LocationFixedSparseGraph graph = GraphParser.createGraphFromFile(file);
                graphVisualiser.updateView(graph);
            } catch (IncorrectFileFormatException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
