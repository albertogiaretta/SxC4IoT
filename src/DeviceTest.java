import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class DeviceTest {
    ArrayList<Device> testSetOfDevices;

    Device dlink933l;
    Device philipsHueWhite;
    Device philipsHueMotion;

    Device starStar;
    Device appleLukePhone;
    Device appleStar;

    
    private void init() {
        testSetOfDevices = new ArrayList<Device>();
        
        dlink933l = new Device("D-Link", "933L");
        philipsHueWhite = new Device("Philips", "HueWhite");
        philipsHueMotion = new Device("Philips", "HueMotion");

        starStar = new Device("*", "*");
        appleLukePhone = new Device("Apple", "LukePhone");
        appleStar = new Device("Apple", "*");
    }
    
    @Test
    final void testEmptySet() {
        init();
        
        assertFalse(philipsHueWhite.isDeviceInSet(testSetOfDevices), "Set is "
                + "empty, device is actually NOT in there!");
    }
    
    @Test
    final void testTwoDevicesInSet() {
        init();
        
        testSetOfDevices.add(philipsHueWhite);
        testSetOfDevices.add(philipsHueMotion);
        testSetOfDevices.add(dlink933l);

        assertTrue(philipsHueWhite.isDeviceInSet(testSetOfDevices), "Device "
                + "is actually in there!");

        assertFalse(appleLukePhone.isDeviceInSet(testSetOfDevices), "Device "
                + "is actually NOT in there!");
    }
    
    @Test
    final void testApplePhoneInSetWithAppleStar() {
        init();
        
        testSetOfDevices.add(appleStar);
        
        assertTrue(appleLukePhone.isDeviceInSet(testSetOfDevices), "We have "
                + "'Apple.LukePhone in Apple.*', "
                + "device should be counted as in!");
    }
    
    @Test
    final void testApplePhoneInSetWithStarStar() {
        init();
        
        testSetOfDevices.add(starStar);
        
        assertTrue(appleLukePhone.isDeviceInSet(testSetOfDevices), "We have "
                + "'Apple.LukePhone in *.*', device should be counted as in!");
    }
    
    @Test
    final void testStarStarInSetWithAppleLukePhone() {
        init();

        testSetOfDevices.add(appleLukePhone);
        
        assertFalse(starStar.isDeviceInSet(testSetOfDevices), "We have '*.* "
                + "in Apple.LukePhone', device should NOT be counted as in!");
    }
    
    @Test
    final void testStarStarInSetWithAppleStar() {
        init();

        testSetOfDevices.add(appleStar);
        
        assertFalse(starStar.isDeviceInSet(testSetOfDevices), "We have '*.* "
                + "in Apple.*', device should NOT be counted as in!");
    }
    
    @Test
    final void testToString() {
        init();
        
        assertEquals(philipsHueWhite.toString(), "Philips.HueWhite",
                "Function toString() should combine manufacturer and device!");
        
        assertEquals(appleStar.toString(), "Apple.*",
                "Function toString() should combine manufacturer and device!");
        
        assertEquals(starStar.toString(), "*.*",
                "Function toString() should combine manufacturer and device!");
    }

}
