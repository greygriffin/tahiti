package net.greenpoppy.tahiti.club;

import net.greenpoppy.tahiti.member.MemberEntity;
import net.greenpoppy.tahiti.resource.ResourceTestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClubResourceTest
    extends ResourceTestBase {

    private static final String NAME = "Merihaan Veneseura";
    private static final String ABBREVIATION = "MVS";

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
        service.create(new ClubDTO(null, NAME, ABBREVIATION));
        List<ClubDTO> clubs = service.getAll();
        String json = json(clubs);
        MvcResult result  = mvc.perform(get("/api/clubs")
            .with(httpBasic("user", "password")))
            .andExpect(status().isOk())
            .andExpect(content().json(json))
            .andReturn();
    }

    @Test
    public void canCreateClub()
        throws Exception {
        ClubDTO newClub = new ClubDTO(null, NAME, ABBREVIATION);
        MvcResult result  = mvc.perform(post("/api/clubs")
            .contentType(CONTENT_TYPE_JSON_UTF8)
            .content(json(newClub))
            .with(httpBasic("admin", "secret")))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION_HEADER))
            .andReturn();
        assertTrue(service.getCount() == 1);
        List<ClubDTO> clubs = service.getAll();
        assertEquals(1, clubs.size());
        assertEquals(NAME, clubs.get(0).getName());
        assertEquals(ABBREVIATION, clubs.get(0).getAbbreviation());
        String location = result.getResponse().getHeader(LOCATION_HEADER);
        assertTrue(location.endsWith("/api/clubs/" + clubs.get(0).getId()));
    }
}
