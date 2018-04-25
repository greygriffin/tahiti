package net.greenpoppy.tahiti.club;

import net.greenpoppy.tahiti.resource.ResourceTestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClubResourceTest
    extends ResourceTestBase {

    private static final String NAME = "Merihaan Veneseura";
    private static final String ABBREVIATION = "MVS";
    private static final String OTHER_NAME = "Suomalainen Pursiseura";
    private static final String OTHER_ABBREVIATION = "SPS";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClubService service;

    @Before
    public void setup() {
        service.deleteAll();
    }

    @Test
    public void canGetClubs()
        throws Exception {
        service.create(new ClubDTO(NAME, ABBREVIATION));
        List<ClubDTO> clubs = service.getAll();
        String json = json(clubs);
        mvc.perform(get("/api/clubs")
            .with(httpBasic("user", "password")))
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void cannotGetWithBadCredentials()
        throws Exception {
        mvc.perform(get("/api/clubs")
            .with(httpBasic("user", "wrong password")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void canGetClub()
        throws Exception {
        ClubDTO club = service.create(new ClubDTO(NAME, ABBREVIATION));
        String json = json(club);
        mvc.perform(get("/api/clubs/{id}", club.getId())
            .with(httpBasic("user", "password")))
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void cannotGetNonExistentClub()
        throws Exception {
        mvc.perform(get("/api/clubs/{id}", -1)
            .with(httpBasic("user", "password")))
            .andExpect(status().isNotFound());
    }

    @Test
    public void canCreateClub()
        throws Exception {
        ClubDTO newClub = new ClubDTO(NAME, ABBREVIATION);
        MvcResult result  = mvc.perform(post("/api/clubs")
            .contentType(APPLICATION_JSON_UTF8)
            .content(json(newClub))
            .with(httpBasic("admin", "secret")))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(header().exists(LOCATION_HEADER))
            .andReturn();
        assertEquals(1, service.getCount());
        List<ClubDTO> clubs = service.getAll();
        assertEquals(1, clubs.size());
        assertEquals(NAME, clubs.get(0).getName());
        assertEquals(ABBREVIATION, clubs.get(0).getAbbreviation());
        ClubDTO returned = fromJson(ClubDTO.class, result.getResponse().getContentAsString());
        assertEquals(clubs.get(0), returned);
        String location = result.getResponse().getHeader(LOCATION_HEADER);
        assertTrue(location.endsWith("/api/clubs/" + clubs.get(0).getId()));
    }

    @Test
    public void canUpdateName()
        throws Exception {
        ClubDTO created = service.create(new ClubDTO(NAME, ABBREVIATION));
        Map<String, Object> update = new HashMap<>();
        update.put("name", OTHER_NAME);
        MvcResult result  = mvc.perform(patch("/api/clubs/{id}", created.getId())
            .contentType(APPLICATION_MERGE_PATCH_JSON_UTF8)
            .content(json(update))
            .with(httpBasic("admin", "secret")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andReturn();
        assertEquals(1, service.getCount());   // update didn't create a new club
        ClubDTO fetched = service.get(created.getId());
        assertEquals(created.getId(), fetched.getId());
        assertEquals(OTHER_NAME, fetched.getName());
        assertEquals(created.getAbbreviation(), fetched.getAbbreviation());
        ClubDTO returned = fromJson(ClubDTO.class, result.getResponse().getContentAsString());
        assertEquals(fetched, returned);
    }

    @Test
    public void canUpdateAbbrevation()
        throws Exception {
        ClubDTO created = service.create(new ClubDTO(NAME, ABBREVIATION));
        Map<String, Object> update = new HashMap<>();
        update.put("abbreviation", OTHER_ABBREVIATION);
        MvcResult result  = mvc.perform(patch("/api/clubs/{id}", created.getId())
            .contentType(APPLICATION_MERGE_PATCH_JSON_UTF8)
            .content(json(update))
            .with(httpBasic("admin", "secret")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andReturn();
        assertEquals(1, service.getCount());   // update didn't create a new club
        ClubDTO fetched = service.get(created.getId());
        assertEquals(created.getId(), fetched.getId());
        assertEquals(created.getName(), fetched.getName());
        assertEquals(OTHER_ABBREVIATION, fetched.getAbbreviation());
        ClubDTO returned = fromJson(ClubDTO.class, result.getResponse().getContentAsString());
        assertEquals(fetched, returned);
    }

    @Test
    public void canUpdateAll()
        throws Exception {
        ClubDTO created = service.create(new ClubDTO(NAME, ABBREVIATION));
        Map<String, Object> update = new HashMap<>();
        update.put("name", OTHER_NAME);
        update.put("abbreviation", OTHER_ABBREVIATION);
        MvcResult result  = mvc.perform(patch("/api/clubs/{id}", created.getId())
            .contentType(APPLICATION_MERGE_PATCH_JSON_UTF8)
            .content(json(update))
            .with(httpBasic("admin", "secret")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andReturn();
        assertEquals(1, service.getCount());   // update didn't create a new club
        ClubDTO fetched = service.get(created.getId());
        assertEquals(created.getId(), fetched.getId());
        assertEquals(OTHER_NAME, fetched.getName());
        assertEquals(OTHER_ABBREVIATION, fetched.getAbbreviation());
        ClubDTO returned = fromJson(ClubDTO.class, result.getResponse().getContentAsString());
        assertEquals(fetched, returned);
    }

    @Test
    public void canUpdateNothing()
        throws Exception {
        ClubDTO created = service.create(new ClubDTO(NAME, ABBREVIATION));
        Map<String, Object> update = new HashMap<>();
        MvcResult result  = mvc.perform(patch("/api/clubs/{id}", created.getId())
            .contentType(APPLICATION_MERGE_PATCH_JSON_UTF8)
            .content(json(update))
            .with(httpBasic("admin", "secret")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andReturn();
        assertEquals(1, service.getCount());   // update didn't create a new club
        ClubDTO fetched = service.get(created.getId());
        assertEquals(created, fetched);
        ClubDTO returned = fromJson(ClubDTO.class, result.getResponse().getContentAsString());
        assertEquals(fetched, returned);
    }

    @Test
    public void canDelete()
        throws Exception {
        ClubDTO created = service.create(new ClubDTO(NAME, ABBREVIATION));
        mvc.perform(delete("/api/clubs/{id}", created.getId())
            .with(httpBasic("admin", "secret")))
            .andExpect(status().isNoContent());
        assertEquals(0, service.getCount());
    }
}
