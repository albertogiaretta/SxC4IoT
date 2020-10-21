

public class FogNode {
    private Policy policy;
    
    public FogNode() {
        policy = new Policy();
    }
    
    public FogNode(Policy inputPolicy) {
        this();
        addPolicy(inputPolicy);
    }
    
    public FogNode(String inputPathPolicy) {
        this(new Policy(inputPathPolicy));
    }
    
    private void addPolicy(Policy inputPolicy) {
        if(inputPolicy.isConsistentPolicy())
            policy = inputPolicy;
    }
    
    public void updatePolicy(Policy newPolicy) {
        Policy tempPolicy = policy.clone();
        policy = newPolicy;
        
        for(int i=0; i<tempPolicy.numberOfContracts(); i++) {
            Contract iterContract = tempPolicy.getContractNumber(i);

            if(compliantWithPolicy(iterContract)) 
                policy.addContract(iterContract);
        }
    }
    
    //PACKAGE VISIBILITY FOR TESTING
    void clearPolicy() {
        policy = new Policy();
    }
    
    //PACKAGE VISIBILITY FOR TESTING
    int numberOfContracts() {
        return policy.numberOfContracts();
    }
    
    public boolean addContract(IoTDev inputDevice, Contract newContract) {
        Contract tempContract;
        
        if(hasValidPoC(inputDevice, newContract)) {
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
        
        if(compliantWithPolicy(tempContract)) {
            inputDevice.setContract(tempContract);
            policy.addContract(tempContract);
            return true;
        }
        else
            return false;
    }
    
    public boolean updateContract(IoTDev inputDevice, Contract newContract) {
        Contract tempContract;
        
        if(hasValidPoC(inputDevice, newContract)) {
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
        
        if(compliantWithPolicy(tempContract, inputDevice.getContract())) {
            removeFromNetwork(inputDevice);
            inputDevice.setContract(tempContract);
            policy.addContract(tempContract);
            return true;
        }
        else
            return false;
    }
    
    public boolean addDevice(IoTDev inputDevice) {
        if(inputDevice.hasContract() 
                & hasValidPoC(inputDevice)) 
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
        
        if(compliantWithPolicy(inputDevice.getContract())) {
            inputDevice.setContract(inputDevice.getContract());
            policy.addContract(inputDevice.getContract());
            return true;
        }
        else 
            return false;
    }
    
    public boolean updateSoftware(IoTDev inputDevice, Contract newContract) {
        Contract tempContract;
        
        if(newContract.isConsistentContract() 
                & hasValidPoC(inputDevice, newContract)) {
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
        
        if(compliantWithPolicy(tempContract, inputDevice.getContract())) {
            removeFromNetwork(inputDevice);
            inputDevice.setContract(tempContract);
            policy.addContract(tempContract);
            return true;
        }
        else
            return false;
    }
    
    private boolean compliantWithPolicy(Contract inputContract, Contract oldContract) {
        if(inputContract.isCompliantWithPolicy(policy, oldContract))
            return true;
        else 
            return false;
    }
    
    private boolean compliantWithPolicy(Contract inputContract) {
        if(inputContract.isCompliantWithPolicy(policy, inputContract))
            return true;
        else 
            return false;
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
    
    //STUB METHOD
    public boolean hasValidPoC(IoTDev inputDev, Contract newContract) {
        if(inputDev.getPoC() == true & newContract.isConsistentContract())
            return true;
        else
            return false;
    }
    
    //STUB METHOD
    private boolean hasValidPoC(IoTDev inputDev) {
        return hasValidPoC(inputDev, inputDev.getContract());
    }
}
