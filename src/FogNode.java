import java.util.ArrayList;

public class FogNode {
    private Policy policy;
    private ArrayList<Contract> updatePool;
    
    public FogNode() {
        policy = new Policy();
        updatePool = new ArrayList<>();
    }
    
    public FogNode(Policy inputPolicy) {
        this();
        addPolicy(inputPolicy);
    }
    
    public void addPolicy(Policy inputPolicy) {
        if(inputPolicy.isConsistentPolicy())
            policy = inputPolicy;
    }
    
    public FogNode(String inputPathPolicy) {
        this(new Policy(inputPathPolicy));
    }
    
    public void updatePolicy(Policy newPolicy) {
        Policy tempPolicy = policy.clone();
        policy = newPolicy;
        
        for(int i=0; i<tempPolicy.numberOfContracts(); i++) {
            Contract iterContract = tempPolicy.getContractNumber(i);
            
            addToPolicy(iterContract);
        }
    }
    
    public void storeInUpdatePool(Contract newContract) {
        updatePool.add(newContract);
    }
    
    //PACKAGE VISIBILITY FOR TESTING
    void clearPolicy() {
        policy = new Policy();
    }
    
    public int numberOfContracts() {
        return policy.numberOfContracts();
    }
    
    public boolean addContract(IoTDev inputDevice, Contract newContract) {
        if(inputDevice.hasValidPoC(newContract)) 
            newContract.markVerified();  
        else {
            new Dbac();
            if(Dbac.canExtractContract(inputDevice)) {
                Dbac.extractContract(inputDevice);
                inputDevice.getContract().markExtracted();
            }
            else
                return false;
        }
        
        if(canAddToPolicy(newContract)) {
            inputDevice.updateContract(newContract);
            addToPolicy(newContract);
            return true;
        }
        else {
            storeInUpdatePool(newContract);
            return false;
        }
    }
    
    public boolean updateContract(IoTDev inputDevice, Contract newContract) {
        if(inputDevice.hasValidPoC(newContract)) 
            newContract.markVerified();  
        else {
            new Dbac();
            if(inputDevice.getContract().isVerified() == false 
                    & Dbac.canExtractContract(inputDevice)) {
                Dbac.extractContract(inputDevice);
                inputDevice.getContract().markExtracted();
            }
            else
                return false;
        }
        
        if(canAddToPolicy(newContract)) {
            removeFromNetwork(inputDevice);
            inputDevice.updateContract(newContract);
            addToPolicy(newContract);
            return true;
        }
        else {
            storeInUpdatePool(newContract);
            return false;
        }
    }
    
    public boolean addDevice(IoTDev inputDevice) {
        if(inputDevice.hasContract() 
                & inputDevice.hasValidPoC()) 
            inputDevice.getContract().markVerified();
        else {
            new Dbac();
            if(Dbac.canExtractContract(inputDevice)) {
                Dbac.extractContract(inputDevice);
                inputDevice.getContract().markExtracted();
            }
            else
                return false;
        }
        
        if(canAddToPolicy(inputDevice.getContract())) {
            inputDevice.updateContract(inputDevice.getContract());
            addToPolicy(inputDevice.getContract());
            return true;
        }
        else {
            storeInUpdatePool(inputDevice.getContract());
            return false;
        }
    }
    
    public boolean updateSoftware(IoTDev inputDevice) {
        if(inputDevice.hasContract() 
                & inputDevice.hasValidPoC(inputDevice.getContract())) 
            inputDevice.getContract().markVerified();  
        else {
            new Dbac();
            if(inputDevice.getContract().isVerified() == false 
                    & Dbac.canExtractContract(inputDevice)) {
                Dbac.extractContract(inputDevice);
                inputDevice.getContract().markExtracted();
            }
            else
                return false;
        }
        
        if(canAddToPolicy(inputDevice.getContract())) {
            inputDevice.updateContract(inputDevice.getContract());
            addToPolicy(inputDevice.getContract());
            return true;
        }
        else {
            storeInUpdatePool(inputDevice.getContract());
            return false;
        }
    }
    
    private boolean canAddToPolicy(Contract inputContract) {
        if(inputContract.isCompliantWithPolicy(policy, inputContract))
            return true;
        else 
            return false;
    }

    private void addToPolicy(Contract inputContract) {
        if(canAddToPolicy(inputContract)) 
            policy.addContract(inputContract);
        else 
            storeInUpdatePool(inputContract);
    }
    
    public void removeFromNetwork(IoTDev inputDevice) {
        this.removeFromNetwork(inputDevice.getContract());
    }
    
    public void removeFromNetwork(Contract inputContract) {
        policy.removeContract(inputContract);
    }
    
    public Policy getPolicy() {
        return policy;
    }
    
    public boolean containsContract(Contract inputContract) {
        return policy.containsContract(inputContract);
    }
}
