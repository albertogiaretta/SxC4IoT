import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class RuleTest {
    ArrayList<Rule> testSetOfRules;

    Rule ruleB1;
    Rule ruleB1bis;
    Rule ruleE1;
    Rule ruleE1inv;
    Rule ruleB2;
    Rule ruleB3;
    Rule ruleMA;
    Rule ruleNoShares;

    public void init() {
        testSetOfRules = new ArrayList<Rule>();
        
        ruleB1 = new Rule("files/Rule_B1.json");
        ruleB1bis = new Rule("files/Rule_B1.json");
        ruleE1 = new Rule("files/Rule_E1.json");
        ruleE1inv = new Rule("files/Rule_E1inv.json");
        ruleB2 = new Rule("files/Rule_B2.json");
        ruleB3 = new Rule("files/Rule_B3.json");
        ruleMA = new Rule("files/Rule_MA.json");
        ruleNoShares = new Rule("files/Rule_NoShares.json");
    }

    @Test
    final void testRuleMAIsWellFormedOrNot() {
        init();

        assertFalse(ruleMA.isWellFormedRule(), "Rule MA is NOT well formed, "
                + "Provides withouth Shares!");
        
        
        ruleMA.provides.clear();
        
        assertTrue(ruleMA.isWellFormedRule(), "Rule is actually well formed, "
                + "Shares is empty but also Provides is!");
        
    }

    @Test
    final void testEmptySet() {
        init();

        assertTrue(ruleB1.isCoreRule(testSetOfRules), "Set is empty, "
                + "the rule does not contradict anything, it should be core!");
        
    }
    
    @Test
    final void testContractBAreCoreRules() {
        init();
        
        testSetOfRules.add(ruleB1);
        testSetOfRules.add(ruleB2);
        
        assertTrue(ruleB1.isCoreRule(testSetOfRules), "This is contract CB, "
                + "it should be core!");
        
        
        assertTrue(ruleB2.isCoreRule(testSetOfRules),"This is contract CB, "
                + "it should be core!");
        
    }
    
    @Test
    final void testRuleIsCoreAgainstAnotherRuleWithNoShares() {
        init();

        testSetOfRules.add(ruleNoShares);
        
        assertTrue(ruleB1.isCoreRule(testSetOfRules), "The other rule shares"
                + "with none, so Rule B1 should be core!");
        
    }
    
    @Test
    final void testContractIBAreNotCoreRules() {
        init();

        testSetOfRules.add(ruleB2);
        testSetOfRules.add(ruleB3);
        
        assertFalse(ruleB2.isCoreRule(testSetOfRules), "Rule B2 is restricted "
                + "by Rule B3, B2 should NOT be core");
        
        
        assertTrue(ruleB3.isCoreRule(testSetOfRules), "Rule B3 is NOT "
                + "restricted by Rule B2, but it RESTRICTS Rule B2, "
                + "therefore B3 IS core (not whole IB contract)");
        
    }
    
    @Test
    final void testOneRuleIsCoreRule() {
        init();
        
        testSetOfRules.add(ruleB1);
        assertTrue(ruleB1.isCoreRule(testSetOfRules), "Set contains only "
                + "rule B1 itself, it should be core!");
        
    }

    @Test
    final void testisProvidesSubsetOf() {
        init();
        
        assertTrue(ruleB1.isProvidesSubsetOf(ruleB1), "Rule B1[Provides] "
                + "SHOULD be subset of itself!");
        
        assertTrue(ruleB2.isProvidesSubsetOf(ruleB1), "Rule B2[Provides] "
                + "SHOULD be subset of B1[Provides] !");
        
        
        assertFalse(ruleB1.isProvidesSubsetOf(ruleB2),"Rule B1[Provides] "
                + "SHOULD NOT be subset of B2[Provides]!");
        
    }
    
    @Test
    final void testisSharesSubsetOf() {
        init();
        
        assertTrue(ruleB1.isSharesSubsetOf(ruleB1), "Rule B1[Shares] "
                + "SHOULD be subset of itself!");
        
        
        assertFalse(ruleB2.isSharesSubsetOf(ruleB1), "Rule B2[Shares] "
                + "SHOULD NOT be subset of B1[Shares] !");
        
        assertTrue(ruleB1.isSharesSubsetOf(ruleB2), "Rule B1[Shares] "
                + "SHOULD be subset of B2[Shares]!");
        
    }
    
    @Test
    final void testEquals() {
        init();
        
        assertTrue(ruleB1.equals(ruleB1bis), "Rule B1 and Rule B1bis "
                + "have the same content but are two different instances."
                + "Equals should return true!");
    }
    
    @Test
    final void testEqualsInverted() {
        init();
        
        assertTrue(ruleE1.equals(ruleE1inv), "Rule E1 and Rule E1inv "
                + "have the same content but the order is changed."
                + "Equals should return true!");
    }  
}
