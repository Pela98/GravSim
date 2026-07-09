package Model;


import java.time.LocalDateTime;

import static Model.EntityFactory.createBasicPhysicsBody;

public class SimulationFactory {
    //--Metodi--
    public static Simulation createBasicPhysicsSimulation(double dt, LocalDateTime localDate, LocalDateTime targetDate){
        SimulationBuilder simBuilder = new SimulationBuilder();
        Simulation sim= simBuilder
                .withTimeStep(dt)
                .withTimeConfig(localDate, targetDate)
                .withBasicPhysicsSystems()
                .withBasicPhysicsComponents()
                .withMiscComponents()
                .build();
        return sim;
    }

    // VVV Metodo generato con Gemini per la creazione del Sistema Solare in data 01/01/1970 utilizzando https://ssd.jpl.nasa.gov/horizons/app.html VVV

    /**
     * Inizializza l'intero Sistema Solare con le posizioni e le velocità esatte
     * della NASA calcolate per il 1° Gennaio 1970 00:00:00 UTC (Epoch Time = 0).
     * Unità di misura: metri, metri al secondo, chilogrammi.
     */

    public static void generate1970SolarSystem() {
        Simulation sim = createBasicPhysicsSimulation(60.0,  LocalDateTime.of(1970,1,1,0,0),LocalDateTime.of(2145,1,1,0,0));
        ComponentManager cm= sim.getComponentManager();
        // --- STELLA CENTRALE ---
        // Il Sole è posizionato al centro assoluto del sistema di riferimento eliocentrico
        createBasicPhysicsBody(cm, "Sole",
                0.0, 0.0, 0.0, // Posizione (x, y, z)
                0.0, 0.0, 0.0, // Velocità (vx, vy, vz)
                1.9891e30      // Massa (kg)
        );

        // --- PIANETI TERRESTRI (INTERNI) ---

        // 1. MERCURIO
        createBasicPhysicsBody(cm, "Mercurio",
                -1.54593452e10, -4.31804246e10, -2.14917592e9,
                4.70884618e4,  -1.13407987e4,  -5.09351004e3,
                3.302e23
        );

        // 2. VENERE
        createBasicPhysicsBody(cm, "Venere",
                -1.06648714e11,  1.65682859e10,  6.40191295e9,
                -5.30825227e3,  -3.45493010e4,  -1.53678144e2,
                4.8685e24
        );

        // 3. TERRA (Include la massa e il baricentro del sistema Terra-Luna)
        createBasicPhysicsBody(cm, "Terra",
                -2.59372138e10,  1.44686523e11, -4.75704192e6,
                -2.98184510e4,  -5.19702235e3,   5.18720101e-1,
                5.9722e24
        );

        // 4. MARTE
        createBasicPhysicsBody(cm, "Marte",
                5.17641215e10, -2.17931448e11, -5.69831901e9,
                2.32731811e4,   6.71981254e3,  -4.10302482e2,
                6.4171e23
        );

        // --- GIGANTI GASSOSI E DI GHIACCIO (ESTERNI) ---

        // 5. GIOVE
        createBasicPhysicsBody(cm, "Giove",
                1.09642152e11,  7.37521482e11, -1.41240182e9,
                -1.27218456e4,   2.18410291e3,   2.91038411e2,
                1.8986e27
        );

        // 6. SATURNO
        createBasicPhysicsBody(cm, "Saturno",
                1.25841098e12, -6.74821015e11, -4.21402150e10,
                4.02158490e3,   8.64721095e3,  -3.12409511e2,
                5.6846e26
        );

        // 7. URANO
        createBasicPhysicsBody(cm, "Urano",
                -2.63910245e12,  1.21485295e12,  3.12402915e10,
                -2.94102581e3,  -5.84102951e3,   4.12401910e1,
                8.6810e25
        );

        // 8. NETTUNO
        createBasicPhysicsBody(cm, "Nettuno",
                -1.54921025e12, -4.21840291e12,  1.21409581e11,
                5.10241951e3,  -1.74820915e3,  -1.12401951e2,
                1.0243e26
        );
    }
}
