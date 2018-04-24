package net.greenpoppy.tahiti.berth;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import net.greenpoppy.tahiti.service.BaseService;
import net.greenpoppy.tahiti.resource.BaseResource;


@RestController
public class BerthResource
    extends BaseResource {

    private BerthService service;

    @Autowired
    public BerthResource(BerthService service) {
        this.service = service;
    }

    @GetMapping(value = API_PREFIX + "/berth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<BerthDTO> getAll() {
        return service.getAll();
    }

    @GetMapping(value = API_PREFIX + "/club/{clubId}/berth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<BerthDTO> getByClub(@PathVariable Integer clubId) {
        return service.getByClubId(clubId);
    }

    @GetMapping(value = API_PREFIX + "/berth/{berthId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BerthDTO get(@PathVariable int berthId) {
        BerthDTO dto = service.get(berthId);
        if (dto == null) {
            throw createNotFoundResourceException("Berth", berthId);
        } else {
            return dto;
        }
    }

    @DeleteMapping(value = API_PREFIX + "/berth/{berthId}")
    public void delete(@PathVariable int berthId) {
        service.delete(berthId);
    }

    @PostMapping(value = API_PREFIX + "/club/{clubId}/berth",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BerthDTO create(@PathVariable Integer clubId, @Valid @RequestBody BerthDTO berthData)  {
        try {
            return service.create(berthData);
        } catch (BaseService.IncompleteDataException ex) {
            throw createIncompleteDataResourceException(ex);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }

    @PatchMapping(value = API_PREFIX + "/berth/{berthId}",
        consumes = APPLICATION_MERGE_PATCH_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BerthDTO update(@PathVariable int berthId, @RequestBody Map<String, Object> updateData) {
        BerthUpdate update = new BerthUpdate(
            getStringUpdate(updateData, "name"),
            getIntegerUpdate(updateData, "clubId"));
        try {
            return service.update(berthId, update);
        } catch (BaseService.NotFoundException ex) {
            throw createNotFoundResourceException("Berth", berthId);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }
}
