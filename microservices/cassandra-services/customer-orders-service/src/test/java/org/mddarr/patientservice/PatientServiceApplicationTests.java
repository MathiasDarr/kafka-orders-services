package org.mddarr.patientservice;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mddarr.patientservice.config.CassandraConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CassandraConfig.class)
class PatientServiceApplicationTests {

//	public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS ks2 " + "WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";
//	public static final String KEYSPACE_ACTIVATE_QUERY = "USE ks2;";
//
//	@Autowired
//	private CassandraOperations cassandraTemplate;
//
//	@BeforeClass
//	public static void startCassandraEmbedded() throws InterruptedException, TTransportException, ConfigurationException, IOException {
////		EmbeddedCassandraServerHelper.startEmbeddedCassandra();
//		Cluster cluster = Cluster.builder()
//				.addContactPoints("127.0.0.1").withPort(9042).build();
//		Session session = cluster.connect();
//		session.execute(KEYSPACE_CREATION_QUERY);
//		session.execute(KEYSPACE_ACTIVATE_QUERY);
//		Thread.sleep(5000);
//	}
//
////	@AfterClass
////	public static void stopCassandraEmbedded() {
////		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
////	}
//
//	@Before
//	public void createTable() throws InterruptedException, TTransportException, ConfigurationException, IOException {
////		cassandraTemplate.
//
//
//
////		adminTemplate.createTable(true, CqlIdentifier.cqlId("PatientsTest"), Patient.class, new HashMap<String, Object>());
//	}
//
//	@Autowired
//	PatientsService patientsService;
//
//
//
//	@Test
//	void contextLoads() {
//		String first_name = "dude";
//		String last_name = "theam";
//		String userID = patientsService.createNewPatient(new PostPatientRequest(first_name, last_name));
//
//
//
//	}

}
