package Model;


class NewtonsLawSystem implements SimulationSystem {

    public void update(Simulation sim){
        final double G=6.67430e-11; // Gravitational constant in m^3 kg^-1 s^-2
        final double minimunDistance=1.0e6; // Minimum distance to avoid singularity in meters
        ComponentManager cm=sim.getComponentManager();
        int n=cm.getLastIndex();
        PositionComponent[] positions=cm.getComponent(PositionComponent.class);
        MassComponent[] masses=cm.getComponent(MassComponent.class);
        AccelerationComponent[] accelerations=cm.getComponent(AccelerationComponent.class);


        for (int i=0;i<n;i++){
            if(positions[i]==null||masses[i]==null||accelerations[i]==null)
                continue;
            for (int j=0;j<n;j++){
                if (i==j) continue;
                if(positions[j]==null||masses[j]==null||accelerations[j]==null)
                    continue;
                double dx=positions[j].x-positions[i].x;
                double dy=positions[j].y-positions[i].y;
                double dz=positions[j].z-positions[i].z;
                double distanceSquared=dx*dx+dy*dy+dz*dz;
                double distance=Math.sqrt(distanceSquared);
                if (distance<minimunDistance) continue;
                double sclA=G*masses[j].mass/distanceSquared;
                accelerations[i].ax+=sclA*dx/distance;
                accelerations[i].ay+=sclA*dy/distance;
                accelerations[i].az+=sclA*dz/distance;


            }
        }
    }
}