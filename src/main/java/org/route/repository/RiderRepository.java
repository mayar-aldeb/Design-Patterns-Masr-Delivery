package org.route.repository;

import org.route.model.Rider;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RiderRepository {
    private final Map<Integer, Rider> riders=new HashMap<>();

    public Optional<Rider> findRiderById(int riderId){
        return Optional.ofNullable(riders.get(riderId));
    }

    public List<Rider> findAllRiders(){
        return riders.values().stream().toList();
    }

    public void addRider(Rider rider){
        riders.put(rider.getId(), rider);
    }


}
