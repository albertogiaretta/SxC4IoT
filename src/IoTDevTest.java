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
    
    Contract contractIB;
    Contract contractD1;
    Contract contractD2;
    Contract contractD5;
    
    
    void init() {
        fogNodePF = new FogNode("files/Policy_PF.json");
        fogNodePFlegal = new FogNode("files/Policy_PFlegal.json");
        
        deviceA = new IoTDev("files/Contract_D1.json", fogNodePF);
        deviceB = new IoTDev("files/Contract_D2.json", fogNodePF);
        deviceC = new IoTDev("files/Contract_D5.json", fogNodePFlegal);
        deviceD = new IoTDev("files/Contract_D2.json", fogNodePFlegal);
        deviceE = new IoTDev("files/Contract_D5.json");
        
        contractIB = new Contract("files/Contract_IB.json");
        contractD1 = new Contract("files/Contract_D1.json");
        contractD2 = new Contract("files/Contract_D2.json");
        contractD5 = new Contract("files/Contract_D5.json");
    }
    
    @Test
    final void testUpdateContractWithInconsistentOne() {
        init();
        
        assertTrue(deviceE.getContract().equals(contractD5), "It is true that "
                + "Device E has Contract.");
        
        deviceE.updateContract(contractIB);
        
        assertTrue(deviceE.getContract().equals(contractIB), "In theory we should"
                + "reject this contract and extract a new one with DBAC. But"
                + "in our stub method we mark the current contract as Extracted"
                + "and that's it. Should return true!");
    }
    
    @Test
    final void testUpdateContractWithoutFogNode() {
        init();
        
        assertTrue(deviceE.getContract().equals(contractD5), "It is true that "
                + "Device E has Contract.");
        
        deviceE.updateContract(contractD1);
        
        assertTrue(deviceE.getContract().equals(contractD1), "It is true that "
                + "Device E has Contract.");
    }
    
    @Test
    final void testUpdateContract() {
        init();
        
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
    final void testPolicyPFNotInserted() {
        init();
        
        assertEquals(fogNodePF.numberOfContracts(), 0, "Policy is not valid, "
                + "we expect to have no contracts!");
    }
    
    @Test
    final void testMessageFromCD1toCD2() {
        init();
        
        assertFalse(deviceA.sendMessage(deviceB, "Hello there!"), "There is an "
                + "Illegal Information Exchange, messages are not allowed!");
    }
    
    @Test
    final void testMessageFromCD2toCD1() {
        init();
        
        assertFalse(deviceB.sendMessage(deviceA, "Hello there!"), "There is an "
                + "Illegal Information Exchange, messages are allowed!");
    }

    @Test
    final void testMessageFromCD2toCD5() {
        init();

        assertTrue(deviceD.sendMessage(deviceC, "Hello there!"), "There is not "
                + "an Illegal Information Exchange, messages are allowed!");
    }
    
    @Test
    final void testMessageFromCD5toCD2() {
        init();
        
        assertTrue(deviceC.sendMessage(deviceD, "Hello there!"), "There is not "
                + "an Illegal Information Exchange, messages are allowed!");
    }
}
