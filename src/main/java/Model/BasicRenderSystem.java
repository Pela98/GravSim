package Model;

import java.util.ArrayList;
import java.util.List;

public class BasicRenderSystem implements SimulationSystem{
    //Attributo usato per definire quanti Frame al secondo cattura la view
    private final long frequency=60;

    private long lastRenderTime= 0;

    @Override
    public void update(Simulation sim) {
        long currentTime = System.currentTimeMillis();
        long renderIntervalInMs = 1000 / frequency;
        if(currentTime - lastRenderTime >= renderIntervalInMs){
            lastRenderTime = currentTime;
            executeRender(sim);
        }

    }


    private void executeRender(Simulation sim){
        PositionComponent[] positions= sim.getComponentManager().getComponent(PositionComponent.class);
        VelocityComponent[] velocities= sim.getComponentManager().getComponent(VelocityComponent.class);
        MassComponent[] masses= sim.getComponentManager().getComponent(MassComponent.class);
        NameComponent[] names= sim.getComponentManager().getComponent(NameComponent.class);
        AccelerationComponent[] accelerations= sim.getComponentManager().getComponent(AccelerationComponent.class);
        List<BasicPhysicsBodyDTO> bodies= new ArrayList<BasicPhysicsBodyDTO>();
        for(int i=0; i<sim.getComponentManager().getLastIndex(); i++){
            String name=names[i].name;
            double mass=masses[i].mass;
            double positionX=positions[i].x;
            double positionY=positions[i].y;
            double positionZ=positions[i].z;
            double velocityX=velocities[i].vx;
            double velocityY=velocities[i].vy;
            double velocityZ=velocities[i].vz;
            double accelerationX=accelerations[i].ax;
            double accelerationY=accelerations[i].ay;
            double accelerationZ=accelerations[i].az;
            bodies.add(new BasicPhysicsBodyDTO(name, mass, positionX, positionY, positionZ, velocityX, velocityY, velocityZ, accelerationX, accelerationY, accelerationZ));
        }
        sim.lastState= new BasicPhysicsSimulationDTO(bodies);
    }
}
