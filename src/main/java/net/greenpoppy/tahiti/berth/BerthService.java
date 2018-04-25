package net.greenpoppy.tahiti.berth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import net.greenpoppy.tahiti.club.ClubEntity;
import net.greenpoppy.tahiti.service.BaseService;
import net.greenpoppy.tahiti.service.Update;


@Service
public class BerthService
    extends BaseService {

    private final EntityManager entityManager;
    private final BerthRepository repository;

    @Autowired
    public BerthService(EntityManager entityManager, BerthRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public List<BerthDTO> getAll() {
        return getStream(repository.findAll())
            .map(BerthService::toDTO)
            .collect(Collectors.toList());
    }

    public List<BerthDTO> getByClubId(Integer clubId) {
        return repository.findByClubId(clubId).stream()
            .map(BerthService::toDTO)
            .collect(Collectors.toList());
    }

    public BerthDTO getByName(String name) {
        Optional<BerthEntity> maybeBerth = repository.findByName(name);
        if (maybeBerth.isPresent()) {
            return toDTO(maybeBerth.get());
        } else {
            return null;
        }
    }

    public BerthDTO get(Integer id) {
        Optional<BerthEntity> maybeBerth = repository.findById(id);
        if (maybeBerth.isPresent()) {
            return toDTO(maybeBerth.get());
        } else {
            return null;
        }
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public BerthDTO create(BerthDTO data)
        throws IncompleteDataException, InvalidValueException {
        validateCreate(data);
        BerthEntity newEntity = BerthEntity.builder()
            .name(data.getName())
            .club(entityManager.getReference(ClubEntity.class, data.getClubId()))
            .build();
        // TODO: Test that this works, by violating the unique constraints
        try {
            return toDTO(repository.save(newEntity));
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidValueException(ex);
        }
    }

    public BerthDTO update(int id, BerthUpdate update)
        throws NotFoundException, InvalidValueException {
        Optional<BerthEntity> maybeBerth = repository.findById(id);
        if (!maybeBerth.isPresent()) {
            throw new NotFoundException(id);
        } else {
            BerthEntity berthEntity = maybeBerth.get();
            if (update.isEmpty()) {
                return toDTO(berthEntity);
            } else {
                validateUpdate(update);
                if (update.getName() != null) {
                    berthEntity.setName(update.getName().getValue());
                }
                if (update.getClubId() != null) {
                    berthEntity.setClub(entityManager.getReference(ClubEntity.class, update.getClubId().getValue()));
                }
                // TODO: Test that this works, by violating the unique constraints
                try {
                    return toDTO(repository.save(berthEntity));
                } catch (DataIntegrityViolationException ex) {
                    throw new InvalidValueException(ex);
                }
            }
        }
    }

    private void validateCreate(BerthDTO data)
        throws IncompleteDataException, InvalidValueException {
        if ((data.getName() == null) || (data.getClubId() == null)) {
            throw new IncompleteDataException();
        }
        Map<String, Object> invalidValues = new HashMap<>();
        validateMandatoryCreateValue("name", data.getName(), BaseService::isNotBlank, invalidValues);
        validateMandatoryCreateValue("clubId", data.getClubId(), BaseService::isNotNull, invalidValues);
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private void validateUpdate(BerthUpdate update)
        throws InvalidValueException {
        Map<String, Object> invalidValues = new HashMap<>();
        Update<String> nameUpdate = update.getName();
        if (nameUpdate != null) {
            String name = nameUpdate.getValue();
            if ((name == null) || isBlank(name)) {
                invalidValues.put("name", name);
            }
        }
        Update<Integer> clubIdUpdate = update.getClubId();
        if (clubIdUpdate != null) {
            Integer clubId = clubIdUpdate.getValue();
            if (clubId == null) {
                invalidValues.put("clubId", null);
            }
        }
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private static BerthDTO toDTO(BerthEntity entity) {
        return BerthDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .clubId(entity.getClub().getId())
            .berthAssignmentId(entity.getBerthAssignment() == null ? null : entity.getBerthAssignment().getId())
            .createdAt(toISO8601(entity.getCreatedAt()))
            .updatedAt(toISO8601(entity.getUpdatedAt()))
            .build();
    }
}
