package Model;

public class BasicPhysicsBodyDTO {
    //--Attributi--
    private final String name;
    private final double mass;
    private final double positionX;
    private final double positionY;
    private final double positionZ;
    private final double velocityX;
    private final double velocityY;
    private final double velocityZ;
    private final double accelX;
    private final double accelY;
    private final double accelZ;

    //--Metodi--


    public BasicPhysicsBodyDTO(String name, double mass, double positionX, double positionY, double positionZ, double velocityX, double velocityY, double velocityZ, double accelX, double accelY, double accelZ) {
        this.name = name;
        this.mass = mass;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.accelX = accelX;
        this.accelY = accelY;
        this.accelZ = accelZ;
    }

    public String getName() {return name;}
    public double getMass() {return mass;}
    public double getPositionX() {return positionX;}
    public double getPositionY() {return positionY;}
    public double getPositionZ() {return positionZ;}
    public double getVelocityX() {return velocityX;}
    public double getVelocityY() {return velocityY;}
    public double getVelocityZ() {return velocityZ;}
    public double getAccelX() {return accelX;}
    public double getAccelY() {return accelY;}
    public double getAccelZ() {return accelZ;}
}
