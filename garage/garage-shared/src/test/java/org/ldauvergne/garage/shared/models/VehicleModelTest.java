package org.ldauvergne.garage.shared.models;

import junit.framework.TestCase;

public class VehicleModelTest extends TestCase{
    
    public void testCanCreateVehicleTest()
    {
    	VehicleModel lVehicle = new VehicleModel("TEST", null, null, VehicleType.UNDEFINED, 2, "DUCATI");
    	assertEquals(VehicleType.UNDEFINED, lVehicle.getVehicleType());
    }
    
    public void testCanCreateMotorbike()
    {
    	VehicleModel lVehicle = new VehicleModel("TEST", null, null, VehicleType.UNDEFINED, 2, "DUCATI");
    	
    	assertEquals(VehicleType.UNDEFINED, lVehicle.getVehicleType());
    	
    	lVehicle=lVehicle.mAddVehicleTypeFromNbWheels();
    	
    	assertEquals(VehicleType.MOTORBIKE, lVehicle.getVehicleType());
    	
    	assertTrue(lVehicle instanceof MotorbikeModel);
    }
    
    public void testCanCreateCar()
    {
    	String lVehicleId = "TEST";

    	VehicleModel lVehicle = new VehicleModel(lVehicleId, null, null, VehicleType.UNDEFINED, 4, "PEUGEOT");

    	assertEquals(VehicleType.UNDEFINED, lVehicle.getVehicleType());
    	
    	lVehicle=lVehicle.mAddVehicleTypeFromNbWheels();
    	
    	assertTrue(lVehicle instanceof CarModel);
    	
    	assertEquals(VehicleType.CAR, lVehicle.getVehicleType());
    	
		assertEquals(lVehicleId, lVehicle.getRegistration_id());
    }
    
    public void testCanCreateUntypedVehicle()
    {
    	VehicleModel lVehicle = new VehicleModel("TEST", null, null, VehicleType.UNDEFINED, 0, "PEUGEOT");
    	
    	assertEquals(VehicleType.UNDEFINED, lVehicle.getVehicleType());
    	
    	lVehicle=lVehicle.mAddVehicleTypeFromNbWheels();
    	
    	assertEquals(VehicleType.UNDEFINED, lVehicle.getVehicleType());
    	
    	assertTrue(lVehicle instanceof VehicleModel);
    }
    
    public void testCheckVehiclePropertiesAfterConvertion()
    {
    	String lVehicleId = "TEST";

    	String lVehicleBrand = "PEUGEOT";
    	
    	VehicleModel lVehicle = new VehicleModel(lVehicleId, null, null, VehicleType.UNDEFINED, 4, lVehicleBrand);

    	assertEquals(VehicleType.UNDEFINED, lVehicle.getVehicleType());
    	
    	lVehicle=lVehicle.mAddVehicleTypeFromNbWheels();
    	
		assertEquals(lVehicleId, lVehicle.getRegistration_id());
		
		assertEquals(lVehicleBrand, lVehicle.getBrand());
    }
}
