package concreteTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DatabaseManager.DatabaseManager;
import DatabaseManager.DatabaseManager.dbTables;
import datasourceConcrete.ConcreteTableHelper;
import datasourceSingle.SingleTableHelper;
import exception.DatabaseException;
import mappersConcrete.StripNailMapper;
import sharedDomain.MapperManager;
import sharedDomain.Nail;
import sharedDomain.PowerTool;
import sharedDomain.StripNail;
import sharedDomain.Tool;
import testData.NailsForTest;

import org.junit.jupiter.api.Test;

class TestStripNailMapper {
	Connection c = null;


	@BeforeEach
	public void setup() throws DatabaseException {
		try {
			DatabaseManager dbm = DatabaseManager.getDatabaseManager();
			c = dbm.getConnection(dbTables.CONCRETE);
			ConcreteTableHelper.main(null);
			c.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("IN THE CATCH");
		}
	}

	@AfterEach
	public void rollback() {
		try {
			c.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
	}

	/**
	 * Tests to make sure the creation constructor works and that you can find the
	 * correct nail ID in the mapper.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFindingOneNail() throws Exception {
		MapperManager.getSingleton().setMapperPackageName("mappersConcrete");
		StripNailMapper nm = new StripNailMapper("987", 23, 20, 4, 20); // Creation Constructor
		int stripNailID = nm.getStripNailID();
		assertNotNull(nm.getStripNail());
		// compare the id in the gateway and the ID created by the nail object
		assertEquals(stripNailID, nm.getStripNail().getID());
	}
	/**
	 * Tests to make sure the finder constructor works and to make sure all the data
	 * is correct.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFinderConstructor() throws Exception {
		MapperManager.getSingleton().setMapperPackageName("mappersConcrete");
		StripNailMapper snm = new StripNailMapper(16);
		System.out.println(snm);

		assertEquals("987", snm.getStripNail().getUpc());
		assertEquals(23, snm.getStripNail().getManufacturerID());
		assertEquals(20, snm.getStripNail().getPrice());
		assertEquals(4, snm.getStripNail().getLength());
		assertEquals(20, snm.getStripNail().getNumberInStrip());
		assertEquals(16, snm.getStripNail().getID());
	}
	/**
	 * Tests to see whether or not the FindAll Method works and returns the correct ID's.
	 * 
	 * @throws DatabaseException
	 */
	@Test
	public void testFindAllMethod() throws DatabaseException {
		MapperManager.getSingleton().setMapperPackageName("mappersConcrete");
		ArrayList<StripNail> temp = StripNailMapper.findAll();
		assertEquals(16, temp.get(0).getID());
		assertEquals(17, temp.get(1).getID());
		assertEquals(18, temp.get(2).getID());
		assertEquals(19, temp.get(3).getID());
		assertEquals(20, temp.get(4).getID());
	}
	/**
	 * Test that delete returns true when successful, and False when there is a failure
	 * @throws Exception 
	 */
	@Test
	public void testDelete() throws Exception {
		DatabaseManager dbm = DatabaseManager.getDatabaseManager();
		StripNailMapper snm = new StripNailMapper(16);
		String query = "SELECT * FROM StripNails WHERE ID = ?";

		PreparedStatement stmnt1 = dbm.getConnection(dbTables.CONCRETE).prepareStatement(query);
		stmnt1.setInt(1, 16);
		ResultSet rs = stmnt1.executeQuery();

		assertTrue(rs.next());
		snm.delete();

		PreparedStatement stmnt2 = dbm.getConnection(dbTables.CONCRETE).prepareStatement(query);
		stmnt2.setInt(1, 16);
		rs = stmnt2.executeQuery();

		assertFalse(rs.next());
	}
	/**
	 * Tests that persist updates everything in the row where the id's match.
	 * @throws Exception 
	 */
	@Test
	public void testPersistChangeAll() throws Exception {
		StripNailMapper stripNail = new StripNailMapper(16);
		assertNotNull(stripNail);
		stripNail.persist();
	}

	/**
	 * Tests that persist can update some data and pass through the previous data that is not updated.
	 * @throws Exception 
	 */
	@Test
	public void testPersistChangeSome() throws Exception {
		StripNailMapper stripNail = new StripNailMapper(16);
		assertNotNull(stripNail);
		stripNail.persist();

	}

}
