import java.util.ArrayList;

public class Service {
    private Device deviceAndManufacturerName;
    private String serviceName;
    
    public Service(String manufacturerInput, String deviceInput, 
            String serviceInput) {
        deviceAndManufacturerName = new Device(manufacturerInput, deviceInput);
        serviceName = serviceInput;
      }
    
    public Service(Device composedDevAndManInput, String serviceInput) {
        this(composedDevAndManInput.getManufacturerName(), 
                composedDevAndManInput.getDeviceName(), serviceInput);
      }
    
    public Service(String manufacturerAndDeviceAndServiceInput) {
        String[] inputAsArray = manufacturerAndDeviceAndServiceInput.split("\\.", 0);
        deviceAndManufacturerName = new Device(inputAsArray[0], inputAsArray[1]);
        serviceName = inputAsArray[2];
    }
    
    public boolean isServiceInSet(ArrayList<Service> setOfServicesInput) {
        return setOfServicesInput.contains(this);
    }
    
    @Override
    public boolean equals(Object objectInput) {
        Service castObjectInput = (Service) objectInput;
        if(serviceName.equals(castObjectInput.serviceName) &
                castObjectInput.getDeviceAndManufacturer().equals(getDeviceAndManufacturer())) {
            return true;
        }
        
        return false;
    }
    
    private String getDeviceAndManufacturer() {
        return deviceAndManufacturerName.toString();
    }
    
    @Override
    public String toString() {
        return deviceAndManufacturerName.toString() + "." + serviceName;
    }    
}
