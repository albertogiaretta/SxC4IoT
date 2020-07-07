import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class IoTDevNoSxC extends IoTDev {
    public IoTDevNoSxC() {
        super();
    }
    
    public IoTDevNoSxC(String inputPathContract, FogNode inputFogNode) {
        super(inputPathContract, inputFogNode);
    }
    
    public IoTDevNoSxC(String inputPathContract, FogNode inputFogNode, 
            String inputLogPath) {
        super(inputPathContract, inputFogNode, inputLogPath);
    }
    
    @Override
    public boolean sendMessage(IoTDev recipient, String inputMessage) {
        try {
            if ( !client.isConnected())
                return false;
                
            MessageNoSxC wrapMsg = new MessageNoSxC(this, inputMessage);
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
        MessageNoSxC msg = new MessageNoSxC();
        msg.deserialize(inputMessage);
        
        receivedMessages.add(msg);
        writeToCSV(msg, inputMessage.getPayload());
    }
}