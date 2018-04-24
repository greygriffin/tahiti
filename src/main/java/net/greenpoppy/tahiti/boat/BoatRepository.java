package net.greenpoppy.tahiti.boat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface BoatRepository
    extends CrudRepository<BoatEntity, Integer> {

    List<BoatEntity> findByOwnerId(Integer ownerId);

    List<BoatEntity> findByName(String name);

    Optional<BoatEntity> findByRegistrationNumber(String registrationNumber);
}
