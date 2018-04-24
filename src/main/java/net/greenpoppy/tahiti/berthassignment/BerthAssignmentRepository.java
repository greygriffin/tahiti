package net.greenpoppy.tahiti.berthassignment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
public interface BerthAssignmentRepository
    extends CrudRepository<BerthAssignmentEntity, Integer> {

    Optional<BerthAssignmentEntity> findByBerthId(Integer berthId);

    Optional<BerthAssignmentEntity> findByBoatId(Integer boatId);

    List<BerthAssignmentEntity> findByOwnerId(Integer ownerId);
}
