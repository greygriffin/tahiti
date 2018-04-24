package net.greenpoppy.tahiti.member;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import net.greenpoppy.tahiti.service.BaseService;
import net.greenpoppy.tahiti.resource.ResourceException;
import net.greenpoppy.tahiti.resource.BaseResource;


@RestController
public class MemberResource
    extends BaseResource {

    private MemberService service;

    @Autowired
    public MemberResource(MemberService service) {
        this.service = service;
    }

    @GetMapping(value = API_PREFIX + "/members", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<MemberDTO> getAll() {
        return service.getAll();
    }

    @GetMapping(value = API_PREFIX + "/clubs/{clubId}/members", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<MemberDTO> getByClub(@PathVariable Integer clubId) {
        return service.getByClubId(clubId);
    }

    @GetMapping(value = API_PREFIX + "/members", params = { "lastName" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<MemberDTO> getByLastName(@RequestParam String lastName) {
        return service.getByLastName(lastName);
    }

    @GetMapping(value = API_PREFIX + "/members", params = { "firstName", "lastName" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<MemberDTO> getByLastName(@RequestParam String firstName, @RequestParam String lastName) {
        return service.getByFirstNameAndLastName(firstName, lastName);
    }

    @GetMapping(value = API_PREFIX + "/members/{memberId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MemberDTO get(@PathVariable int memberId) {
        MemberDTO dto = service.get(memberId);
        if (dto == null) {
            throw createNotFoundResourceException("Member", memberId);
        } else {
            return dto;
        }
    }

    @GetMapping(value = API_PREFIX + "/members", params = { "email" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MemberDTO getByEmail(@RequestParam String email) {
        MemberDTO dto = service.getByEmail(email);
        if (dto == null) {
            throw new ResourceException(
                HttpStatus.NOT_FOUND,
                NOT_FOUND,
                "Member with e-mail '" + email + "' not found");
        } else {
            return dto;
        }
    }

    @DeleteMapping(value = API_PREFIX + "/members/{memberId}")
    public void delete(@PathVariable int memberId) {
        service.delete(memberId);
    }

    @PostMapping(value = "/clubs/{clubId}/members",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MemberDTO create(@PathVariable Integer clubId, @Valid @RequestBody MemberDTO memberData)  {
        try {
            return service.create(clubId, memberData);
        } catch (BaseService.IncompleteDataException ex) {
            throw createIncompleteDataResourceException(ex);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }

    @PatchMapping(value = API_PREFIX + "/members/{memberId}",
        consumes = APPLICATION_MERGE_PATCH_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MemberDTO update(@PathVariable int memberId, @RequestBody Map<String, Object> updateData) {
        MemberUpdate update = new MemberUpdate(
            getStringUpdate(updateData, "firstName"),
            getStringUpdate(updateData, "lastName"),
            getStringUpdate(updateData, "streetAddress"),
            getStringUpdate(updateData, "postCode"),
            getStringUpdate(updateData, "city"),
            getStringUpdate(updateData, "country"),
            getStringUpdate(updateData, "phoneNumber"),
            getStringUpdate(updateData, "email"),
            getStringUpdate(updateData, "password"),
            getIntegerUpdate(updateData, "clubId"));
        try {
            return service.update(memberId, update);
        } catch (BaseService.NotFoundException ex) {
            throw createNotFoundResourceException("Member", memberId);
        } catch (BaseService.InvalidValueException ex) {
            throw createInvalidValuesResourceException(ex);
        }
    }
}
