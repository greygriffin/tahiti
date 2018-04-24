package net.greenpoppy.tahiti;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
@SpringBootTest
@ActiveProfiles("test")
public class TahitiApplicationTest {

	@Test
	public void contextLoads() {

	}
}
