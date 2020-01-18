

public class FogNode {
    private Policy policy;
    //private ArrayList<WaitingDevice> addContractPool;
    //private ArrayList<WaitingDevice> updateContractPool;
    //private ArrayList<WaitingDevice> addDevicePool;
    //private ArrayList<WaitingDevice> updateSoftwarePool;
    
    public FogNode() {
        policy = new Policy();
        //addContractPool = new ArrayList<>();
        //updateContractPool = new ArrayList<>();
        //addDevicePool = new ArrayList<>();
        //updateSoftwarePool = new ArrayList<>();
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
            
            //addToPolicy(iterContract);
            
            if(compliantWithPolicy(iterContract)) 
                policy.addContract(iterContract);
        }
    }
    
    //public void storeInUpdatePool(Contract newContract) {
    //    updatePool.add(newContract);
    //}
    
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
        
        if(compliantWithPolicy(tempContract)) {
            inputDevice.setContract(tempContract); //SPOSTARE QUESTE FUNZIONI SU IoTDev!
            policy.addContract(tempContract);
            //addToPolicy(tempContract);
            return true;
        }
        else {
            //WaitingDevice waitingDev = new WaitingDevice(inputDevice, tempContract);
            //addContractPool.add(waitingDev);
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
        
        if(compliantWithPolicy(tempContract)) {
            removeFromNetwork(inputDevice);
            inputDevice.setContract(tempContract);
            policy.addContract(tempContract);
            return true;
        }
        else {
            //WaitingDevice waitingDev = new WaitingDevice(inputDevice, tempContract);
            //updateContractPool.add(waitingDev);
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
        
        if(compliantWithPolicy(inputDevice.getContract())) {
            inputDevice.setContract(inputDevice.getContract());
            policy.addContract(inputDevice.getContract());
            return true;
        }
        else {
            //WaitingDevice waitingDev = new WaitingDevice(inputDevice, inputDevice.getContract());
            //addDevicePool.add(waitingDev);
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
        
        if(compliantWithPolicy(tempContract)) {
            inputDevice.setContract(tempContract);
            policy.addContract(tempContract);
            return true;
        }
        else {
            //WaitingDevice waitingDev = new WaitingDevice(inputDevice, tempContract);
            //updateSoftwarePool.add(waitingDev);
            return false;
        }
    }
    
    private boolean compliantWithPolicy(Contract inputContract) {
        if(inputContract.isCompliantWithPolicy(policy, inputContract))
            return true;
        else 
            return false;
    }
/*
    private void addToPolicy(Contract inputContract) {
        if(canAddToPolicy(inputContract)) 
            policy.addContract(inputContract);
        else 
            storeInUpdatePool(inputContract);
    }*/
    
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
