package Persistence;

import Model.ComponentManager;
import Model.EntityFactory;
import Model.PositionComponent;
import Model.VelocityComponent;
import Model.MassComponent;
import Model.NameComponent;

import java.sql.*;

public class SimulationDAO {

    private final String dbUrl;

    /**
     * Costruttore del DAO per SQLite.
     * @param fileName Il nome del file del database (es. "simulazioni.db")
     */
    public SimulationDAO(String fileName) {
        // La stringa di connessione indica a SQLite di creare/aprire un file locale
        this.dbUrl = "jdbc:sqlite:" + fileName;
        initDatabase();
    }

    /**
     * Crea le tabelle nel file SQLite se non sono già presenti.
     * In SQLite, usiamo AUTOINCREMENT (senza underscore) e i vincoli standard.
     */
    private void initDatabase() {
        String createSimulazioni = "CREATE TABLE IF NOT EXISTS simulazioni (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "tempo_corrente_secondi REAL NOT NULL, " +
                "dt REAL NOT NULL);";

        String createCorpi = "CREATE TABLE IF NOT EXISTS corpi (" +
                "id_corpo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "simulazione_id INTEGER NOT NULL, " +
                "entity_id INTEGER NOT NULL, " +
                "nome TEXT NOT NULL, " +
                "pos_x REAL NOT NULL, pos_y REAL NOT NULL, pos_z REAL NOT NULL, " +
                "vel_x REAL NOT NULL, vel_y REAL NOT NULL, vel_z REAL NOT NULL, " +
                "massa REAL NOT NULL, " +
                "FOREIGN KEY (simulazione_id) REFERENCES simulazioni(id) ON DELETE CASCADE);";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            
            // Abilita esplicitamente il supporto alle Foreign Key (disattivato di default in SQLite)
            stmt.execute("PRAGMA foreign_keys = ON;");
            
            stmt.execute(createSimulazioni);
            stmt.execute(createCorpi);
            
        } catch (SQLException e) {
            System.err.println("Errore durante l'inizializzazione del database SQLite: " + e.getMessage());
        }
    }

    /**
     * SALVATAGGIO: Disattiva l'autocommit per raggruppare i corpi in un'unica transazione atomica.
     * Questo velocizza la scrittura su disco in modo esponenziale.
     */
    public void saveSimulation(String nomeSimulazione, double currentTime, double dt, ComponentManager cm) {
        String insertSimSql = "INSERT INTO simulazioni (nome, tempo_corrente_secondi, dt) VALUES (?, ?, ?)";
        String insertCorpoSql = "INSERT INTO corpi (simulazione_id, entity_id, nome, pos_x, pos_y, pos_z, vel_x, vel_y, vel_z, massa) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            // Ottimizzazione cruciale per SQLite: evita di scrivere su disco a ogni singola riga
            conn.setAutoCommit(false); 

            int simulazioneId;
            
            // 1. Inserimento dei metadati della simulazione
            try (PreparedStatement pstmtSim = conn.prepareStatement(insertSimSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmtSim.setString(1, nomeSimulazione);
                pstmtSim.setDouble(2, currentTime);
                pstmtSim.setDouble(3, dt);
                pstmtSim.executeUpdate();
                
                try (ResultSet generatedKeys = pstmtSim.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        simulazioneId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Errore: Impossibile recuperare l'ID autogenerato da SQLite.");
                    }
                }
            }

            // 2. Recupero degli array paralleli dal ComponentManager dell'ECS
            PositionComponent[] positions = cm.getSafeComponent(PositionComponent.class);
            VelocityComponent[] velocities = cm.getSafeComponent(VelocityComponent.class);
            MassComponent[] masses = cm.getSafeComponent(MassComponent.class);
            NameComponent[] names = cm.getSafeComponent(NameComponent.class);

            // 3. Preparazione del Batch per salvare tutte le entità insieme
            try (PreparedStatement pstmtCorpo = conn.prepareStatement(insertCorpoSql)) {
                for (int i = 0; i < cm.getLastIndex(); i++) {
                    if (positions[i] != null && velocities[i] != null && masses[i] != null && names[i] != null) {
                        pstmtCorpo.setInt(1, simulazioneId);
                        pstmtCorpo.setInt(2, i); // Memorizza l'indice ECS originale
                        pstmtCorpo.setString(3, names[i].name);
                        
                        pstmtCorpo.setDouble(4, positions[i].x);
                        pstmtCorpo.setDouble(5, positions[i].y);
                        pstmtCorpo.setDouble(6, positions[i].z);
                        
                        pstmtCorpo.setDouble(7, velocities[i].vx);
                        pstmtCorpo.setDouble(8, velocities[i].vy);
                        pstmtCorpo.setDouble(9, velocities[i].vz);
                        
                        pstmtCorpo.setDouble(10, masses[i].mass);
                        
                        pstmtCorpo.addBatch();
                    }
                }
                pstmtCorpo.executeBatch(); // Spara il blocco di dati al file SQLite
            }

            conn.commit(); // Scrive fisicamente i dati sul file di database
            System.out.println("Simulazione '" + nomeSimulazione + "' salvata con successo nel file SQLite.");
            
        } catch (SQLException e) {
            System.err.println("Errore SQLite durante il salvataggio: " + e.getMessage());
        }
    }

    /**
     * CARICAMENTO: Legge i corpi dal file ordinandoli per entity_id, 
     * garantendo lo stesso esatto ordine di memoria nell'ECS.
     */
    public double[] loadSimulation(int simulazioneId, ComponentManager cm, EntityFactory factory) {
        String selectSimSql = "SELECT tempo_corrente_secondi, dt FROM simulazioni WHERE id = ?";
        String selectCorpiSql = "SELECT * FROM corpi WHERE simulazione_id = ? ORDER BY entity_id ASC";
        
        double[] metaData = new double[2];

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            
            // Abilita i vincoli anche in lettura per sicurezza
            stmt.execute("PRAGMA foreign_keys = ON;");
            
            // 1. Caricamento metadati temporali della simulazione
            try (PreparedStatement pstmtSim = conn.prepareStatement(selectSimSql)) {
                pstmtSim.setInt(1, simulazioneId);
                try (ResultSet rsSim = pstmtSim.executeQuery()) {
                    if (rsSim.next()) {
                        metaData[0] = rsSim.getDouble("tempo_corrente_secondi");
                        metaData[1] = rsSim.getDouble("dt");
                    } else {
                        System.err.println("Nessuna simulazione trovata nel file SQLite con ID: " + simulazioneId);
                        return null;
                    }
                }
            }

            // 2. Caricamento e ripopolamento dell'ECS tramite la Factory
            try (PreparedStatement pstmtCorpi = conn.prepareStatement(selectCorpiSql)) {
                pstmtCorpi.setInt(1, simulazioneId);
                try (ResultSet rsCorpi = pstmtCorpi.executeQuery()) {
                    
                    while (rsCorpi.next()) {
                        factory.createBasicPhysicsBody(
                            cm,
                            rsCorpi.getString("nome"),
                            rsCorpi.getDouble("pos_x"),
                            rsCorpi.getDouble("pos_y"),
                            rsCorpi.getDouble("pos_z"),
                            rsCorpi.getDouble("vel_x"),
                            rsCorpi.getDouble("vel_y"),
                            rsCorpi.getDouble("vel_z"),
                            rsCorpi.getDouble("massa")
                        );
                    }
                }
            }
            System.out.println("Simulazione ID " + simulazioneId + " caricata con successo da SQLite.");
            return metaData;

        } catch (SQLException e) {
            System.err.println("Errore SQLite durante il caricamento: " + e.getMessage());
            return null;
        }
    }
}
