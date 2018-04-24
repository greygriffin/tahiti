package net.greenpoppy.tahiti.club;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
@SpringBootTest
@ActiveProfiles("test")
public class ClubDTOTest {
    private static final Integer ID = 1;
    private static final String NAME = "Merihaan Veneseura";
    private static final String ABBREVIATION = "MVS";

    @Test
    public void canBuild() {
        ClubDTO dto = ClubDTO.builder()
            .id(ID)
            .name(NAME)
            .abbreviation(ABBREVIATION)
            .build();
        assertEquals(ID, dto.getId());
    }
}
