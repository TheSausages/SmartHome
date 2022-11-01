package pwr.smart.home.data.intergation.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Base class for integration tests. Creates 2 containers: a database (Postgres) and Keycloak.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Sql(scripts = {"classpath:data.sql", "classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class BaseIntegrationTest {
	private final static Slf4jLogConsumer containerLogger = new Slf4jLogConsumer(LoggerFactory.getLogger(BaseIntegrationTest.class));

	@Autowired
	protected ObjectMapper mapper;

	@Autowired
	protected WebApplicationContext context;

	// Use this to make http cals in integration tests
	protected WebTestClient webTestClient;

	protected static final PostgreSQLContainer<?> postreg = new PostgreSQLContainer<>("postgres:14.1-alpine")
			.withDatabaseName("SmartHomeApp")
				.withUsername("SmartHomeUser")
				.withPassword("SmartHomePassword")
				.withExposedPorts(5432)
			.withStartupCheckStrategy(
					new MinimumDurationRunningStartupCheckStrategy(Duration.ofSeconds(5))
			)
			.withLogConsumer(containerLogger)
			.withReuse(true);

	// This is not BeforeAll, as we need the containers running inside 'registerContainerProperties' function (below)
	static {
		postreg.start();
	}

	@BeforeEach
	public void setup() {
		MockMvc mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();

		this.webTestClient = MockMvcWebTestClient
				.bindTo(mockMvc)
				.build();

		assertThat(postreg.isRunning(), is(true));
	}

	/**
	 * Add container information for properties
	 * @param registry The registry holding the properties
	 */
	@DynamicPropertySource
	static void registerContainerProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postreg::getJdbcUrl);
	}
}
