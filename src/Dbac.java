
public class Dbac {
    
    //STUB METHOD
    public static boolean canExtractContract(IoTDev inputDevice) {
        if(inputDevice.getContract().isEmpty())
            return false;
        else
            return true;
    }
    
    //STUB METHOD, WE PRETEND WE EXTRACTED THE CONTRACT
    public static Contract extractContract(IoTDev inputDevice) {
        if(canExtractContract(inputDevice))
            return inputDevice.getContract();
        else
            return null;
    }
}