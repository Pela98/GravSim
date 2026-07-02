package Model;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Queue;

public class Simulation implements Runnable {
    //---Attribiti---

    boolean running = false;
    //Motore dei Dati
    private final ComponentManager manager;
    //Quanto Temporale
    private double dt;
    //Data per la visualizzazione
    private LocalDateTime localDate;
    //Data per calcolo di arresto
    private LocalDateTime targetDate;
    //Tempo utilizzato dal motore fisico
    private double totalElapsedTime;
    private double targetTime;

    //Code di Esecuzione dei Sistemi
    private final Queue<SimulationSystem> preconditionsQueue;    //Coda di Sistemi da eseguire per primi
    private final Queue<SimulationSystem> causesQueue;    //Coda dei Sistemi che causano le variazioni (forze)
    private final Queue<SimulationSystem> effectsQueue;   //Coda dei Sistemi che applicano le variazioni (spostamenti)
    private final Queue<SimulationSystem> postconditionsQueue;    //Coda dei Sistemi da eseguire per ultimi

    //---Metodi---

    private double secondsBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toSeconds();
    }
    private void executeQueue(Queue<SimulationSystem> queue) {
        for (SimulationSystem sys : queue) {
            sys.update(this);
        }
    }
    public double getTimestep(){ return dt;}
    public ComponentManager getComponentManager() {return this.manager;}

    public void addPrecondition(SimulationSystem system) {
        preconditionsQueue.add(system);
    }
    public void addCause(SimulationSystem system) {
        causesQueue.add(system);
    }
    public void addEffect(SimulationSystem system) {
        effectsQueue.add(system);
    }
    public void addPostcondition(SimulationSystem system) {
        postconditionsQueue.add(system);
    }
    public void run(){
        this.running = true;
        totalElapsedTime = 0;
        targetTime = secondsBetween(localDate, targetDate);

        while (running && totalElapsedTime < targetTime && !Thread.currentThread().isInterrupted()) {
            executeQueue(preconditionsQueue);
            executeQueue(causesQueue);
            executeQueue(effectsQueue);
            executeQueue(postconditionsQueue);
            totalElapsedTime += dt;
        }
       localDate = localDate.plusSeconds((long)totalElapsedTime);

    }

    Simulation(ComponentManager manager, double dt, LocalDateTime localDate, LocalDateTime targetDate,
               Queue<SimulationSystem> preconditionsQueue, Queue<SimulationSystem> causesQueue,
               Queue<SimulationSystem> effectsQueue, Queue<SimulationSystem> postconditionsQueue) {
        this.manager = manager;
        this.dt = dt;
        this.localDate = localDate;
        this.targetDate = targetDate;
        this.totalElapsedTime = 0;
        this.targetTime = 0;
        this.preconditionsQueue = preconditionsQueue;
        this.causesQueue = causesQueue;
        this.effectsQueue = effectsQueue;
        this.postconditionsQueue = postconditionsQueue;

        
    }
    
}
