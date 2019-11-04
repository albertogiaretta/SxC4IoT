import java.util.ArrayList;

public class IoTDev {
    private Contract contract;
    private FogNode fogNode;
    private ArrayList<Message> receivedMessages;
    
    public IoTDev() {
        contract = new Contract();
        fogNode = new FogNode();
        receivedMessages = new ArrayList<>();
    }
    
    public IoTDev(Contract inputContract) {
        this();
        addContract(inputContract);
    }
    
    public IoTDev(String inputPathContract) {
        this(new Contract(inputPathContract));
    }
    
    public IoTDev(String inputPathContract, FogNode inputFogNode) {
        this(inputPathContract);
        fogNode = inputFogNode;        
    }
    
    private void addContract(Contract inputContract) {
            contract = inputContract;
    }
    
    public Contract getContract() {
        return contract;
    }
    
    //STUB METHOD
    public boolean hasValidPoC() {
        return true;
    }
    
    public void updateContract(Contract newContract) {
        if(newContract.isConsistentContract()
                & this.hasValidPoC()
                & newContract.isCompliantWithPolicy(fogNode.getPolicy())) {
            fogNode.removeFromNetwork(this);
            addContract(newContract);
            fogNode.addToNetwork(this);
        }
    }
    
    public boolean sendMessage(IoTDev recipient, String inputMessage) {
        if(fogNode.containsContract(contract) 
                & fogNode.containsContract(recipient.contract)
                & contract.allowedInformationFlow(recipient.contract))
            return recipient.receiveMessage(this, inputMessage);
        return false;
    }
    
    private boolean receiveMessage(IoTDev sender, String inputMessage) {
        if(fogNode.containsContract(sender.contract)
                & fogNode.containsContract(contract)
                & contract.allowedInformationFlow(sender.contract)) {
            receivedMessages.add(new Message(sender, inputMessage));
            return true;
        }
        return false;
    }
    
    public void printReceivedMessages() {
        for(Message sender : receivedMessages) 
            System.out.println(sender.sender + " sent: " + sender.message);
       
    }
    
    //PACKAGE VISIBILITY FOR TESTING
    void clearReceivedMessages() {
        receivedMessages.clear();
    }
    
    @Override
    public String toString() {
        return contract.toString();
    }
}
