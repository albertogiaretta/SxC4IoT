import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class IoTDev implements MqttCallback{
    private final int qos = 2;
    private Contract contract;
    private FogNode fogNode;
    private ArrayList<Message> receivedMessages;
    private boolean PoC;
    private IMqttClient client;
    
    public IoTDev() {
        contract = new Contract();
        fogNode = new FogNode();
        receivedMessages = new ArrayList<>();
        PoC = true;
    }
    
    public IoTDev(Contract inputContract) {
        this();
        setContract(inputContract);
        connectToMQTTServer();
    }
    
    public IoTDev(String inputPathContract) {
        this(new Contract(inputPathContract));
    }
    
    public IoTDev(String inputPathContract, FogNode inputFogNode) {
        this(inputPathContract);
        fogNode = inputFogNode;        
    }
    
    private void connectToMQTTServer() {
        try {
            client = new MqttClient("tcp://mqtt.eclipse.org:1883", ""+ThreadLocalRandom.current().nextInt(1,180), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(60);
            client.connect(options);
            client.setCallback(this);
            client.subscribe(contract.getID()+"/in", qos);
            System.out.println("Subscribed to: " + contract.getID() + "/in");
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
    /*
    //STUB METHOD
    public boolean hasValidPoC(Contract newContract) {
        if(PoC == true & newContract.isConsistentContract())
            return true;
        else
            return false;
    }
    
    //STUB METHOD
    private boolean hasValidPoC() {
        return hasValidPoC(contract);
    }*/
    
    public void updateContract(Contract newContract) {
        fogNode.updateContract(this, newContract);
    }
    
    public void updateSoftware(Contract newContract) {
        fogNode.updateSoftware(this, newContract);
    }
    
    public boolean sendMessage(IoTDev recipient, String inputMessage) {
        //if ( !client.isConnected())
        //    connectToMQTTServer();
        if(fogNode.containsContract(contract) 
                & fogNode.containsContract(recipient.contract)
                & contract.allowedInformationFlow(recipient.contract)) {
            try {
                if ( !client.isConnected())
                    return false;
                
                Message wrapMsg = new Message(this, inputMessage);
                MqttMessage msg = new MqttMessage(wrapMsg.serialize());
                msg.setQos(qos);
                client.publish(recipient.getID()+"/in", msg);
                return true;
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Information flow not allowed");
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

    @Override
    public void messageArrived(String inputTopic, MqttMessage inputMessage) {
        Message msg = new Message();
        msg.deserialize(inputMessage);
        
        if(fogNode.containsContract(msg.contract)
                & fogNode.containsContract(contract)
                & contract.allowedInformationFlow(msg.contract)) {
            receivedMessages.add(msg);
            //System.out.println(msg.sender + "sent: " + msg.message);
            System.out.println("Payload size is: " + inputMessage.getPayload().length + " bytes");
        }
        else {
            //Simply discard message
        }
    }
    
    @Override
    public void connectionLost(Throwable arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        // TODO Auto-generated method stub
    }
    
    public boolean getPoC() {
        return PoC;
    }
    
    public String getID() {
        return contract.getID();
    }
    
    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
