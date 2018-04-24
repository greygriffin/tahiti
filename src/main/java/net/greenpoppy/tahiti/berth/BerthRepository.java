package net.greenpoppy.tahiti.berth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface BerthRepository
    extends CrudRepository<BerthEntity, Integer> {

        List<BerthEntity> findByClubId(Integer clubId);

        Optional<BerthEntity> findByName(String name);
}
