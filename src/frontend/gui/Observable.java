package frontend.gui;

/**
 * @author Jayen kumar Jaentilal k1189304
 */
public interface Observable {

    public void register(Observer observer);

    public void unregister(Observer observer);

    public void notifyObservers();

    public Object getUpdate(Observer observer);
}
