import java.io.Serializable;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class Message implements Serializable {
    public String sender;
    public String message;
    public Contract contract;
    public long timestamp;
    
    public Message() {
        sender = new String();
        message = new String();
        contract = new Contract();
        timestamp = new Date().getTime();
    }
    
    public Message(IoTDev inputSender, String inputMessage) {
        this();
        sender = inputSender.toString();
        message = inputMessage;
        contract = inputSender.getContract();
    }
    
    public byte[] serialize() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);

        return jsonString.getBytes();
    }

    
    public void deserialize(MqttMessage inputMessage) {    
        String jsonString = new String(inputMessage.getPayload());
        Gson gson = new Gson();
        
        sender = gson.fromJson(jsonString, Message.class).sender;
        message = gson.fromJson(jsonString, Message.class).message;
        contract = gson.fromJson(jsonString, Message.class).contract;
        timestamp = gson.fromJson(jsonString, Message.class).timestamp;
    }

}
