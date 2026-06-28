package Model;


class EulerIntegrationSystem implements SimulationSystem {
    public void update(Simulation sim){
        ComponentManager cm=sim.getComponentManager();
        int n=cm.getLastIndex();
        PositionComponent[] positions=cm.getComponent(PositionComponent.class);
        VelocityComponent[] velocities=cm.getComponent(VelocityComponent.class);
        AccelerationComponent[] accelerations=cm.getComponent(AccelerationComponent.class);
        double dt=sim.getTimestep();
        for (int i=0;i<n;i++){
            velocities[i].vx+=accelerations[i].ax*dt;
            velocities[i].vy+=accelerations[i].ay*dt;
            velocities[i].vz+=accelerations[i].az*dt;
            positions[i].x+=velocities[i].vx*dt;
            positions[i].y+=velocities[i].vy*dt;
            positions[i].z+=velocities[i].vz*dt;
        }
    }
}