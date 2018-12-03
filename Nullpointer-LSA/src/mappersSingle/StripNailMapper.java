package mappersSingle;

import java.util.ArrayList;

import datasource.InventoryItemDTO;
import datasource.PowerToolToStripNailDTO;
import datasource.StripNailDTO;
import datasourceSingle.PowerToolsToStripNailsGateway;
import datasourceSingle.InventoryItemsGateway;
import exception.DatabaseException;
import mappersSingle.PowerToolMapper;
import sharedDomain.PowerTool;
import sharedDomain.StripNail;
import sharedDomain.StripNailAbstract;
/**
 * @author Chris Roadcap
 * @author Kanza Amin
 */

public class StripNailMapper extends StripNailAbstract
{
	final static int STRIPNAIL_TYPE = 4;
	private InventoryItemsGateway inventoryItemsGateway;
	private StripNail stripNail;

	/**
	 * Find Constructor
	 * @param stripNailID - the ID of the stripnail in the database
	 * @throws Exception
	 */
	public StripNailMapper(int stripNailID) throws Exception
	{
		this.inventoryItemsGateway = new InventoryItemsGateway(stripNailID);
		this.buildStripNail();
	}
	
	/**
	 * Create Constructor
	 * @param upc upc of the stripNail
	 * @param manufacturerID manufacturerID of the stripNail
	 * @param price price of the stripNail
	 * @param numberInStrip number of nails in a strip
	 * @param length the length of the stripnail
	 * @throws Exception
	 */
	public StripNailMapper(String upc, int manufacturerID, int price, int numberInStrip, double length) throws Exception
	{
		this.inventoryItemsGateway = new InventoryItemsGateway(upc, manufacturerID, price, numberInStrip, length);
		this.buildStripNail();

	}
	
	/**
	 * Finds all stripnails within the database
	 * @return an ArrayList of all StripNails which exist in the database
	 * @throws DatabaseException
	 */
	public static ArrayList<StripNail> findAll() throws DatabaseException
    {
        ArrayList<InventoryItemDTO> temp = InventoryItemsGateway.findAllByType(STRIPNAIL_TYPE);
        ArrayList<StripNailDTO> dtos = new ArrayList<StripNailDTO>();
        ArrayList<StripNail> stripNails = new ArrayList<StripNail>();
        
        for(int i = 0; i < temp.size(); i++)
        {
        	dtos.add((StripNailDTO) temp.get(i));
            stripNails.add(new StripNail(dtos.get(i).getId(), dtos.get(i).getUpc(), dtos.get(i).getManufacturerID(),
                           dtos.get(i).getPrice(),dtos.get(i).getLength(), dtos.get(i).getNumberInStrip()));
        }
        return stripNails;
    }
	
	/**
	 * Saves changed values of the stripNail to the database
	 */
	@Override
	public void persist() 
	{   
		inventoryItemsGateway.setUPC(this.stripNail.getUpc());
		inventoryItemsGateway.setManID(this.stripNail.getManufacturerID());
		inventoryItemsGateway.setPrice(this.stripNail.getPrice());
		inventoryItemsGateway.setLength(this.stripNail.getLength());
		inventoryItemsGateway.setNumberInStrip(this.stripNail.getNumberInStrip());
		try 
		{
			inventoryItemsGateway.persist();
		}
		catch (Exception e) 
		{
			
			e.printStackTrace();
		}
		
	}   

	/**
	 * Deletes a the powertool from the database
	 */
	@Override
	public void delete() 
	{
		try 
		{
			inventoryItemsGateway.deleteInventoryItem();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * returns the instance of stripnail stored as an instance variable
	 */
	public StripNail getStripNail() 
	{
		return stripNail;
	}

	/**
	 * finds all powertools which exist in the database
	 * @return an arraylist of all powertools which exist in the database
	 */
	public ArrayList<PowerTool> getPowerTools() throws DatabaseException 
	{
		ArrayList<PowerTool> powerTools = new ArrayList<PowerTool>();
		ArrayList<PowerToolToStripNailDTO> dtos = PowerToolsToStripNailsGateway.findPowerToolByStripNailID(stripNail.getID());
		for(int i = 0; i < dtos.size(); i++)
		{
			powerTools.add(new PowerToolMapper(dtos.get(i).getPowerToolId()).getPowerTool());
		}
		
		return powerTools;
	}

	
	/**
	 * @returns the ID of stripnail
	 */
	public int getStripNailID() 
	{
		return stripNail.getID();
	}

	/**
	 * constructs the instance of the stripnail stored by the class as an instance variable
	 */
	@Override
	protected void buildStripNail() 
	{
		stripNail = new StripNail(inventoryItemsGateway.getAutoGeneratedID(), inventoryItemsGateway.getUPC(), inventoryItemsGateway.getManID(),
									inventoryItemsGateway.getPrice(), inventoryItemsGateway.getLength(), inventoryItemsGateway.getNumberInStrip());
		
	}

	public void addPowerTool(PowerTool powerTool) throws DatabaseException
	{
		PowerToolsToStripNailsGateway pt2sng = new PowerToolsToStripNailsGateway(powerTool.getID(), stripNail.getID());
		stripNail.getPowerToolList().update();
	}
}
