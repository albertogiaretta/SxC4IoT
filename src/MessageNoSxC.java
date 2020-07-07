import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class MessageNoSxC extends Message {
    
    public MessageNoSxC() {
        sender = new String();
        message = new String();
        //contract = null;
        timestamp = new Date().getTime();
    }
    
    public MessageNoSxC(IoTDev inputSender, String inputMessage) {
        this();
        sender = inputSender.toString();
        message = inputMessage;
    }

    public void deserialize(MqttMessage inputMessage) {    
        String jsonString = new String(inputMessage.getPayload());
        Gson gson = new Gson();
        
        sender = gson.fromJson(jsonString, Message.class).sender;
        message = gson.fromJson(jsonString, Message.class).message;
        timestamp = gson.fromJson(jsonString, Message.class).timestamp;
        //System.out.println("Sender payload is: " + sender.getBytes().length + " bytes");
        //System.out.println("Message payload is: " + message.getBytes().length + " bytes");
    }
}
