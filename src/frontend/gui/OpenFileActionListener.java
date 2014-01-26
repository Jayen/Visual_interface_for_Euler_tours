package frontend.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jayen kumar Jaentilal k1189304
 */
public class OpenFileActionListener implements ActionListener, Observable{

    List<Observer> observers;

    public  OpenFileActionListener() {
        observers = new ArrayList<Observer>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void register(Observer observer) {

    }

    @Override
    public void unregister(Observer observer) {

    }

    @Override
    public void notifyObservers() {

    }

    @Override
    public Object getUpdate(Observer observer) {
        return null;
    }
}
