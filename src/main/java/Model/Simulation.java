package Model;

import java.time.LocalDateTime;

public class Simulation {
    //Motore dei Dati
    private final ComponentManager manager;
    //Quanto Temporale
    private double dt;
    //Data per la visualizzazione
    private LocalDateTime localDate;
    //Data per calcolo di arresto
    private LocalDateTime targetDate;
    //Tempo utilizzato dal motore fisico (aggiornamento più veloce)
    private double TotalElapsedTime;
    private double targetTime;
    public Simulation(ComponentManager manager, double dt, LocalDateTime localDate) {
        this.manager = manager;
        this.dt = dt;
        this.localDate = localDate;
        this.targetDate = localDate.plusDays(1);
    }
}
