package net.greenpoppy.tahiti.club;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface ClubRepository
    extends CrudRepository<ClubEntity, Integer> {

    Optional<ClubEntity> findByName(String name);

    Optional<ClubEntity> findByAbbreviation(String abbreviation);
}
