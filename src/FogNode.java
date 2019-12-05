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
        System.out.println("--- Updating the Policy!---");
        Policy tempPolicy = policy.clone();
        policy = newPolicy;
        
        for(int i=0; i<tempPolicy.numberOfContracts(); i++) {
            Contract iterContract = tempPolicy.getContractNumber(i);
            
            addToNetwork(iterContract);
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
    
    public boolean addToNetwork(IoTDev inputDevice) {
        if(inputDevice.hasValidPoC())
            return addToNetwork(inputDevice.getContract());
        return false;
    }
    
    private boolean addToNetwork(Contract inputContract) {
        if(containsContract(inputContract)) {
            System.out.println(inputContract.toString() + " already in network!");
            return true;
        }
        if(inputContract.isConsistentContract()
                & inputContract.isCompliantWithPolicy(policy)
                ) {
            policy.addContract(inputContract);
            System.out.println(inputContract.toString() + " accepted!");
            return true;
        }
        System.out.println(inputContract.toString() + " rejected!");
        return false;
    }
    
    public void removeFromNetwork(IoTDev inputDevice) {
        policy.removeContract(inputDevice.getContract());
    }
    
    public Policy getPolicy() {
        return policy;
    }
    
    public boolean containsContract(Contract inputContract) {
        return policy.containsContract(inputContract);
    }
}
