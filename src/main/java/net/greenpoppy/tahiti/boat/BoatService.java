package net.greenpoppy.tahiti.boat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import net.greenpoppy.tahiti.member.MemberEntity;
import net.greenpoppy.tahiti.service.BaseService;


@Service
public class BoatService
    extends BaseService {

    private final EntityManager entityManager;
    private final BoatRepository repository;

    @Autowired
    public BoatService(EntityManager entityManager, BoatRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public List<BoatDTO> getAll() {
        return getStream(repository.findAll())
            .map(BoatService::toDTO)
            .collect(Collectors.toList());
    }

    public List<BoatDTO> getByOwnerId(Integer ownerId) {
        return repository.findByOwnerId(ownerId).stream()
            .map(BoatService::toDTO)
            .collect(Collectors.toList());
    }

    public List<BoatDTO> getByName(String name) {
        return repository.findByName(name).stream()
            .map(BoatService::toDTO)
            .collect(Collectors.toList());
    }

    public BoatDTO getByRegistrationNumber(String registrationNumber) {
        Optional<BoatEntity> maybeBoat = repository.findByRegistrationNumber(registrationNumber);
        if (maybeBoat.isPresent()) {
            return toDTO(maybeBoat.get());
        } else {
            return null;
        }
    }

    public BoatDTO get(Integer id) {
        Optional<BoatEntity> maybeBoat = repository.findById(id);
        if (maybeBoat.isPresent()) {
            return toDTO(maybeBoat.get());
        } else {
            return null;
        }
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public BoatDTO create(Integer ownerId, BoatDTO data)
        throws IncompleteDataException, InvalidValueException {
        validateCreate(ownerId, data);
        BoatEntity newEntity = BoatEntity.builder()
            .name(data.getName())
            .model(data.getModel())
            .registrationNumber(data.getRegistrationNumber())
            .length(data.getLength())
            .width(data.getWidth())
            .owner(entityManager.getReference(MemberEntity.class, data.getOwnerId()))
            .build();
        // TODO: Test that this works, by violating the unique constraints
        try {
            return toDTO(repository.save(newEntity));
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidValueException(ex);
        }
    }

    public BoatDTO update(int id, BoatUpdate update)
        throws NotFoundException, InvalidValueException {
        Optional<BoatEntity> maybeBoat = repository.findById(id);
        if (!maybeBoat.isPresent()) {
            throw new NotFoundException(id);
        } else {
            BoatEntity boatEntity = maybeBoat.get();
            if (update.isEmpty()) {
                return toDTO(boatEntity);
            } else {
                validateUpdate(update);
                if (update.getName() != null) {
                    boatEntity.setName(update.getName().getValue());
                }
                if (update.getOwnerId() != null) {
                    boatEntity.setOwner(entityManager.getReference(MemberEntity.class, update.getOwnerId().getValue()));
                }
                // TODO: Test that this works, by violating the unique constraints
                try {
                    return toDTO(repository.save(boatEntity));
                } catch (DataIntegrityViolationException ex) {
                    throw new InvalidValueException(ex);
                }
            }
        }
    }

    private void validateCreate(Integer ownerId, BoatDTO data)
        throws IncompleteDataException, InvalidValueException {
        if (ownerId == null) {
            throw new IncompleteDataException();
        }
        Map<String, Object> invalidValues = new HashMap<>();
        validateOptionalCreateValue("name", data.getName(), BaseService::isNotBlank, invalidValues);
        validateOptionalCreateValue("model", data.getModel(), BaseService::isNotBlank, invalidValues);
        validateOptionalCreateValue("registrationNumber", data.getRegistrationNumber(), BaseService::isNotBlank, invalidValues);
        validateOptionalCreateValue("length", data.getLength(), BaseService::isPositive, invalidValues);
        validateOptionalCreateValue("width", data.getWidth(), BaseService::isPositive, invalidValues);
        validateMandatoryCreateValue("ownerId", data.getOwnerId(), BaseService::isNotNull, invalidValues);
        // database can check if owner exists
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private void validateUpdate(BoatUpdate update)
        throws InvalidValueException {
        Map<String, Object> invalidValues = new HashMap<>();
        //TODO: What about values which can be null but can't be blank. Is there a validation framework?
        validateUpdateValue("name", update.getName(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("model", update.getModel(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("registrationNumber", update.getRegistrationNumber(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("length", update.getLength(), BaseService::isPositive, invalidValues);
        validateUpdateValue("width", update.getWidth(), BaseService::isPositive, invalidValues);
        validateUpdateValue("ownerId", update.getOwnerId(), BaseService::isNotNull, invalidValues);
        // database can check if owner exists
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private static BoatDTO toDTO(BoatEntity entity) {
        return BoatDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            //TODO: continue here with other values
            .ownerId(entity.getOwner().getId())
            .berthAssignmentId(entity.getBerthAssignment() == null ? null : entity.getBerthAssignment().getId())
            .build();
    }
}
