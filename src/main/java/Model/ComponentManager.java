package Model;

import java.util.Arrays;
import java.util.HashMap;

public final class ComponentManager {
    // Storage mappa ciascuno degli Array dei Component al nome del Component concreto
    private HashMap<Class<? extends Component>,Component[]> storage = new HashMap<>();
    // size e lastIndex servono per riallocare l'array quando è pieno assicurandosi di tenere i dati sempre in posizioni contigue in memoria
    private int size;
    private int lastIndex = 0;


    // Utilizzati per verificare se gli array sono pieni o quasi vuoti
    private Boolean isFull() {
        return lastIndex >= size;
    }
    private Boolean isScarce() {
        return lastIndex <= size/3;
    }
    // Utilizzato per allocare più memoria
    private void grow(){
        int newSize = size * 2;
        // Per ogni array contenuto nello storage creiamo una copia con spazio raddoppiato
        for (HashMap.Entry<Class<? extends Component>, Component[]> entry : storage.entrySet()){
            Component[] oldArray = entry.getValue();
            Component[] newArray = Arrays.copyOf(oldArray, newSize);
            entry.setValue(newArray);
        }
        this.size = newSize;
    }
    // Utilizzato per rimuovere spazio inutilizzato
    private void shrink(){
        int newSize = size / 2;
        // Per ogni array contenuto nello storage creiamo una copia con spazio ridotto
        for (HashMap.Entry<Class<? extends Component>, Component[]> entry : storage.entrySet()){
            Component[] oldArray = entry.getValue();
            Component[] newArray = Arrays.copyOf(oldArray, newSize);
            entry.setValue(newArray);
        }
    }


    // Questo metodo permette di aggiungere nuovi component senza dover apportare modifiche alla classe
    public <T extends Component> void addComponent(Class<T> component) {
        Component[] newComponent = new Component[this.size];
        storage.put(component, newComponent);
    }

    // Questo metodo viene utilizzato dai System per operare direttamente sui dati (Accesso Veloce)
    @SuppressWarnings("unchecked")
    <T extends Component> T[] getComponent(Class<T> component) {
        return (T[]) storage.get(component);
    }
    
    // Metodo sicuro per le classi esterne al pacchetto
    public <T extends Component> T[] getSafeComponent(Class<T> component) {
        T[] original = getComponent(component);
        if (original == null) {
            return null;
        }
        return java.util.Arrays.copyOf(original, original.length);
    }



}
