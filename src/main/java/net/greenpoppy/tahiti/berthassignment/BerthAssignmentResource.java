package net.greenpoppy.tahiti.berthassignment;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import net.greenpoppy.tahiti.service.BaseService;
import net.greenpoppy.tahiti.resource.BaseResource;


@RestController
public class BerthAssignmentResource
    extends BaseResource {

    private BerthAssignmentService service;

    @Autowired
    public BerthAssignmentResource(BerthAssignmentService service) {
        this.service = service;
    }

    @GetMapping(value = API_PREFIX + "/berth-assignments", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<BerthAssignmentDTO> getAll() {
        return service.getAll();
    }

    @GetMapping(value = API_PREFIX + "/berths/{berthId}/berth-assignment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BerthAssignmentDTO getByBerth(@PathVariable Integer berthId) {
        return service.getByBerthId(berthId);
    }

    @GetMapping(value = API_PREFIX + "/boats/{boatId}/berth-assignment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BerthAssignmentDTO getByBoat(@PathVariable Integer boatId) {
        return service.getByBoatId(boatId);
    }

    @GetMapping(value = API_PREFIX + "/members/{memberId}/berth-assignments", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<BerthAssignmentDTO> getByOwner(@PathVariable Integer memberId) {
        return service.getByOwnerId(memberId);
    }

    @GetMapping(value = API_PREFIX + "/berth-assignments/{berthAssignmentId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BerthAssignmentDTO get(@PathVariable int berthAssignmentId) {
        BerthAssignmentDTO dto = service.get(berthAssignmentId);
        if (dto == null) {
            throw createNotFoundResourceException("BerthAssignment", berthAssignmentId);
        } else {
            return dto;
        }
    }

    @DeleteMapping(value = API_PREFIX + "/berth-assignments/{berthAssignmentId}")
    public void delete(@PathVariable int berthAssignmentId) {
        service.delete(berthAssignmentId);
    }

    @PostMapping(value = "/berth-assignments",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BerthAssignmentDTO create(@Valid @RequestBody BerthAssignmentDTO berthAssignmentData)  {
        try {
            return service.create(berthAssignmentData);
        } catch (BaseService.IncompleteDataException ex) {
            throw createIncompleteDataResourceException(ex);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }

    @PatchMapping(value = API_PREFIX + "/berth-assignments/{berthAssignmentId}",
        consumes = APPLICATION_MERGE_PATCH_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BerthAssignmentDTO update(@PathVariable int berthAssignmentId, @RequestBody Map<String, Object> updateData) {
        BerthAssignmentUpdate update = new BerthAssignmentUpdate(
            getIntegerUpdate(updateData, "boatId"),
            getBooleanUpdate(updateData, "guardDuty"));
        try {
            return service.update(berthAssignmentId, update);
        } catch (BaseService.NotFoundException ex) {
            throw createNotFoundResourceException("BerthAssignment", berthAssignmentId);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }
}
