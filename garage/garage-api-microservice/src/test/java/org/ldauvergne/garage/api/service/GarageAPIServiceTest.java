package org.ldauvergne.garage.api.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import garage.api.service.GarageAPIService;
import junit.framework.TestCase;

public class GarageAPIServiceTest extends TestCase{
	
    List<String> aFaultyPlates = Arrays.asList("99-AV-D/","99-AV-D;","'égfsgrtez","? 99-AV-D/","aafdsa*","99-av-d+");
    
    List<String> aCorrectPlates = Arrays.asList("99-AV-D4","99-AVD","DSA-AV-DASD");
    
    GarageAPIService aGarageAPIService= new GarageAPIService();
    
    public void testFaultyPlates()
    {
    	for(String plate: aFaultyPlates){
    		Assert.assertFalse(aGarageAPIService.mIsValidPlate(plate));
    	}
    }
    
    public void testCorrectPlates()
    {
    	for(String plate: aCorrectPlates){
    		Assert.assertTrue(aGarageAPIService.mIsValidPlate(plate));
    	}
    }
}
