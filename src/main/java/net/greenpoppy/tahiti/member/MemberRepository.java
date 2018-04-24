package net.greenpoppy.tahiti.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface MemberRepository
    extends CrudRepository<MemberEntity, Integer> {

    List<MemberEntity> findByClubId(Integer clubId);

    List<MemberEntity> findByLastName(String lastName);

    List<MemberEntity> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<MemberEntity> findByEmail(String email);
}
