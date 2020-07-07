import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class IoTDevNoFlowCheck extends IoTDev {
    public IoTDevNoFlowCheck() {
        super();
    }
    
    public IoTDevNoFlowCheck(String inputPathContract, FogNode inputFogNode) {
        super(inputPathContract, inputFogNode);
    }
    
    public IoTDevNoFlowCheck(String inputPathContract, FogNode inputFogNode, 
            String inputLogPath) {
        super(inputPathContract, inputFogNode, inputLogPath);
    }
    
    @Override
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
    
    @Override
    public void messageArrived(String inputTopic, MqttMessage inputMessage) 
            throws IOException {
        Message msg = new Message();
        msg.deserialize(inputMessage);
        
        receivedMessages.add(msg);
        writeToCSV(msg, inputMessage.getPayload());
        //System.out.println(msg.sender + "sent: " + msg.message);
        /*
        System.out.println("Message is: "+ msg.message 
                + "; payload size is: " + inputMessage.getPayload().length 
                + " bytes; message size is: " + msg.message.getBytes().length 
                + "; sender name size is: " + msg.sender.getBytes().length 
                + "; estimated contract size is: " 
                + (inputMessage.getPayload().length 
                        - msg.message.getBytes().length 
                        - msg.sender.getBytes().length));*/
    }
}