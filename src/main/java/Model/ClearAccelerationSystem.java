package Model

class ClearAccelerationSystem implements SimulationSystem {
    void update(Simulation sim){
        ComponentManager cm = sim.getComponentManager();
        AccelerationComponent[] accelerations = cm.getComponents(AccelerationComponent.class);
        int n= cm.getLastIndex();
        for(int i=0; i<n; i++){
            accelerations[i].ax = 0;
            accelerations[i].ay = 0;
            accelerations[i].az = 0;
    }
}