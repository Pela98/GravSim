package Model;

class SimulationBuilder {
    //--Attributi--
    private final ComponentManager componentManager = new ComponentManager();
    private double dt = 60.0;
    private LocalDateTime localDate;
    private LocalDateTime targetDate;
    private final Queue<SimulationSystem> preconditionsQueue = new ArrayDeque<>();
    private final Queue<SimulationSystem> causesQueue = new ArrayDeque<>();
    private final Queue<SimulationSystem> eventsQueue = new ArrayDeque<>();
    private final Queue<SimulationSystem> postconditionsQueue = new ArrayDeque<>();

    //--Metodi--

    public SimulationBuilder withTimeStep(double dt) {
        this.dt = dt;
        return this;
    }
    public SimulationBuilder withTimeConfig(LocalDateTime localDate, LocalDateTime targetDate) {
        this.localDate = localDate;
        this.targetDate = targetDate;
        return this;
    }
    public SimulationBuilder withBasicPhysicsComponents(){
        componentManager.addComponent(PositionComponent.class);
        componentManager.addComponent(VelocityComponent.class);
        componentManager.addComponent(AccelerationComponent.class);
        componentManager.addComponent(MassComponent.class);
        return this;
    }
    public SimulationBuilder withMiscComponents(){
        componentManager.addComponent(NameComponent.class);
        return this;
    }
    public SimulationBuilder withBasicPhysicsSystems(){
        this.preconditionsQueue.add(new ClearAccelerationSystem());
        this.causesQueue.add(new NewtonsLawSystem());
        this.eventsQueue.add(new EulerIntegrationSystem());
        return this;
    }
    public Simulation build() {
        return new Simulation(componentManager, dt, localDate, targetDate, preconditionsQueue, causesQueue, eventsQueue, postconditionsQueue);
    }

}

    