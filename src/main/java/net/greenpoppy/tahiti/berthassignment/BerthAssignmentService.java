package net.greenpoppy.tahiti.berthassignment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import net.greenpoppy.tahiti.berth.BerthEntity;
import net.greenpoppy.tahiti.boat.BoatEntity;
import net.greenpoppy.tahiti.member.MemberEntity;
import net.greenpoppy.tahiti.service.BaseService;
import net.greenpoppy.tahiti.service.Update;


@Service
public class BerthAssignmentService
    extends BaseService {

    private final EntityManager entityManager;
    private final BerthAssignmentRepository repository;

    @Autowired
    public BerthAssignmentService(EntityManager entityManager, BerthAssignmentRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public List<BerthAssignmentDTO> getAll() {
        return getStream(repository.findAll())
            .map(BerthAssignmentService::toDTO)
            .collect(Collectors.toList());
    }

    public BerthAssignmentDTO getByBoatId(Integer boatId) {
        Optional<BerthAssignmentEntity> maybeBerthAssignment = repository.findByBoatId(boatId);
        if (maybeBerthAssignment.isPresent()) {
            return toDTO(maybeBerthAssignment.get());
        } else {
            return null;
        }
    }

    public BerthAssignmentDTO getByBerthId(Integer berthId) {
        Optional<BerthAssignmentEntity> maybeBerthAssignment = repository.findByBerthId(berthId);
        if (maybeBerthAssignment.isPresent()) {
            return toDTO(maybeBerthAssignment.get());
        } else {
            return null;
        }
    }

    public List<BerthAssignmentDTO> getByOwnerId(Integer ownerId) {
        return repository.findByOwnerId(ownerId).stream()
            .map(BerthAssignmentService::toDTO)
            .collect(Collectors.toList());
    }

    public BerthAssignmentDTO get(Integer id) {
        Optional<BerthAssignmentEntity> maybeBerthAssignment = repository.findById(id);
        if (maybeBerthAssignment.isPresent()) {
            return toDTO(maybeBerthAssignment.get());
        } else {
            return null;
        }
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public BerthAssignmentDTO create(BerthAssignmentDTO data)
        throws IncompleteDataException, InvalidValueException {
        validateCreate(data);
        BerthAssignmentEntity.BerthAssignmentEntityBuilder builder = BerthAssignmentEntity.builder()
            .berth(entityManager.getReference(BerthEntity.class, data.getBerthId()))
            .owner(entityManager.getReference(MemberEntity.class, data.getOwnerId()))
            .guardDuty(data.getGuardDuty());
        if (data.getBoatId() != null) {
            builder = builder.boat(entityManager.getReference(BoatEntity.class, data.getBoatId()));
        }
        BerthAssignmentEntity newEntity = builder.build();
        // TODO: Test that this works, by violating the unique constraints
        try {
            return toDTO(repository.save(newEntity));
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidValueException(ex);
        }
    }

    public BerthAssignmentDTO update(int id, BerthAssignmentUpdate update)
        throws NotFoundException, InvalidValueException {
        Optional<BerthAssignmentEntity> maybeBerthAssignment = repository.findById(id);
        if (!maybeBerthAssignment.isPresent()) {
            throw new NotFoundException(id);
        } else {
            BerthAssignmentEntity berthAssignmentEntity = maybeBerthAssignment.get();
            if (update.isEmpty()) {
                return toDTO(berthAssignmentEntity);
            } else {
                validateUpdate(update);
                if (update.getBoatId() != null) {
                    berthAssignmentEntity.setBoat(entityManager.getReference(BoatEntity.class, update.getBoatId().getValue()));
                }
                if (update.getGuardDuty() != null) {
                    berthAssignmentEntity.setGuardDuty(update.getGuardDuty().getValue());
                }
                // TODO: Test that this works, by violating the unique constraints
                try {
                    return toDTO(repository.save(berthAssignmentEntity));
                } catch (DataIntegrityViolationException ex) {
                    throw new InvalidValueException(ex);
                }
            }
        }
    }

    private void validateCreate(BerthAssignmentDTO data)
        throws IncompleteDataException, InvalidValueException {
        if ((data.getBerthId() == null) || (data.getOwnerId() == null) || (data.getGuardDuty() == null)) {
            throw new IncompleteDataException();
        }
        Map<String, Object> invalidValues = new HashMap<>();
        validateMandatoryCreateValue("berthId", data.getBerthId(), BaseService::isNotNull, invalidValues);
        validateMandatoryCreateValue("ownerId", data.getOwnerId(), BaseService::isNotNull, invalidValues);
        validateMandatoryCreateValue("guardDuty", data.getGuardDuty(), BaseService::isNotNull, invalidValues);
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private void validateUpdate(BerthAssignmentUpdate update)
        throws InvalidValueException {
        Map<String, Object> invalidValues = new HashMap<>();
        // boatId can be not present or null, nothing to validate
        Update<Boolean> guardDutyUpdate = update.getGuardDuty();
        if (guardDutyUpdate != null) {
            Boolean guardDuty = guardDutyUpdate.getValue();
            if (guardDuty == null) {
                invalidValues.put("guardDuty", null);
            }
        }
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private static BerthAssignmentDTO toDTO(BerthAssignmentEntity entity) {
        BerthAssignmentDTO.BerthAssignmentDTOBuilder builder = BerthAssignmentDTO.builder()
            .id(entity.getId())
            .berthId(entity.getBerth().getId())
            .ownerId(entity.getOwner().getId())
            .guardDuty(entity.getGuardDuty());
        BoatEntity boatEntity = entity.getBoat();
        if (boatEntity != null) {
            builder = builder.boatId(boatEntity.getId());
        }
        return builder.build();
    }
}
