import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.opencsv.CSVWriter;

public class Main {    
    static String[] records = {"Sent", "Received", "#OfRules", "PayloadSize", 
            "MessageSize", "ContractSize"};
    static String inputPrefix = "files/2rules/";
    static String outputSuffix = "output/";
    
    static int iterations = 100;

    public static void main(String[] args) {
        try {
            ExecuteSxC();
            ExecuteNoSxC();
            ExecuteNoFlowCheck();
        
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void ExecuteSxC() throws IOException {
        final FogNode fogNode = new FogNode();
        final IoTDev deviceC = new IoTDev(inputPrefix + "Contract_D5.json", 
                fogNode, outputSuffix + "WithSxC");
        final IoTDev deviceD = new IoTDev(inputPrefix + "Contract_D2.json", 
                fogNode, outputSuffix + "WithSxC");
        
        fogNode.addDevice(deviceC);
        fogNode.addDevice(deviceD);
        
        WriteOutput(deviceC, deviceD, outputSuffix + "WithSxC");
    }
    
    public static void ExecuteNoSxC() throws IOException {
        final FogNode fogNode = new FogNode();
        final IoTDevNoSxC deviceC2 = new IoTDevNoSxC(inputPrefix 
                + "Contract_D5.json", fogNode, outputSuffix + "WithoutSxC");
        final IoTDevNoSxC deviceD2 = new IoTDevNoSxC(inputPrefix 
                + "Contract_D2.json", fogNode, outputSuffix + "WithoutSxC");
        
        fogNode.addDevice(deviceC2);
        fogNode.addDevice(deviceD2);
        
        WriteOutput(deviceC2, deviceD2, outputSuffix + "WithoutSxC");
    }
    
    public static void ExecuteNoFlowCheck() throws IOException {
        final FogNode fogNode = new FogNode();
        final IoTDevNoFlowCheck deviceC3 = new IoTDevNoFlowCheck(inputPrefix 
                + "Contract_D5.json", fogNode, outputSuffix 
                + "WithContractButNoSxC");
        final IoTDevNoFlowCheck deviceD3 = new IoTDevNoFlowCheck(inputPrefix 
                + "Contract_D2.json", fogNode, outputSuffix 
                + "WithContractButNoSxC");
        
        fogNode.addDevice(deviceC3);
        fogNode.addDevice(deviceD3);
        
        WriteOutput(deviceC3, deviceD3, outputSuffix + "WithContractButNoSxC");
    }
    
    public static void WriteOutput(IoTDev inputDev1, IoTDev inputDev2, 
            String inputPath) throws IOException {
        String s = "ab"; // String long 2 chars
        
        CSVWriter csvWriter = new CSVWriter(new FileWriter("./" + inputPath 
                + "/" + inputDev1.toString() + "_" + inputDev2.toString() 
                + ".csv"));
        csvWriter.writeNext(records);
        csvWriter.close();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        while(s.length() < 8192) {
            for(int i=0; i < iterations; i++)
            {
                inputDev1.sendMessage(inputDev2, s);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            s = s.repeat(2);
        }

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        inputDev1.disconnect();
        inputDev2.disconnect();
    }

}
