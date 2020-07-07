import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IoTDevTest {
    FogNode fogNodePF;
    FogNode fogNodePFlegal;
    
    IoTDev deviceA;
    IoTDev deviceB;
    IoTDev deviceC;
    IoTDev deviceD;
    IoTDev deviceE;
    IoTDev deviceEmpty;
    
    Contract contractIB;
    Contract contractD1;
    Contract contractD2;
    Contract contractD5;
    
    @Test
    final void testUpdateContractWithInconsistentContract() {
        deviceE = new IoTDev("files/Contract_D5.json");
        contractD5 = new Contract("files/Contract_D5.json");
        contractIB = new Contract("files/Contract_IB.json");
        
        assertTrue(deviceE.getContract().equals(contractD5), "It is true that "
                + "Device E has Contract.");
        
        deviceE.updateContract(contractIB);
        
        assertFalse(deviceE.getContract().equals(contractIB), "We reject IB "
                + "contract, since it is inconsistent. Contracts in our "
                + "stub implementations are not verified by default, so we "
                + "extract a new contract and mark it extracted. However, what"
                + "we do in the stub implementation is to take the old contract"
                + "and mark it extracted. Therefore the contract is still D5."
                + "Should return false!");
        
        assertTrue(deviceE.getContract().equals(contractD5), "We reject IB "
                + "contract, since it is inconsistent. Contracts in our "
                + "stub implementations are not verified by default, so we "
                + "extract a new contract and mark it extracted. However, what"
                + "we do in the stub implementation is to take the old contract"
                + "and mark it extracted. Therefore the contract is still D5."
                + "Should return true!");
    }
    
    @Test
    final void testUpdateSoftwaretWithInconsistentContract() {
        deviceE = new IoTDev("files/Contract_D5.json");
        contractD5 = new Contract("files/Contract_D5.json");
        contractIB = new Contract("files/Contract_IB.json");
        
        assertTrue(deviceE.getContract().equals(contractD5), "It is true that "
                + "Device E has Contract.");
        
        deviceE.updateSoftware(contractIB);
        
        assertFalse(deviceE.getContract().equals(contractIB), "We reject IB "
                + "contract, since it is inconsistent. Contracts in our "
                + "stub implementations are not verified by default, so we "
                + "extract a new contract and mark it extracted. However, what"
                + "we do in the stub implementation is to take the old contract"
                + "and mark it extracted. Therefore the contract is still D5."
                + "Should return false!");
        
        assertTrue(deviceE.getContract().equals(contractD5), "We reject IB "
                + "contract, since it is inconsistent. Contracts in our "
                + "stub implementations are not verified by default, so we "
                + "extract a new contract and mark it extracted. However, what"
                + "we do in the stub implementation is to take the old contract"
                + "and mark it extracted. Therefore the contract is still D5."
                + "Should return true!");
    }
    
    @Test
    final void testUpdateContractWithoutFogNode() {
        deviceE = new IoTDev("files/Contract_D5.json");
        contractD5 = new Contract("files/Contract_D5.json");
        contractD1 = new Contract("files/Contract_D1.json");
        
        assertTrue(deviceE.getContract().equals(contractD5), "It is true that "
                + "Device E has Contract.");
        
        deviceE.updateContract(contractD1);
        
        assertTrue(deviceE.getContract().equals(contractD1), "It is true that "
                + "Device E has Contract.");
    }
    
    @Test
    final void testUpdateContract() {
        fogNodePFlegal = new FogNode("files/Policy_PFlegal.json");
        deviceC = new IoTDev("files/Contract_D5.json", fogNodePFlegal);
        contractD1 = new Contract("files/Contract_D1.json");
        contractD2 = new Contract("files/Contract_D2.json");
        contractD5 = new Contract("files/Contract_D5.json");
        
        assertEquals(fogNodePFlegal.numberOfContracts(), 2, "Policy is valid, "
                + "we expect to have 2 contracts, true!");
        assertTrue(deviceC.getContract().equals(contractD5), "It is true that "
                + "Device C has Contract D5.");
        assertTrue(fogNodePFlegal.containsContract(contractD5), "It is true "
                + "that Contract D5 was within Policy PFlegal.");
        
        deviceC.updateContract(contractD1);
        
        assertEquals(fogNodePFlegal.numberOfContracts(), 2, "We tried to "
                + "update Device C with Contract D1. However, D1 is not "
                + "consistent with Contract D2. Therefore, the contract is"
                + "not updated!");
        
        assertTrue(fogNodePFlegal.containsContract(contractD2), "It is true "
                + "that Contract D2 is still within Policy PFlegal.");
        
        assertTrue(fogNodePFlegal.containsContract(contractD5), " Contract D5 "
                + "is still in Policy PFlegal, since we could not updated it"
                + "with Contract D1.");
        
        assertFalse(fogNodePFlegal.containsContract(contractD1), "Contract D1 "
                + "has not been inserted in Policy PFlegal.");
    }
    
    @Test
    final void testUpdateSoftware() {
        fogNodePFlegal = new FogNode("files/Policy_PFlegal.json");
        deviceC = new IoTDev("files/Contract_D5.json", fogNodePFlegal);
        contractD1 = new Contract("files/Contract_D1.json");
        contractD2 = new Contract("files/Contract_D2.json");
        contractD5 = new Contract("files/Contract_D5.json");
        
        assertEquals(fogNodePFlegal.numberOfContracts(), 2, "Policy is valid, "
                + "we expect to have 2 contracts, true!");
        assertTrue(deviceC.getContract().equals(contractD5), "It is true that "
                + "Device C has Contract D5.");
        assertTrue(fogNodePFlegal.containsContract(contractD5), "It is true "
                + "that Contract D5 was within Policy PFlegal.");
        
        deviceC.updateSoftware(contractD1);
        
        assertEquals(fogNodePFlegal.numberOfContracts(), 2, "We tried to "
                + "update Device C with Contract D1. However, D1 is not "
                + "consistent with Contract D2. Therefore, the contract is"
                + "not updated!");
        
        assertTrue(fogNodePFlegal.containsContract(contractD2), "It is true "
                + "that Contract D2 is still within Policy PFlegal.");
        
        assertTrue(fogNodePFlegal.containsContract(contractD5), " Contract D5 "
                + "is still in Policy PFlegal, since we could not updated it"
                + "with Contract D1.");
        
        assertFalse(fogNodePFlegal.containsContract(contractD1), "Contract D1 "
                + "has not been inserted in Policy PFlegal.");
    }
    
    @Test
    final void testPolicyPFNotInserted() {
        fogNodePF = new FogNode("files/Policy_PF.json");
        
        assertEquals(fogNodePF.numberOfContracts(), 0, "Policy is not valid, "
                + "we expect to have no contracts!");
    }
    
    @Test
    final void testHasNoContract() {
        deviceEmpty = new IoTDev();
        
        assertFalse(deviceEmpty.hasContract(), "We created a default device"
                + "without a contract, just an empty object. Should "
                + "return false.");
    }
    
    @Test
    final void testHasContract() {
        fogNodePF = new FogNode("files/Policy_PF.json");
        deviceA = new IoTDev("files/Contract_D1.json", fogNodePF);
        
        assertTrue(deviceA.hasContract(), "We created a device with a contract,"
                + "Should return true.");
    }
}
