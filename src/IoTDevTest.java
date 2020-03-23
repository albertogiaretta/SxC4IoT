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
    
    /*
    void init() {
        fogNodePF = new FogNode("files/Policy_PF.json");
        fogNodePFlegal = new FogNode("files/Policy_PFlegal.json");
        
        deviceA = new IoTDev("files/Contract_D1.json", fogNodePF);
        deviceB = new IoTDev("files/Contract_D2.json", fogNodePF);
        deviceC = new IoTDev("files/Contract_D5.json", fogNodePFlegal);
        deviceD = new IoTDev("files/Contract_D2.json", fogNodePFlegal);
        deviceE = new IoTDev("files/Contract_D5.json");
        deviceEmpty = new IoTDev();
        
        contractIB = new Contract("files/Contract_IB.json");
        contractD1 = new Contract("files/Contract_D1.json");
        contractD2 = new Contract("files/Contract_D2.json");
        contractD5 = new Contract("files/Contract_D5.json");
    }*/
    
    @Test
    final void testUpdateContractWithInconsistentContract() {
        //init();
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
        //init();
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
        //init();
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
        //init();
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
        //init();
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
        //init();
        fogNodePF = new FogNode("files/Policy_PF.json");
        
        assertEquals(fogNodePF.numberOfContracts(), 0, "Policy is not valid, "
                + "we expect to have no contracts!");
    }
    
    @Test
    final void testMessageFromCD1toCD2() {
        //init();
        fogNodePF = new FogNode("files/Policy_PF.json");
        deviceA = new IoTDev("files/Contract_D1.json", fogNodePF);
        deviceB = new IoTDev("files/Contract_D2.json", fogNodePF);
        
        assertFalse(deviceA.sendMessage(deviceB, "Hello there!"), "There is an "
                + "Illegal Information Exchange, messages are not allowed!");
    }
    
    @Test
    final void testMessageFromCD2toCD1() {
        //init();
        fogNodePF = new FogNode("files/Policy_PF.json");
        deviceA = new IoTDev("files/Contract_D1.json", fogNodePF);
        deviceB = new IoTDev("files/Contract_D2.json", fogNodePF);
        
        assertFalse(deviceB.sendMessage(deviceA, "Hello there!"), "There is an "
                + "Illegal Information Exchange, messages are allowed!");
    }

    @Test
    final void testMessageFromCD2toCD5() {
        //init();
        fogNodePFlegal = new FogNode("files/Policy_PFlegal.json");
        deviceC = new IoTDev("files/Contract_D5.json", fogNodePFlegal);
        deviceD = new IoTDev("files/Contract_D2.json", fogNodePFlegal);
        
        assertTrue(deviceD.sendMessage(deviceC, "Hello there!"), "There is not "
                + "an Illegal Information Exchange, messages are allowed!");
    }
    
    @Test
    final void testMessageFromCD5toCD2() {
        //init();
        fogNodePFlegal = new FogNode("files/Policy_PFlegal.json");
        deviceC = new IoTDev("files/Contract_D5.json", fogNodePFlegal);
        deviceD = new IoTDev("files/Contract_D2.json", fogNodePFlegal);
        
        assertTrue(deviceC.sendMessage(deviceD, "Hello there!"), "There is not "
                + "an Illegal Information Exchange, messages are allowed!");
    }
    
    @Test
    final void testHasNoContract() {
        //init();
        deviceEmpty = new IoTDev();
        
        assertFalse(deviceEmpty.hasContract(), "We created a default device"
                + "without a contract, just an empty object. Should "
                + "return false.");
    }
    
    @Test
    final void testHasContract() {
        //init();
        fogNodePF = new FogNode("files/Policy_PF.json");
        deviceA = new IoTDev("files/Contract_D1.json", fogNodePF);
        
        assertTrue(deviceA.hasContract(), "We created a device with a contract,"
                + "Should return true.");
    }
}
