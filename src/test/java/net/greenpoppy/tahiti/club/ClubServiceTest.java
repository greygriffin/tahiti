package net.greenpoppy.tahiti.club;

import net.greenpoppy.tahiti.service.ServiceTestBase;
import net.greenpoppy.tahiti.service.Update;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ClubServiceTest
    extends ServiceTestBase {
    private static final String NAME = "Merihaan Veneseura";
    private static final String ABBREVIATION = "MVS";
    private static final String OTHER_NAME = "Suomalainen Pursiseura";
    private static final String OTHER_ABBREVIATION = "SPS";

    @Autowired
    private ClubService service;

    @Before
    public void setup() {
        service.deleteAll();
    }

    @Test
    public void afterSetupThereAreNoClubs() {
        assertEquals(0, service.getCount());
    }

    @Test
    public void createOne()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        assertEquals(1, service.getCount());
        assertNotNull(created.getId());
        assertEquals(NAME, created.getName());
        assertEquals(ABBREVIATION, created.getAbbreviation());
        assertNotNull(created.getCreatedAt());
        assertNotNull(created.getUpdatedAt());
        assertFalse(getInstant(created.getUpdatedAt()).isBefore(getInstant(created.getUpdatedAt())));
    }

    @Test
    public void createOneGetOne()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO fetched = service.get(created.getId());
        assertEquals(created, fetched);
    }

    @Test
    public void createOneGetOneByWrongId()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO fetched = service.get(created.getId() + 1);
        assertNull(fetched);
    }

    @Test
    public void createOneGetAll()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        List<ClubDTO> all = service.getAll();
        assertEquals(1, all.size());
        assertEquals(created, all.get(0));
    }

    @Test
    public void createOneDeleteOne()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        service.delete(created.getId());
        assertEquals(0, service.getCount());
        List<ClubDTO> all = service.getAll();
        assertEquals(0, all.size());
        ClubDTO fetched = service.get(created.getId());
        assertNull(fetched);
    }

    @Test
    public void createOneGetByName()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO fetched = service.getByName(NAME);
        assertEquals(created, fetched);
    }

    @Test
    public void createOneGetByWrongName()
        throws Exception {
        service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO fetched = service.getByName(OTHER_NAME);
        assertNull(fetched);
    }

    @Test
    public void createOneGetByAbbreviation()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO fetched = service.getByAbbreviation(ABBREVIATION);
        assertEquals(created, fetched);
    }

    @Test
    public void createOneGetByWrongAbbreviation()
        throws Exception {
        service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO fetched = service.getByAbbreviation(OTHER_ABBREVIATION);
        assertNull(fetched);
    }

    @Test
    public void createOneUpdateName()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO updated = service.update(created.getId(), new ClubUpdate(new Update<>(OTHER_NAME), null));
        assertEquals(created.getId(), updated.getId());
        assertEquals(OTHER_NAME, updated.getName());
        assertEquals(created.getAbbreviation(), updated.getAbbreviation());
        assertEquals(created.getCreatedAt(), updated.getCreatedAt());
        assertTrue(getInstant(updated.getUpdatedAt()).isAfter(getInstant(updated.getCreatedAt())));
        assertEquals(1, service.getCount());  // no extra clubs got created
        assertEquals(updated, service.get(created.getId()));   // was really updated
    }

    @Test
    public void createOneUpdateAbbreviation()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO updated = service.update(created.getId(), new ClubUpdate(null, new Update<>(OTHER_ABBREVIATION)));
        assertEquals(created.getId(), updated.getId());
        assertEquals(created.getName(), updated.getName());
        assertEquals(OTHER_ABBREVIATION, updated.getAbbreviation());
        assertEquals(created.getCreatedAt(), updated.getCreatedAt());
        assertTrue(getInstant(updated.getUpdatedAt()).isAfter(getInstant(updated.getCreatedAt())));
        assertEquals(1, service.getCount());  // no extra clubs got created
        assertEquals(updated, service.get(created.getId()));   // was really updated
    }

    @Test
    public void createOneUpdateAll()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO updated = service.update(created.getId(), new ClubUpdate(new Update<>(OTHER_NAME), new Update<>(OTHER_ABBREVIATION)));
        assertEquals(created.getId(), updated.getId());
        assertEquals(OTHER_NAME, updated.getName());
        assertEquals(OTHER_ABBREVIATION, updated.getAbbreviation());
        assertEquals(created.getCreatedAt(), updated.getCreatedAt());
        assertTrue(getInstant(updated.getUpdatedAt()).isAfter(getInstant(updated.getCreatedAt())));
        assertEquals(1, service.getCount());  // no extra clubs got created
        assertEquals(updated, service.get(created.getId()));   // was really updated
    }

    @Test
    public void createOneUpdateNothing()
        throws Exception {
        ClubDTO created = service.create(ClubDTO.builder().name(NAME).abbreviation(ABBREVIATION).build());
        ClubDTO updated = service.update(created.getId(), new ClubUpdate(null, null));
        assertEquals(created, updated);
        assertEquals(1, service.getCount());  // no extra clubs got created
        assertEquals(created, service.get(created.getId()));   // was really not updated
    }
}
