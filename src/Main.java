
public class Main {
    static FogNode fogNodePFlegal = new FogNode("files/Policy_PFlegal.json");
    
    static IoTDev deviceC = new IoTDev("files/Contract_D5.json", fogNodePFlegal);
    static IoTDev deviceD = new IoTDev("files/Contract_D2.json", fogNodePFlegal);
    
    public static void main(String[] args) {
        deviceC.sendMessage(deviceD, "Hello there!");
        deviceC.sendMessage(deviceD, "This is a second message");
        deviceC.sendMessage(deviceD, "And this is a third one!");
        deviceC.sendMessage(deviceD, "It's getting annoying...");
        deviceD.printReceivedMessages();
        
        System.out.println("Updating the network policy...");
        fogNodePFlegal.updatePolicy(new Policy("files/Policy_PF.json"));
        deviceD.clearReceivedMessages();
        
        deviceC.sendMessage(deviceD, "Hello there!");
        deviceC.sendMessage(deviceD, "This is a second message");
        deviceC.sendMessage(deviceD, "And this is a third one!");
        deviceC.sendMessage(deviceD, "It's getting annoying...");
        deviceD.printReceivedMessages();
    }
}
