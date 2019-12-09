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
        //System.out.println("--- Updating the Policy!---");
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
        
        if(newContract.isCompliantWithPolicy(policy, newContract)) 
            this.removeFromNetwork(inputDevice);
        
        return this.addToPolicy(newContract);
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
        
        return this.addToPolicy(newContract);
    }
    
    public boolean addDevice(IoTDev inputDevice) {
        if(inputDevice.hasValidPoC()) 
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
        
        return addToPolicy(inputDevice.getContract());
    }
    
    private boolean addToPolicy(Contract inputContract) {
        if(inputContract.isCompliantWithPolicy(policy, inputContract)) {
            policy.addContract(inputContract);
            return true;
        }
        else {
            this.storeInUpdatePool(inputContract);
            return false;
        }

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
