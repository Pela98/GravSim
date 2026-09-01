package Model;


public class EntityFactory {
    public static void createBasicPhysicsBody(Simulation sim, String name, double x, double y, double z, double vx, double vy, double vz, double mass) {
        PositionComponent position = new PositionComponent();
        position.x = x;
        position.y = y;
        position.z = z;
        NameComponent nameComponent = new NameComponent();
        nameComponent.name = name;
        VelocityComponent velocity = new VelocityComponent();
        velocity.vx = vx;
        velocity.vy = vy;
        velocity.vz = vz;
        MassComponent massComponent = new MassComponent();
        massComponent.mass = mass;

        sim.getComponentManager().addEntity(position, nameComponent, velocity, massComponent);
    }

}