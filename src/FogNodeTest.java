import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class FogNodeTest {
    ByteArrayOutputStream outContent;
    
    FogNode fogNodePA;
    FogNode fogNodePB;
    
    IoTDev deviceIB;
    IoTDev deviceA1;
    IoTDev deviceCB;
    IoTDev deviceB3;
    IoTDev deviceD3;
    
    public void init() {
        outContent = new ByteArrayOutputStream();
                
        fogNodePA = new FogNode("files/Policy_PA.json");
        fogNodePB = new FogNode("files/Policy_PB.json");
        
        deviceIB = new IoTDev("files/Contract_IB.json", fogNodePA);
        deviceA1 = new IoTDev("files/Contract_A1.json", fogNodePB);
        deviceCB = new IoTDev("files/Contract_CB.json", fogNodePA);
        deviceB3 = new IoTDev("files/Contract_B3.json", fogNodePA);
        deviceD3 = new IoTDev("files/Contract_D3.json", fogNodePA);
    }
    
    
    
    @Test
    final void testRefuseContractIBWithPolicyPA() {
        init();
        System.out.println(deviceIB.getContract().numberOfRules());
        assertFalse(fogNodePA.addDevice(deviceIB), "Contract IB is"
                + "inconsistent. Should return false!");
    }
    
    @Test
    final void testAcceptContractCBWithPolicyPA() {
        init();
        
        assertTrue(fogNodePA.addDevice(deviceCB), "Contract CB is"
                + "already part of the network, so the device with this"
                + "contract can join. Should return true!");
    }
    
    @Test
    final void testAcceptContractCBWithPolicyEmpty() {
        init();
        fogNodePA.clearPolicy();
        
        assertTrue(fogNodePA.addDevice(deviceCB), "The Fog policy"
                + "is empty, so the device with the Contract CB"
                + "can join. Should return true!");
    }
    
    @Test
    final void testAcceptContractB3WithPolicyPA() {
        init();
        
        assertFalse(fogNodePA.addDevice(deviceB3), "Contract B3 is"
                + "inconsistent with Policy PA, which contains Contract CB."
                + "Should return false!");
    }
    
    @Test
    final void testAcceptContractD4WithPolicyPA() {
        init();
        
        assertTrue(fogNodePA.addDevice(deviceD3), "Contract D3 is"
                + "consistent with Policy PA, which contains Contract CB."
                + "Should return true!");
    }
    
    @Test
    final void testRemoveContractCBFromPolicyPA() {
        init();
        
        assertTrue(fogNodePA.containsContract(deviceCB.getContract()), "Contract CB is in"
                + "Policy PA, we should find it! Should return true!");
        
        fogNodePA.removeFromNetwork(deviceCB);
        
        assertFalse(fogNodePA.containsContract(deviceCB.getContract()), "Contract CB has "
                + "been removed, we should not find it! Should return false!");
    }
    
    @Test
    final void testRemoveContractA1FromPolicyPB() {
        init();
        
        assertTrue(fogNodePB.containsContract(deviceA1.getContract()), "Contract A1 is in"
                + "Policy PB, we should find it! Should return true!");
        
        System.setOut(new PrintStream(outContent));
        fogNodePB.removeFromNetwork(deviceA1);
        assertEquals("This contract is necessary for others, this action might "
                + "break other functionalities!", outContent.toString());
        
        assertFalse(fogNodePB.containsContract(deviceA1.getContract()), "Contract A1 has "
                + "been removed, we should not find it! Should return false!");
    }
    
    @Test
    final void testUpdatePolicy() {
        init();
        
        assertTrue(fogNodePA.containsContract(deviceCB.getContract()), "Contract CB is in"
                + "Policy PA, we should find it! Should return true!");
        
        fogNodePA.updatePolicy(new Policy("files/Policy_PB.json"));
        
        assertTrue(fogNodePA.containsContract(deviceCB.getContract()), "Contract CB was in"
                + "Policy PA. Now FogNode contains Policy PB, but when we update we keep"
                + "contracts that are still OK with the new policy. CB is consistent with"
                + "Policy PB so we maintained it. Should return true!");
        
        assertTrue(fogNodePA.containsContract(deviceA1.getContract()), "Now FogNode"
                + "contains Policy PB. Contract A1 is in PB. Should return true!");
        
    }
}
