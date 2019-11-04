import java.util.ArrayList;

public class Device {
    private String manufacturerName;
    private String deviceName;
    
    public Device (String manufacturerInput, String deviceInput) {
        manufacturerName = manufacturerInput;
        deviceName = deviceInput;
    }

    public Device(String manufacturerAndDeviceInput) {
        String[] inputAsArray = manufacturerAndDeviceInput.split("\\.", 0);
        manufacturerName = inputAsArray[0];
        deviceName = inputAsArray[1];
    }
    
    public boolean isDeviceInShares(Rule ruleInput) {
        return isDeviceInSet(ruleInput.shares);
    }
    
    public boolean isDeviceInSet(ArrayList<Device> setOfDevicesInput) {
        for(int i=0, j=setOfDevicesInput.size(); i<j; i++) {
            Device iteratedDevice = setOfDevicesInput.get(i);
            
            if(isDeviceIn(iteratedDevice))
                return true;

        }
    return false; 
    }
    
    private boolean isDeviceIn(Device deviceInput) {
        if(deviceInput.manufacturerName.equals("*") 
                & deviceInput.deviceName.equals("*")) {
            return true;
        }
               
        if(manufacturerName.equals(deviceInput.manufacturerName) 
                & deviceInput.deviceName.equals("*")) {
            return true;
        }
        
        if(equals(deviceInput))
            return true;

        return false; 
    }

    @Override
    public boolean equals(Object objectInput) {
        Device castObjectInput = (Device) objectInput;
        if(manufacturerName.equals(castObjectInput.getManufacturerName()) 
                & deviceName.equals(castObjectInput.getDeviceName()))
            return true;
        return false;
    }
    
    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getDeviceName() {
        return deviceName;
    }
    
    @Override
    public String toString() {
        return manufacturerName + "." + deviceName;
    }
}