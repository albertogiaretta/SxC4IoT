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
        Contract tempContract;
        
        if(inputDevice.hasValidPoC(newContract)) {
            newContract.markVerified();
            tempContract = newContract;
        }
        else {
            new Dbac();
            if(Dbac.canExtractContract(inputDevice)) {
                tempContract = Dbac.extractContract(inputDevice);
                tempContract.markExtracted();
            }
            else
                return false;
        }
        
        if(canAddToPolicy(tempContract)) {
            inputDevice.addContract(tempContract);
            addToPolicy(tempContract);
            return true;
        }
        else {
            storeInUpdatePool(tempContract);
            return false;
        }
    }
    
    public boolean updateContract(IoTDev inputDevice, Contract newContract) {
        Contract tempContract;
        
        if(inputDevice.hasValidPoC(newContract)) {
            newContract.markVerified();
            tempContract = newContract;
        }
        else {
            new Dbac();
            if(inputDevice.getContract().isVerified() == false 
                    & Dbac.canExtractContract(inputDevice)) {
                tempContract = Dbac.extractContract(inputDevice);
                tempContract.markExtracted();
            }
            else
                return false;
        }
        
        if(canAddToPolicy(tempContract)) {
            removeFromNetwork(inputDevice);
            inputDevice.addContract(tempContract);
            addToPolicy(tempContract);
            return true;
        }
        else {
            storeInUpdatePool(tempContract);
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
            inputDevice.addContract(inputDevice.getContract());
            addToPolicy(inputDevice.getContract());
            return true;
        }
        else {
            storeInUpdatePool(inputDevice.getContract());
            return false;
        }
    }
    
    public boolean updateSoftware(IoTDev inputDevice, Contract newContract) {
        Contract tempContract;
        
        if(newContract.isConsistentContract() 
                & inputDevice.hasValidPoC(newContract)) {
            newContract.markVerified();
            tempContract = newContract;
        }
        else {
            new Dbac();
            if(inputDevice.getContract().isVerified() == false 
                    & Dbac.canExtractContract(inputDevice)) {
                tempContract = Dbac.extractContract(inputDevice);
                tempContract.markExtracted();
            }
            else
                return false;
        }
        
        if(canAddToPolicy(tempContract)) {
            inputDevice.addContract(tempContract);
            addToPolicy(tempContract);
            return true;
        }
        else {
            storeInUpdatePool(tempContract);
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
