import java.util.ArrayList;

public class IoTDev {
    private Contract contract;
    private FogNode fogNode;
    private ArrayList<Message> receivedMessages;
    private boolean PoC;
    
    public IoTDev() {
        contract = new Contract();
        fogNode = new FogNode();
        receivedMessages = new ArrayList<>();
        PoC = true;
    }
    
    public IoTDev(Contract inputContract) {
        this();
        setContract(inputContract);
    }
    
    public IoTDev(String inputPathContract) {
        this(new Contract(inputPathContract));
    }
    
    public IoTDev(String inputPathContract, FogNode inputFogNode) {
        this(inputPathContract);
        fogNode = inputFogNode;        
    }
    
    public void setContract(Contract inputContract) {
            contract = inputContract;
    }
    
    public Contract getContract() {
        return contract;
    }
    
    public boolean hasContract() {
        if(contract.isEmpty())
            return false;
        else
            return true;
    }
    
    //STUB METHOD
    public boolean hasValidPoC(Contract newContract) {
        if(PoC == true & newContract.isConsistentContract())
            return true;
        else
            return false;
    }
    
    //STUB METHOD
    public boolean hasValidPoC() {
        return hasValidPoC(contract);
    }
    
    public void updateContract(Contract newContract) {
        fogNode.updateContract(this, newContract);
    }
    
    public void updateSoftware(Contract newContract) {
        fogNode.updateSoftware(this, newContract);
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
