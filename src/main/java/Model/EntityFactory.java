package Model;


class EntityFactory {
    public void basicPhysicsBody(ComponentManger cm, String name, double x, double y, double z, double vx, double vy, double vz, double mass) {
        PositionComponent[] positions = cm.getComponent(PositionComponent.class);
        VelocityComponent[] velocities = cm.getComponent(VelocityComponent.class);
        MassComponent[] masses = cm.getComponent(MassComponent.class);
        NameComponent[] names = cm.getComponent(NameComponent.class);

        
    }
}