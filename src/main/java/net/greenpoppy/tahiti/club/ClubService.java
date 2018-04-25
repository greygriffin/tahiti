package net.greenpoppy.tahiti.club;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import net.greenpoppy.tahiti.service.BaseService;


@Service
public class ClubService
    extends BaseService {

    private ClubRepository repository;

    @Autowired
    public ClubService(ClubRepository repository) {
        this.repository = repository;
    }

    public List<ClubDTO> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(ClubService::toDTO)
            .collect(Collectors.toList());
    }

    public ClubDTO getByName(String name) {
        Optional<ClubEntity> maybeClub = repository.findByName(name);
        if (maybeClub.isPresent()) {
            return toDTO(maybeClub.get());
        } else {
            return null;
        }
    }

    public ClubDTO getByAbbreviation(String abbreviation) {
        Optional<ClubEntity> maybeClub = repository.findByAbbreviation(abbreviation);
        if (maybeClub.isPresent()) {
            return toDTO(maybeClub.get());
        } else {
            return null;
        }
    }

    public ClubDTO get(Integer id) {
        Optional<ClubEntity> maybeClub = repository.findById(id);
        if (maybeClub.isPresent()) {
            return toDTO(maybeClub.get());
        } else {
            return null;
        }
    }

    // Useful for testing
    public long getCount() {
        return repository.count();
    }

    // Useful for testing
    public void deleteAll() {
        repository.deleteAll();
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public ClubDTO create(ClubDTO data)
        throws IncompleteDataException, InvalidValueException {
        validateCreate(data);
        ClubEntity newEntity = ClubEntity.builder()
            .name(data.getName())
            .abbreviation(data.getAbbreviation())
            .build();
        // TODO: Test that this works, by violating the unique constraints
        try {
            return toDTO(repository.save(newEntity));
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidValueException(ex);
        }
    }

    public ClubDTO update(int id, ClubUpdate update)
        throws NotFoundException, InvalidValueException {
        Optional<ClubEntity> maybeClub = repository.findById(id);
        if (!maybeClub.isPresent()) {
            throw new NotFoundException(id);
        } else {
            ClubEntity clubEntity = maybeClub.get();
            if (update.isEmpty()) {
                return toDTO(clubEntity);
            } else {
                validateUpdate(update);
                if (update.getName() != null) {
                    clubEntity.setName(update.getName().getValue());
                }
                if (update.getAbbreviation() != null) {
                    clubEntity.setAbbreviation(update.getAbbreviation().getValue());
                }
                // TODO: Test that this works, by violating the unique constraints
                try {
                    return toDTO(repository.save(clubEntity));
                } catch (DataIntegrityViolationException ex) {
                    throw new InvalidValueException(ex);
                }
            }
        }
    }

    private void validateCreate(ClubDTO data)
        throws IncompleteDataException, InvalidValueException {
        if ((data.getName() == null) || (data.getAbbreviation() == null)) {
            throw new IncompleteDataException();
        }
        Map<String, Object> invalidValues = new HashMap<>();
        validateMandatoryCreateValue("name", data.getName(), BaseService::isNotBlank, invalidValues);
        validateMandatoryCreateValue("abbreviation", data.getAbbreviation(), BaseService::isNotBlank, invalidValues);
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private void validateUpdate(ClubUpdate update)
        throws InvalidValueException {
        Map<String, Object> invalidValues = new HashMap<>();
        validateUpdateValue("name", update.getName(), BaseService::isNotBlank, invalidValues);
        validateUpdateValue("abbreviation", update.getAbbreviation(), BaseService::isNotBlank, invalidValues);
        if (!invalidValues.isEmpty()) {
            throw new InvalidValueException(invalidValues);
        }
    }

    private static ClubDTO toDTO(ClubEntity entity) {
        return ClubDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .abbreviation(entity.getAbbreviation())
            // don't return members, there are too many
            // don't return berths, there are too many
            .createdAt(toISO8601(entity.getCreatedAt()))
            .updatedAt(toISO8601(entity.getUpdatedAt()))
            .build();
    }
}
