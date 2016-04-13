package org.ldauvergne.garage.shared.models;

import junit.framework.TestCase;

public class VehicleModelTest extends TestCase{
    
    public void testCanCreateVehicleTest()
    {
    	VehicleModel lVehicle = new VehicleModel("TEST", null, null, VehicleType.UNDEFINED,  "DUCATI");
    	assertEquals(VehicleType.UNDEFINED, lVehicle.getType());
    }
    
    public void testCanCreateMotorbike()
    {
    	VehicleModel lVehicle = new VehicleModel("TEST", null, null, VehicleType.MOTORBIKE, "DUCATI");
    	
    	assertTrue(lVehicle instanceof VehicleModel);
    	
    	lVehicle=lVehicle.mCheckVehicleType();
    	
    	assertEquals(VehicleType.MOTORBIKE, lVehicle.getType());
    	
    	assertTrue(lVehicle instanceof MotorbikeModel);
    }
    
    public void testCanCreateCar()
    {
    	String lVehicleId = "TEST";

    	VehicleModel lVehicle = new VehicleModel(lVehicleId, null, null, VehicleType.CAR, "PEUGEOT");

    	assertTrue(lVehicle instanceof VehicleModel);
    	
    	lVehicle=lVehicle.mCheckVehicleType();
    	
    	assertTrue(lVehicle instanceof CarModel);
    	
    	assertEquals(VehicleType.CAR, lVehicle.getType());
    	
		assertEquals(lVehicleId, lVehicle.getRegistration_id());
    }
    
    public void testCanCreateUntypedVehicle()
    {
    	VehicleModel lVehicle = new VehicleModel("TEST", null, null, VehicleType.UNDEFINED, "PEUGEOT");
    	
    	assertTrue(lVehicle instanceof VehicleModel);
    	
    	lVehicle=lVehicle.mCheckVehicleType();
    	
    	assertEquals(VehicleType.UNDEFINED, lVehicle.getType());
    	
    	assertTrue(lVehicle instanceof VehicleModel);
    }
    
    public void testCheckVehiclePropertiesAfterConvertion()
    {
    	String lVehicleId = "TEST";

    	String lVehicleBrand = "PEUGEOT";
    	
    	VehicleModel lVehicle = new VehicleModel(lVehicleId, null, null, VehicleType.UNDEFINED, lVehicleBrand);

    	assertEquals(VehicleType.UNDEFINED, lVehicle.getType());
    	
    	lVehicle=lVehicle.mCheckVehicleType();
    	
		assertEquals(lVehicleId, lVehicle.getRegistration_id());
		
		assertEquals(lVehicleBrand, lVehicle.getBrand());
    }
}
