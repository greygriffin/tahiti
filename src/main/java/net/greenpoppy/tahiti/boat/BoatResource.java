package net.greenpoppy.tahiti.boat;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import net.greenpoppy.tahiti.service.BaseService;
import net.greenpoppy.tahiti.resource.BaseResource;


@RestController
public class BoatResource
    extends BaseResource {

    private BoatService service;

    @Autowired
    public BoatResource(BoatService service) {
        this.service = service;
    }

    @GetMapping(value = API_PREFIX + "/boats", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<BoatDTO> getAll() {
        return service.getAll();
    }

    @GetMapping(value = API_PREFIX + "/members/{memberId}/boats", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<BoatDTO> getByOwner(@PathVariable Integer ownerId) {
        return service.getByOwnerId(ownerId);
    }

    @GetMapping(value = API_PREFIX + "/boats/{boatId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BoatDTO get(@PathVariable int boatId) {
        BoatDTO dto = service.get(boatId);
        if (dto == null) {
            throw createNotFoundResourceException("Boat", boatId);
        } else {
            return dto;
        }
    }

    @DeleteMapping(value = API_PREFIX + "/boats/{boatId}")
    public void delete(@PathVariable int boatId) {
        service.delete(boatId);
    }

    @PostMapping(value = API_PREFIX + "/member/{memberId}/boats",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BoatDTO create(@PathVariable Integer memberId, @Valid @RequestBody BoatDTO boatData) {
        try {
            return service.create(memberId, boatData);
        } catch (BaseService.IncompleteDataException ex) {
            throw createIncompleteDataResourceException(ex);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }

    @PatchMapping(value = API_PREFIX + "/boats/{boatId}",
        consumes = APPLICATION_MERGE_PATCH_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BoatDTO update(@PathVariable int boatId, @RequestBody Map<String, Object> updateData) {
        BoatUpdate update = new BoatUpdate(
            getStringUpdate(updateData, "name"),
            getStringUpdate(updateData, "model"),
            getStringUpdate(updateData, "registrationNumber"),
            getDoubleUpdate(updateData, "length"),
            getDoubleUpdate(updateData, "width"),
            getIntegerUpdate(updateData, "ownerId"));
        try {
            return service.update(boatId, update);
        } catch (BaseService.NotFoundException ex) {
            throw createNotFoundResourceException("Boat", boatId);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }
}
