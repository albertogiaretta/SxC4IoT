
public class Message {
    public IoTDev sender;
    public String message;
    
    public Message() {
        sender = new IoTDev();
        message = new String();
    }
    
    public Message(IoTDev inputSender, String inputMessage) {
        this();
        sender = inputSender;
        message = inputMessage;
    }
}
