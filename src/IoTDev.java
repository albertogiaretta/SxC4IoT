import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.opencsv.CSVWriter;

public class IoTDev implements MqttCallback{
    private Contract contract;
    private FogNode fogNode;
    protected ArrayList<Message> receivedMessages;
    private boolean PoC;
    private String logPath;
    
    protected IMqttClient client;
    protected final int qos = 2;
    
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
    
    public IoTDev(String inputPathContract, FogNode inputFogNode, 
            String inputLogPath) {
        this(inputPathContract, inputFogNode);
        logPath = inputLogPath;        
    }
    
    private void connectToMQTTServer() {
        try {
            client = new MqttClient("tcp://localhost:1883", //"tcp://mqtt.eclipse.org:1883"
                    ""+ThreadLocalRandom.current().nextInt(1,180), 
                    new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(60);
            options.setMaxInflight(10000);
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
    
    public void updateContract(Contract newContract) {
        fogNode.updateContract(this, newContract);
    }
    
    public void updateSoftware(Contract newContract) {
        fogNode.updateSoftware(this, newContract);
    }
    
    public boolean sendMessage(IoTDev recipient, String inputMessage) {
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
    public void messageArrived(String inputTopic, MqttMessage inputMessage) 
            throws IOException {
        Message msg = new Message();
        msg.deserialize(inputMessage);
        
        if(msg.contract.isCompliantWithPolicy(fogNode.getPolicy())) {
            receivedMessages.add(msg);
            
            writeToCSV(msg, inputMessage.getPayload());
        }
        else {
            //Simply discard message
        }
    }
    
    protected void writeToCSV(Message inputMessage, byte[] payloadLength) 
            throws IOException {
        int estimateContractSize = (payloadLength.length 
                - inputMessage.message.getBytes().length 
                - inputMessage.sender.getBytes().length);
        int numberOfRules = 0;
        
        CSVWriter csvWriter = new CSVWriter(new FileWriter("./" + logPath 
                + "/" + inputMessage.sender + "_" + this.toString()
                + ".csv", true));
        
        if(inputMessage.contract != null)
            numberOfRules = inputMessage.contract.getRules().size();
        
        String[] records = {""+inputMessage.timestamp, ""+new Date().getTime(), 
                ""+numberOfRules,
                ""+payloadLength.length , 
                ""+inputMessage.message.getBytes().length, 
                ""+estimateContractSize};
        csvWriter.writeNext(records);
        csvWriter.close();
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
