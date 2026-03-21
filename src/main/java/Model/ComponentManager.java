package Model;

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.HashMap;

public final class ComponentManager {
    // Storage mappa ciascuno degli Array dei Component al nome del Component concreto
    private HashMap<Class<? extends Component>,Component[]> storage = new HashMap<>();
    // size e lastIndex servono per riallocare l'array quando è pieno assicurandosi di tenere i dati sempre in posizioni contigue in memoria
    private int size = 10;
    private int lastIndex = 0;


    // Utilizzati per verificare se gli array sono pieni o quasi vuoti
    private boolean isFull() {return lastIndex >= size;}
    private boolean isScarce() {
        return lastIndex <= size/3;
    }
    // Utilizzato per allocare più memoria
    private void grow(){
        int newSize = size * 2;
        // Per ogni array contenuto nello storage creiamo una copia con spazio raddoppiato
        resize(newSize);
    }
    // Utilizzato per riallocare gli array
    private void resize(int newSize) {
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
        resize(newSize);
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

    // Metodo per aggiungere Entità
    public int addEntity(Component... components) {
        if (isFull())
            grow();
        int entityId = lastIndex;
        for (Component component : components) {
            //Usiamo la classe del componente per trovare l'array in cui inserirlo
            Class<? extends Component> componentClass = component.getClass();
            Component[] array = storage.get(componentClass);
            if (array != null) {
                //inseriamo il componente nell'array alla posizione della nuova entità
                array[entityId] = component;
            }
            else {
                // Se entriamo in questo stato significa che il component non è stato registrato con addComponent
                throw new IllegalStateException("Component not registered");
            }

        }
        lastIndex++;
        return entityId;
    }

    //Metodo per rimuovere Entità con metodo Swap & Pop
    public void deleteEntity(int entityId) {
        int lastEntityId = lastIndex - 1;
        for (Component[] array : storage.values()) {
            // Swap
            array[entityId] = array[lastEntityId];
            // Pop
            array[lastEntityId] = null;
        }
        lastIndex--;
        if (isScarce())
            shrink();
    }


}
