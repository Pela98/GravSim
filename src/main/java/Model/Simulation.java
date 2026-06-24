package Model;

import java.time.LocalDateTime;

public class Simulation {
    //---Attribiti---

    //Motore dei Dati
    private final ComponentManager manager;
    //Quanto Temporale
    private double dt;
    //Data per la visualizzazione
    private LocalDateTime localDate;
    //Data per calcolo di arresto
    private LocalDateTime targetDate;
    //Tempo utilizzato dal motore fisico
    private double TotalElapsedTime;
    private double targetTime;

    //Code di Esecuzione dei Sistemi
    private final Queue<SimulationSystem> precoonditionsQueue;    //Coda di Sistemi da eseguire per primi
    private final Queue<SimulationSystem> causesQueue;    //Coda dei Sistemi che causano le variazioni (forze)
    private final Queue<SimulationSystem> effectsQueue;   //Coda dei Sistemi che applicano le variazioni (spostamenti)
    private final Queue<SimulationSystem> postconditionsQueue;    //Coda dei Sistemi da eseguire per ultimi

    //---Metodi---

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

    Simulation(ComponentManager manager, double dt, LocalDateTime localDate, LocalDateTime targetDate) {
        this.manager = manager; // Riceve il manager già configurato dal Builder
        this.dt = dt;
        this.localDate = localDate;
        this.targetDate = targetDate;
        this.totalElapsedTime = 0;
        this.targetTime = 0;

        // Inizializzazione code usando ArrayDeque (performante, senza overhead)
        this.preconditionsQueue = new ArrayDeque<>();
        this.causesQueue = new ArrayDeque<>();
        this.effectsQueue = new ArrayDeque<>();
        this.postconditionsQueue = new ArrayDeque<>();
    }
    
}
