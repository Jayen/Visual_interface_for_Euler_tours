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
public class OpenFileActionListener implements ActionListener, Observable{

    List<Observer> observers;
    private boolean changed;
    AppGUI appGUI;

    public  OpenFileActionListener(AppGUI appGUI) {
        this.appGUI = appGUI;
        this.changed = false;
        this.observers = new ArrayList<Observer>();
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
//                createGraphView(graph);
                changed = true;
                this.notifyObservers();
            } catch (IncorrectFileFormatException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void register(Observer observer) {
        if(observer!=null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        if(changed) {
            for(int i=0; i<observers.size(); i++) {
                observers.get(i).update();
            }
        }
    }

    @Override
    public Object getUpdate(Observer observer) {
        return null;
    }
}
