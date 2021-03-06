package org.sagebionetworks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sagebionetworks.client.SynapseAdminClient;
import org.sagebionetworks.client.SynapseAdminClientImpl;
import org.sagebionetworks.client.SynapseClient;
import org.sagebionetworks.client.SynapseClientImpl;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.client.exceptions.SynapseNotFoundException;
import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.repo.model.PaginatedResults;
import org.sagebionetworks.repo.model.Project;
import org.sagebionetworks.repo.model.Study;
import org.sagebionetworks.repo.model.TrashedEntity;

public class IT070SynapseJavaClientTrashCanTest {

	private static SynapseAdminClient adminSynapse;
	private static SynapseClient synapse;
	private static Long userToDelete;
	
	private Entity parent;
	private Entity child;
	
	@BeforeClass 
	public static void beforeClass() throws Exception {
		// Create a user
		adminSynapse = new SynapseAdminClientImpl();
		SynapseClientHelper.setEndpoints(adminSynapse);
		adminSynapse.setUserName(StackConfiguration.getMigrationAdminUsername());
		adminSynapse.setApiKey(StackConfiguration.getMigrationAdminAPIKey());
		
		synapse = new SynapseClientImpl();
		userToDelete = SynapseClientHelper.createUser(adminSynapse, synapse);
	}

	@Before
	public void before() throws SynapseException {
		parent = new Project();
		parent.setName("IT070SynapseJavaClientTrashCanTest.parent");
		parent = synapse.createEntity(parent);
		assertNotNull(parent);

		child = new Study();
		child.setName("IT070SynapseJavaClientTrashCanTest.child");
		child.setParentId(parent.getId());
		child = synapse.createEntity(child);
		assertNotNull(child);
	}

	@After
	public void after() throws SynapseException {
		if (child != null) {
			synapse.deleteAndPurgeEntityById(child.getId());
		}
		if (parent != null) {
			synapse.deleteAndPurgeEntityById(parent.getId());
		}
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		adminSynapse.deleteUser(userToDelete);
	}

	@Test
	public void test() throws SynapseException {

		synapse.moveToTrash(parent.getId());
		try {
			synapse.getEntityById(parent.getId());
			fail();
		} catch (SynapseNotFoundException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		try {
			synapse.getEntityById(child.getId());
			fail();
		} catch (SynapseNotFoundException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		PaginatedResults<TrashedEntity> results = synapse.viewTrashForUser(0L, Long.MAX_VALUE);
		assertNotNull(results);
		assertEquals(2, results.getResults().size());

		synapse.restoreFromTrash(parent.getId(), null);
		Entity entity = synapse.getEntityById(parent.getId());
		assertNotNull(entity);
		entity = synapse.getEntityById(child.getId());
		assertNotNull(entity);

		results = synapse.viewTrashForUser(0L, Long.MAX_VALUE);
		assertNotNull(results);
		assertEquals(0, results.getResults().size());
	}

	@Test
	public void testPurge() throws SynapseException {
		synapse.moveToTrash(parent.getId());
		synapse.purgeTrashForUser(child.getId());
		try {
			synapse.getEntityById(child.getId());
			fail();
		} catch (SynapseNotFoundException e) {
			assertTrue(true);
		} catch (Throwable e) {
			fail();
		}
		PaginatedResults<TrashedEntity> results = synapse.viewTrashForUser(0L, Long.MAX_VALUE);
		assertNotNull(results);
		assertEquals(1, results.getResults().size());
		assertEquals(parent.getId(), results.getResults().get(0).getEntityId());
		synapse.purgeTrashForUser(parent.getId());
		try {
			synapse.getEntityById(parent.getId());
			fail();
		} catch (SynapseNotFoundException e) {
			assertTrue(true);
		} catch (Throwable e) {
			fail();
		}
		results = synapse.viewTrashForUser(0L, Long.MAX_VALUE);
		assertNotNull(results);
		assertEquals(0, results.getResults().size());
		// Already purged, no need to clean
		child = null;
		parent = null;
	}

	@Test
	public void testPurgeAll() throws SynapseException {
		synapse.moveToTrash(parent.getId());
		synapse.purgeTrashForUser();
		try {
			synapse.getEntityById(child.getId());
			fail();
		} catch (SynapseNotFoundException e) {
			assertTrue(true);
		} catch (Throwable e) {
			fail();
		}
		try {
			synapse.getEntityById(parent.getId());
			fail();
		} catch (SynapseNotFoundException e) {
			assertTrue(true);
		} catch (Throwable e) {
			fail();
		}
		PaginatedResults<TrashedEntity> results = synapse.viewTrashForUser(0L, Long.MAX_VALUE);
		assertNotNull(results);
		assertEquals(0, results.getResults().size());
		// Already purged, no need to clean
		child = null;
		parent = null;
	}

	@Test
	public void testAdmin() throws SynapseException {
		adminSynapse.purgeTrash();
		synapse.moveToTrash(parent.getId());
		PaginatedResults<TrashedEntity> results = adminSynapse.viewTrash(0L, Long.MAX_VALUE);
		assertNotNull(results);
		assertEquals(2, results.getResults().size());
		assertEquals(2, results.getTotalNumberOfResults());
		adminSynapse.purgeTrash();
		try {
			synapse.getEntityById(child.getId());
			fail();
		} catch (SynapseNotFoundException e) {
			assertTrue(true);
		} catch (Throwable e) {
			fail();
		}
		try {
			synapse.getEntityById(parent.getId());
			fail();
		} catch (SynapseNotFoundException e) {
			assertTrue(true);
		} catch (Throwable e) {
			fail();
		}
		results = adminSynapse.viewTrashForUser(0L, Long.MAX_VALUE);
		assertNotNull(results);
		assertEquals(0, results.getResults().size());
		// Already purged, no need to clean
		child = null;
		parent = null;
	}
}
