import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class ServiceTest {
    ArrayList<Service> emptySet = new ArrayList<Service>();

    Device philipsHueWhite;
    Device philipsHueMotion;
    
    Service servOn;
    Service servPresence;
    Service servFake;
    
    Rule ruleB1;
    Rule ruleA2;
    
    private void init() {
        emptySet = new ArrayList<Service>();

        philipsHueWhite = new Device("Philips", "HueWhite");
        philipsHueMotion = new Device("Philips", "HueMotion");
        
        servOn = new Service(philipsHueWhite, "On");
        servPresence = new Service(philipsHueMotion, "Presence");
        servFake = new Service("Philps", "HueWhite", "FakeService");

        ruleB1 = new Rule("files/Rule_B1.json");
        ruleA2 = new Rule("files/Rule_A2.json");
    }
    
    @Test
    final void testIsServOnInEmptySet() {
        init();
        
        assertFalse(servOn.isServiceInSet(emptySet), "Set is empty, service "
                + "is actually NOT inside this rule!");
    }    
    
    @Test
    final void testIsServOnInRuleB1Provides() {
        init();
        
        assertTrue(servOn.isServiceInSet(ruleB1.provides), "Service "
                + "is actually inside this rule!");
    }
    
    @Test
    final void testIsServOnInRuleA2Requires() {
        init();
        
        assertTrue(servOn.isServiceInSet(ruleA2.requires), "Service "
                + "is actually inside this rule!");
    } 
    
    @Test
    final void testIsServFakeInRuleB1Provides() {
        init();
        
        assertFalse(servFake.isServiceInSet(ruleB1.provides), "Service "
                + "is NOT inside this rule!");
    }
    
    @Test
    final void testToString() {
        init();
        
        assertEquals(servOn.toString(), "Philips.HueWhite.On",
                "Function toString() should combine manufacturer, "
                + "device, and service!");
        
        assertEquals(servPresence.toString(), "Philips.HueMotion.Presence",
                "Function toString() should combine manufacturer, "
                + "device, and service!");
    }
}
