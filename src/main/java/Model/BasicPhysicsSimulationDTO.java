package Model;

import java.util.ArrayList;
import java.util.List;

public class BasicPhysicsSimulationDTO implements  SimulationState {
    //--Attributi--
    private final List<BasicPhysicsBodyDTO> bodies;
    //--Metodi--
    BasicPhysicsSimulationDTO(List<BasicPhysicsBodyDTO> bodies) {
        this.bodies=bodies;
    }
    public List<BasicPhysicsBodyDTO> getBodies() {return bodies;}

    @Override
    public SimulationState getLastState() {
        return this;
    }
}
