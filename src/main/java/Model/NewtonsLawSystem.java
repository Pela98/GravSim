package Model


class NewtosLawSystem implements System {
    private double G=6.67430e-11; // Gravitational constant in m^3 kg^-1 s^-2
    private double minimunDistance=1e-10; // Minimum distance to avoid singularity in meters
    @override
    void update(Simulation sim){
        ComponentManager cm=sim.getComponentManager();
        int n=cm.getLastIndex();
        PositionComponent[] positions=cm.getComponent(PositionComponent.class);
        MassComponent[] masses=cm.getComponent(MassComponent.class);
        AccelerationComponent[] accelerations=cm.getComponent(AccelerationComponent.class);
    }
}