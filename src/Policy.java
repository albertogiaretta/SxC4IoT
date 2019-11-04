import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Policy {
    private ArrayList<Contract> contractsInPolicy;
    
    public Policy() {
        contractsInPolicy = new ArrayList<Contract>();
    }
    
    public Policy(String pathInput) {
        this();
        try {
            JsonObject policyAsJson = getPolicyAsJsonObject(pathInput);
            addContractsFromJsonPolicy(policyAsJson);
        } catch (JsonIOException | JsonSyntaxException 
                | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private JsonObject getPolicyAsJsonObject(String filePathAsString) 
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        JsonParser jsonParser = new JsonParser();
        FileReader fileRead = new FileReader(filePathAsString);
        return jsonParser.parse(fileRead).getAsJsonObject();
    }
    
    
    public boolean isConsistentPolicy() {
        ArrayList<Rule> tempRuleSet = new ArrayList<Rule>();
    
        for(int i=0, j=numberOfContracts(); i<j; i++) {
            Contract contractInPolicy = getContractNumber(i);
            
            if(contractInPolicy.illegalInformationExchange(this))
                return false;
            tempRuleSet.addAll(contractInPolicy.getRules());
        }
        
        for(int i=0, j=tempRuleSet.size(); i<j; i++) {
            Rule ruleInPolicy = tempRuleSet.get(i);
            
            if(ruleInPolicy.isWellFormedRule() == false)
                return false;
            if(ruleInPolicy.isCoreRule(tempRuleSet) == false)
                return false;
        }
        
        return true;
    }

    //PACKAGE VISIBILITY FOR TESTING
    void addContract(Contract contractInput) {
        contractsInPolicy.add(contractInput);
    }
    
    //PACKAGE VISIBILITY FOR TESTING
    int numberOfContracts() {
        return contractsInPolicy.size();
    }
    
    //PACKAGE VISIBILITY FOR TESTING
    public Contract getContractNumber(int inputNumber) {
        return contractsInPolicy.get(inputNumber);
    }
    
    public boolean containsContract(Contract inputContract) {
        if(findContract(inputContract) != null)
            return true;
        return false;
    }
    
    private Contract findContract(Contract inputContract) {
        for(int i=0, j=numberOfContracts(); i<j; i++) {
            Contract contractInPolicy = getContractNumber(i);
            
            if(contractInPolicy.equals(inputContract))
                return contractInPolicy; 
        }
        return null;
    }
    
    public void removeContract(Contract inputContract) {
        if(findContract(inputContract) != null) {
            if(inputContract.criticalForOtherContracts(this))
                System.out.print("This contract is necessary for others, "
                        + "this action might break other functionalities!");
            contractsInPolicy.remove(findContract(inputContract));
        }
    }
    /*
    public boolean consistentWithInputContract(Contract inputContract) {
        Policy tempPolicy = this.clone();
        
        if(this.contractsInPolicy.isEmpty())
            return true;

        tempPolicy.addContract(inputContract);
            
        return tempPolicy.isConsistentPolicy();
    }*/
    
    private void addContractsFromJsonPolicy(JsonObject jsonObjInput) {
        JsonArray tempJsonArray = jsonObjInput.get("contracts").getAsJsonArray();
 
        if (tempJsonArray != null) { 
           for (int i=0;i<tempJsonArray.size();i++) {
               Contract tempContract = 
                       new Contract(tempJsonArray.get(i).getAsJsonObject());
               addContract(tempContract);
           }
        }
    }
    
    public boolean isEmpty() {
        if(this.contractsInPolicy.isEmpty())
            return true;
        return false;
    }

    @Override
    public Policy clone() {
        Policy tempPolicy = new Policy();

        for(int i=0, j=numberOfContracts(); i<j; i++) {
            Contract contractInPolicy = getContractNumber(i);
            
            tempPolicy.contractsInPolicy.add(contractInPolicy);
        }
        return tempPolicy;
    }
}
