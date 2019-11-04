import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContractTest {
    Policy policyP;
    
    Contract contractCB;
    Contract contractCBbis;
    Contract contractCBinv;
    Contract contractIB;
    Contract contractCD1;
    Contract contractCD2;
    Contract contractCD3;
    Contract contractCD4;
    Contract contractCD5;
    Contract contractCB2;
    Contract contractCA3;
    Contract contractCA4;
    
    Rule ruleMA2;
    
    private void init() {
        policyP = new Policy();
        
        contractCB = new Contract("files/Contract_CB.json");
        contractCBbis = new Contract("files/Contract_CB.json");
        contractCBinv = new Contract("files/Contract_CBinv.json");
        contractIB = new Contract("files/Contract_IB.json");
        contractCD1 = new Contract("files/Contract_D1.json");
        contractCD2 = new Contract("files/Contract_D2.json");
        contractCD3 = new Contract("files/Contract_D3.json");
        contractCD4 = new Contract("files/Contract_D4.json");
        contractCD5 = new Contract("files/Contract_D5.json");
        contractCB2 = new Contract("files/Contract_B2.json");
        contractCA3 = new Contract("files/Contract_A3.json");
        contractCA4 = new Contract("files/Contract_A4.json");
        
        ruleMA2 = new Rule("files/Rule_MA2.json");
      }

    @Test
    final void testContractBIsConsistent() {
        init();

        assertTrue(contractCB.isConsistentContract(), "This is contract CB, "
                + "it should be core!");
    }
    
    @Test
    final void testContractBWithMalformedRuleIsInconsistent() {
        init();
        contractCB.addRule(ruleMA2);
        
        assertFalse(contractCB.isConsistentContract(), "This is contract CB, "
                + "but there is a malformed rule, it should NOT be core!");
    }
    
    @Test
    final void testContractIBIsInconsistent() {
        init();
        
        assertFalse(contractIB.isConsistentContract(), "Rule B2 is restricted "
                + "by Rule B3, Contract IB should NOT be consistent");
    }
    
    @Test
    final void testIllegalInformationExchangeD1ToD2() {
        init();
        policyP.addContract(contractCD2);
        
        assertTrue(contractCD1.illegalInformationExchange(policyP),
                "CD1 shares with Apple.LukePhone, but CD2 does NOT, thus" 
                + "we have an Illegal Information Exchange");
    }
    
    @Test
    final void testIllegalInformationExchangeD2ToD1() {
        init();
        policyP.addContract(contractCD1);

        assertTrue(contractCD2.illegalInformationExchange(policyP),
                "CD1 shares with Apple.LukePhone, but CD2 does NOT, thus" 
                + "we have an Illegal Information Exchange");
    }
    
    @Test
    final void testLegalInformationExchangeD2ToD2AndD3() {
        init();
        policyP.addContract(contractCD2);
        policyP.addContract(contractCD3);
        
        assertFalse(contractCD2.illegalInformationExchange(policyP), 
                "CD3 shares with Philips.HueWhite as CD2 does, thus"
                + "we have a Legal Information Exchange");
    }
    
    @Test
    final void testLegalInformationExchangeB2ToD1() {
        init();
        policyP.addContract(contractCD1);
 
        assertFalse(contractCB2.illegalInformationExchange(policyP),
                "CB2 shares with everyone, thus"
                + "we have no Illegal Information Exchange whatsoever");
    }
    
    @Test
    final void testLegalInformationExchangeRuleD2ToA3() {
        init();
        policyP.addContract(contractCA3);

        assertFalse(contractCD2.illegalInformationExchange(policyP),
                "CA3 shares with Apple.LukePhone, CD2 does NOT, but they act" 
                + "on a different domain, we have no Illegal Information "
                + "Exchange");
    }
    
    @Test
    final void testLegalInformationExchangeRuleA3ToD2() {
        init();
        policyP.addContract(contractCD2);

        assertFalse(contractCA3.illegalInformationExchange(policyP),
                "CA3 shares with Apple.LukePhone, CD2 does NOT, but they act" 
                + "on a different domain, we have no Illegal Information "
                + "Exchange");
    }
    
    @Test
    final void testLegalInformationExchangeContractD4toContractCB() {
        init();
        policyP.addContract(contractCB);

        assertFalse(contractCD3.illegalInformationExchange(policyP),
                "we have no Illegal Information "
                + "Exchange");
    }
    
    @Test
    final void testLegalInformationExchangeContractD2toContractD5() {
        init();
        policyP.addContract(contractCD5);

        assertFalse(contractCD2.illegalInformationExchange(policyP),
                "we have no Illegal Information "
                + "Exchange");
    }
    
    @Test
    final void testLegalInformationExchangeRuleA4ToD2() {
        init();
        policyP.addContract(contractCD2);
        
        assertTrue(contractCA4.illegalInformationExchange(policyP),
                "CA4 shares with Apple.LukePhone, CD2 does NOT. CA4 applies to" 
                + "all domains, so we have an Illegal Information Exchange");
    }
    
    @Test
    final void testIllegalInformationExchangeRuleD2ToA4() {
        init();
        policyP.addContract(contractCA4);
        
        assertTrue(contractCD2.illegalInformationExchange(policyP),
                "CA4 shares with Apple.LukePhone, CD2 does NOT. CA4 applies to" 
                + "all domains, so we have an Illegal Information Exchange");
    }
    
    @Test
    final void testNonAllowedDirectCommunication() {
        init();
        policyP.addContract(contractCD1);
        policyP.addContract(contractCD4);
        
        assertFalse(contractCD1.illegalInformationExchange(policyP),
                "CD1[Requires] intersects CD4[Provides] but CD4 does NOT" 
                        + "have CD1 in Shares. Therefore, there is no Allowed"
                        + "Direct Communication");
        
        assertFalse(contractCD4.illegalInformationExchange(policyP),
                "CD1[Requires] intersects CD4[Provides] but CD4 does NOT" 
                + "have CD1 in Shares. Therefore, there is no Allowed"
                + "Direct Communication");
    }
    
    @Test
    final void testEquals() {
        init();
        
        assertTrue(contractCB.equals(contractCBbis), "Contract CB and CBbis "
                + "have the same content but are two different instances."
                + "Equals should return true!");
    }
    
    @Test
    final void testEqualsInverted() {
        init();
        
        assertTrue(contractCB.equals(contractCBinv), "Contract CB and CBinv "
                + "have the same content but are inverted."
                + "Equals should return true!");
    } 
}
