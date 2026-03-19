package Model;

import java.util.HashMap;

public final class ComponentManager {
    // Storage mappa ciascuno degli Array dei Component al nome del Component concreto
    private HashMap<Class<? extends Component>,Component[]> storage = new HashMap<>();
    // maxSize e lastIndex servono per riallocare l'array quando è pieno assicurandosi di tenere i dati sempre in posizioni contigue in memoria
    private int maxSize;
    private int lastIndex = 0;
    // Questo metodo permette di aggiungere nuovi component senza dover apportare modifiche alla classe
    public <T extends Component> void addComponent(Class<T> component) {
        Component[] newComponent = new Component[this.maxSize];
        storage.put(component, newComponent);
    }
    // Questo metodo viene utilizzato dai System per operare direttamente sui dati
    @SuppressWarnings("unchecked")
    <T extends Component> T[] getComponent(Class<T> component) {
        return (T[]) storage.get(component);
    }

}
