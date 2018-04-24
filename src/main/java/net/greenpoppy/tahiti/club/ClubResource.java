package net.greenpoppy.tahiti.club;

import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.greenpoppy.tahiti.resource.BaseResource;
import net.greenpoppy.tahiti.service.BaseService;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
public class ClubResource
    extends BaseResource {

    private ClubService service;

    @Autowired
    public ClubResource(ClubService service) {
        this.service = service;
    }

    @GetMapping(value = API_PREFIX + "/clubs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClubDTO> getAll() {
        return service.getAll();
    }

    @GetMapping(value = API_PREFIX + "/clubs/{clubId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClubDTO get(@PathVariable int clubId) {
        ClubDTO dto = service.get(clubId);
        if (dto == null) {
            throw createNotFoundResourceException("Club", clubId);
        } else {
            return dto;
        }
    }

    @DeleteMapping(value = API_PREFIX + "/clubs/{clubId}")
    public void delete(@PathVariable int clubId) {
        service.delete(clubId);
    }

    @PostMapping(value = API_PREFIX + "/clubs",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ClubDTO> create(@Valid @RequestBody ClubDTO clubData, UriComponentsBuilder builder)  {
        try {
            ClubDTO club = service.create(clubData);
            URI location = builder.path(API_PREFIX + "/clubs/{clubId}").buildAndExpand(club.getId()).toUri();
            return ResponseEntity.created(location).body(club);
        } catch (BaseService.IncompleteDataException ex) {
            throw createIncompleteDataResourceException(ex);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }

    @PatchMapping(value = API_PREFIX + "/clubs/{clubId}",
        consumes = APPLICATION_MERGE_PATCH_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClubDTO update(@PathVariable int clubId, @RequestBody Map<String, Object> updateData) {
        ClubUpdate update = new ClubUpdate(
            getStringUpdate(updateData, "name"),
            getStringUpdate(updateData, "abbreviation"));
        try {
            return service.update(clubId, update);
        } catch (BaseService.NotFoundException ex) {
            throw createNotFoundResourceException("Club", clubId);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }
}
