package Model;


class NewtonsLawSystem implements SimulationSystem {
    private double G=6.67430e-11; // Gravitational constant in m^3 kg^-1 s^-2
    private double minimunDistance=1e-10; // Minimum distance to avoid singularity in meters
    @override
    void update(Simulation sim){
        ComponentManager cm=sim.getComponentManager();
        int n=cm.getLastIndex();
        PositionComponent[] positions=cm.getComponent(PositionComponent.class);
        MassComponent[] masses=cm.getComponent(MassComponent.class);
        AccelerationComponent[] accelerations=cm.getComponent(AccelerationComponent.class);
       /* for (int i=0;i<n;i++){   meglio un clearSystem() che azzera tutti i valori di accelerazione 
            accelerations[i].ax=0;
            accelerations[i].ay=0;
            accelerations[i].az=0;
        */
        }
        for (int i=0;i<n;i++){
            for (int j=0;j<n;j++){
                if (i==j) continue; // Skip self-interaction
                double dx=positions[j].x-positions[i].x;
                double dy=positions[j].y-positions[i].y;
                double dz=positions[j].z-positions[i].z;
                double distanceSquared=dx*dx+dy*dy+dz*dz;
                double distance=Math.sqrt(distanceSquared);
                if (distance<minimunDistance) continue; // Skip if too close to avoid singularity
                double sclA=G*masses[j].mass/distanceSquared;
                accelerations[i].ax+=sclA*dx/distance;
                accelerations[i].ay+=sclA*dy/distance;
                accelerations[i].az+=sclA*dz/distance;


            })
        })
    }
}