package net.greenpoppy.tahiti.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.greenpoppy.tahiti.berthassignment.BerthAssignmentEntity;
import net.greenpoppy.tahiti.boat.BoatEntity;
import net.greenpoppy.tahiti.club.ClubEntity;
import net.greenpoppy.tahiti.service.BaseService;


@Service
public class MemberService
    extends BaseService {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final EntityManager entityManager;
    private final MemberRepository repository;

    @Autowired
    public MemberService(EntityManager entityManager, MemberRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public List<MemberDTO> getAll() {
        return getStream(repository.findAll())
            .map(MemberService::toDTO)
            .collect(Collectors.toList());
    }

    public List<MemberDTO> getByClubId(Integer ownerId) {
        return repository.findByClubId(ownerId).stream()
            .map(MemberService::toDTO)
            .collect(Collectors.toList());
    }

    public List<MemberDTO> getByLastName(String lastName) {
        return repository.findByLastName(lastName).stream()
            .map(MemberService::toDTO)
            .collect(Collectors.toList());
    }

    public List<MemberDTO> getByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findByFirstNameAndLastName(firstName, lastName).stream()
            .map(MemberService::toDTO)
            .collect(Collectors.toList());
    }

    public MemberDTO getByEmail(String email) {
        Optional<MemberEntity> maybeMember = repository.findByEmail(email);
        if (maybeMember.isPresent()) {
            return toDTO(maybeMember.get());
        } else {
            return null;
        }
    }

    public MemberDTO get(Integer id) {
        Optional<MemberEntity> maybeMember = repository.findById(id);
        if (maybeMember.isPresent()) {
            return toDTO(maybeMember.get());
        } else {
            return null;
        }
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public MemberDTO create(Integer clubId, MemberDTO data)
        throws IncompleteDataException, InvalidValueException {
        validateCreate(clubId, data);
        MemberEntity newEntity = MemberEntity.builder()
            .firstName(data.getFirstName())
            .lastName(data.getLastName())
            .streetAddress(data.getStreetAddress())
            .postCode(data.getPostCode())
            .city(data.getCity())
            .country(data.getCountry())
            .phoneNumber(data.getPhoneNumber())
            .email(data.getEmail())
            .password(passwordEncoder.encode(data.getPassword()))
            .club(entityManager.getReference(ClubEntity.class, clubId))
            .build();
        // TODO: Test that this works, by violating the unique constraints
        try {
            return toDTO(repository.save(newEntity));
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidValueException(ex);
        }
    }

    public MemberDTO update(int id, MemberUpdate update)
        throws NotFoundException, InvalidValueException {
        Optional<MemberEntity> maybeMember = repository.findById(id);
        if (!maybeMember.isPresent()) {
            throw new NotFoundException(id);
        } else {
            MemberEntity memberEntity = maybeMember.get();
            if (update.isEmpty()) {
                return toDTO(memberEntity);
            } else {
                validateUpdate(update);
                if (update.getFirstName() != null) {
                    memberEntity.setFirstName(update.getFirstName().getValue());
                }
                if (update.getLastName() != null) {
                    memberEntity.setLastName(update.getLastName().getValue());
                }
                if (update.getStreetAddress() != null) {
                    memberEntity.setStreetAddress(update.getStreetAddress().getValue());
                }
                if (update.getPostCode() != null) {
                    memberEntity.setPostCode(update.getPostCode().getValue());
                }
                if (update.getCity() != null) {
                    memberEntity.setCity(update.getCity().getValue());
                }
                if (update.getCountry() != null) {
                    memberEntity.setCountry(update.getCountry().getValue());
                }
                if (update.getPhoneNumber() != null) {
                    memberEntity.setPhoneNumber(update.getPhoneNumber().getValue());
                }
                if (update.getEmail() != null) {
                    memberEntity.setEmail(update.getEmail().getValue());
                }
                if (update.getPassword() != null) {
                    memberEntity.setPassword(passwordEncoder.encode(update.getPassword().getValue()));
                }
                if (update.getClubId() != null) {
                    memberEntity.setClub(entityManager.getReference(ClubEntity.class, update.getClubId().getValue()));
                }
                // TODO: Test that this works, by violating the unique constraints
                try {
                    return toDTO(repository.save(memberEntity));
                } catch (DataIntegrityViolationException ex) {
                    throw new InvalidValueException(ex);
                }
            }
        }
    }

    public boolean passwordMatches(String email, String password) {
        Optional<MemberEntity> maybeMember = repository.findByEmail(email);
        return maybeMember.isPresent() && passwordEncoder.matches(password, maybeMember.get().getPassword());
    }

    private void validateCreate(Integer clubId, MemberDTO data)
        throws IncompleteDataException, InvalidValueException {
        if (clubId == null) {
            throw new IncompleteDataException();
        }
        Map<String, Object> invalidValues = new HashMap<>();
        validateMandatoryCreateValue("firstName", data.getFirstName(), BaseService::isNotBlank, invalidValues);
        validateMandatoryCreateValue("lastName", data.getLastName(), BaseService::isNotBlank, invalidValues);
        validateOptionalCreateValue("streetAddress", data.getStreetAddress(), BaseService::isNotBlank, invalidValues);
        validateOptionalCreateValue("postCode", data.getPostCode(), BaseService::isNotBlank, invalidValues);
        validateOptionalCreateValue("city", data.getCity(), BaseService::isNotBlank, invalidValues);
        validateOptionalCreateValue("country", data.getCountry(), BaseService::isNotBlank, invalidValues);
        validateOptionalCreateValue("phoneNumber", data.getPhoneNumber(), BaseService::isNotBlank, invalidValues);
        validateMandatoryCreateValue("email", data.getEmail(), BaseService::isNotBlank, invalidValues);
        validateMandatoryCreateValue("password", data.getPassword(), BaseService::isNotBlank, invalidValues);
        // database can check if owner exists
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private void validateUpdate(MemberUpdate update)
        throws InvalidValueException {
        Map<String, Object> invalidValues = new HashMap<>();
        //TODO: What about values which can be null but can't be blank?
        validateUpdateValue("firstName", update.getFirstName(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("lastName", update.getLastName(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("streetAddress", update.getStreetAddress(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("postCode", update.getPostCode(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("city", update.getCity(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("country", update.getCountry(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("phoneNumber", update.getPhoneNumber(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("email", update.getEmail(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("password", update.getPassword(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("clubId", update.getClubId(), BaseService::isNotNull, invalidValues);
        // database can check if owner exists
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private static MemberDTO toDTO(MemberEntity entity) {
        return MemberDTO.builder()
            .id(entity.getId())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .streetAddress(entity.getStreetAddress())
            .postCode(entity.getPostCode())
            .city(entity.getCity())
            .country(entity.getCountry())
            .phoneNumber(entity.getPhoneNumber())
            .email(entity.getEmail())
            // leave password null
            .clubId(entity.getClub().getId())
            .boatIds(entity.getBoats().stream().map(BoatEntity::getId).collect(Collectors.toList()))
            .berthAssignmentIds(entity.getBerthAssignments().stream().map(BerthAssignmentEntity::getId).collect(Collectors.toList()))
            .createdAt(toISO8601(entity.getCreatedAt()))
            .updatedAt(toISO8601(entity.getUpdatedAt()))
            .build();
    }
}
