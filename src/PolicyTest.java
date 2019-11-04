import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PolicyTest {
    Policy policyP;
    Policy policyPA;
    Policy policyPB;
    Policy policyPIB;
    
    Contract contractCD1;
    Contract contractCD2;
    Contract contractCB;
    
    Device philipsHueWhite;
    
    void init() {
        policyP = new Policy();
        policyPA = new Policy("files/Policy_PA.json");
        policyPB = new Policy("files/Policy_PB.json");
        policyPIB = new Policy("files/Policy_PIB.json");
        
        contractCD1 = new Contract("files/Contract_D1.json");
        contractCD2 = new Contract("files/Contract_D2.json");
        contractCB = new Contract("files/Contract_CB.json");
        
        philipsHueWhite = new Device("Philips", "HueWhite");
    }
    
    @Test
    final void testIsInconsistentPolicy() {
        init();
        policyP.addContract(contractCD1);
        policyP.addContract(contractCD2);
        
        assertFalse(policyP.isConsistentPolicy(),
                "There is an Illegal Information Exchange, " 
                + "PF should not be consistent!");
    }
    
    @Test
    final void testIsConsistentPolicy() {
        init();
        policyP.addContract(contractCD1);
        policyP.addContract(contractCD2);
        
        contractCD1.getRuleNumber(0).shares.clear();
        contractCD1.getRuleNumber(0).shares.add(philipsHueWhite);
        
        assertTrue(policyP.isConsistentPolicy(),
                "There is no Illegal Information Exchange whatsoever, " 
                + "PF should be consistent!");
    }
    
    @Test
    final void testPAIsConsistent() {
        init();

        assertTrue(policyPA.isConsistentPolicy(),
                "There is no Illegal Information Exchange whatsoever, " 
                + "PA should be consistent!");
    }
    
    @Test
    final void testPBIsConsistent() {
        init();
        
        assertTrue(policyPB.isConsistentPolicy(),
                "There is no Illegal Information Exchange whatsoever, " 
                + "PB should be consistent!");
    }
    
    @Test
    final void testPIBIsInconsistent() {
        init();

        assertFalse(policyPIB.isConsistentPolicy(),
                "Rule B2 restricts RuleAdmin2, PIB should be inconsistent!");
    }
    
    @Test
    final void testRemoveContractFromPolicyPA() {
        init();
        
        assertTrue(policyPA.containsContract(contractCB), "Policy PA contains "
                + "Contract A1 (with Rule A1) and Contract CB."
                + "This should return true!");
        
        assertEquals(policyPA.numberOfContracts(), 2, "Policy PA contains "+ "2 contracts!");
        
        policyPA.removeContract(contractCB);
        
        assertFalse(policyPA.containsContract(contractCB), "We have just "
                + "removed Contract CB, PA should NOT contain it!");
        
        assertEquals(policyPA.numberOfContracts(), 1, "Policy PA contains "+ "2 contracts!");
    }
    
    @Test
    final void testRemoveContractFromPolicyEmpty() {
        init();
        
        assertEquals(policyP.numberOfContracts(), 0, "Policy PF contains "+ "0 contracts!");
        
        policyP.removeContract(contractCB);

        assertEquals(policyP.numberOfContracts(), 0, "Policy PA contains "+ "0 contracts!");
    }
    
    @Test
    final void testContainsContract() {
        init();
        
        assertTrue(policyPA.containsContract(contractCB), "Policy PA contains "
                + "Contract A1 (with Rule A1) and Contract CB."
                + "This should return true!");

        assertFalse(policyPA.containsContract(contractCD1), "Policy PA contains"
                + "Contract A1 (with Rule A1) and Contract CB. NOT Contract CD1."
                + "This should return false!");
    }
}
