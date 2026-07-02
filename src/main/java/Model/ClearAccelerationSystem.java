package Model;

class ClearAccelerationSystem implements SimulationSystem {
    public void update(Simulation sim) {
        ComponentManager cm = sim.getComponentManager();
        AccelerationComponent[] accelerations = cm.getComponent(AccelerationComponent.class);
        int n = cm.getLastIndex();
        for (int i = 0; i < n; i++) {
            if (accelerations[i]==null)
                continue;
            accelerations[i].ax = 0;
            accelerations[i].ay = 0;
            accelerations[i].az = 0;
        }
    }
}