import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Contract {
    private ArrayList<Rule> rulesInContract;
    private boolean verified;
    private boolean extracted;
    
    public Contract() {
        rulesInContract = new ArrayList<Rule>();
        verified = false;
        extracted = false;
    }
    
    public Contract(String pathInput) {
        this();
        try {
            JsonObject contractAsJson = getContractAsJsonObject(pathInput);
            addRulesFromJsonContract(contractAsJson);
            
        } catch (JsonIOException | JsonSyntaxException 
                | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public Contract(JsonObject jsonObjInput) { //USED BY POLICY CONSTRUCTOR
        this();
        addRulesFromJsonContract(jsonObjInput);
    }
    
    private JsonObject getContractAsJsonObject(String filePathAsString) 
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        JsonParser jsonParser = new JsonParser();
        FileReader fileRead = new FileReader(filePathAsString);
        return jsonParser.parse(fileRead).getAsJsonObject();
    }
    
    private void addRulesFromJsonContract(JsonObject jsonObjInput) {
        JsonArray tempJsonArray = jsonObjInput.get("rules").getAsJsonArray();
 
        if (tempJsonArray != null) { 
           for (int i=0;i<tempJsonArray.size();i++) {
               Rule tempRule = 
                       new Rule(tempJsonArray.get(i).getAsJsonObject());
               addRule(tempRule);
           }
        }
    }

    //PACKAGE VISIBILITY FOR TESTING
    void addRule(Rule ruleInput) {
        rulesInContract.add(ruleInput);    
    }
    
    //PACKAGE VISIBILITY FOR TESTING
    ArrayList<Rule> getRules() {
        return rulesInContract;
    }
    
    public void markVerified() {
        verified = true;
        extracted = false;
    }
    
    public void markExtracted() {
        verified = false;
        extracted = true;
    }
    
    public boolean isVerified() {
        return verified;
    }
    
    public boolean isExtracted() {
        return extracted;
    }
    
    public boolean isConsistentContract() {
        for(int i=0, j=numberOfRules(); i<j; i++) {
            Rule ruleInContract = getRuleNumber(i);
            if(ruleInContract.isWellFormedRule() == false)
                return false;
            if(ruleInContract.isCoreRule(rulesInContract) == false)
                return false;
        }
    return true;
    }

    public int numberOfRules() {
        return rulesInContract.size();
    }
    
    public boolean isCompliantWithPolicy(Policy inputPolicy) {
        Policy tempPolicy = inputPolicy;

        if(inputPolicy.isEmpty())
            return true;

        tempPolicy.addContract(this);
        
        return tempPolicy.isConsistentPolicy();
        }
    
    public boolean isCompliantWithPolicy(Policy inputPolicy, Contract oldContract) {
        Policy tempPolicy = new Policy();
        for(int i=0, j=inputPolicy.numberOfContracts(); i<j; i++) {
            Contract iteratedContract = inputPolicy.getContractNumber(i);
            if(iteratedContract != oldContract)
                tempPolicy.addContract(iteratedContract);
        }
    
    return isCompliantWithPolicy(tempPolicy);
    }
    
    public Rule getRuleNumber(int inputNumber) {
        return rulesInContract.get(inputNumber);
    }
    
    public boolean criticalForOtherContracts(Policy policyInput) {
        for(int i=0, j=numberOfRules(); i<j; i++) {
            Rule ruleOfRemovedContract = getRuleNumber(i);
            
            for(int k=0, m=policyInput.numberOfContracts(); k<m; k++) {
                Contract contractInPolicy = policyInput.getContractNumber(k);
                
                if(ruleOfRemovedContract.criticalForInputContract(contractInPolicy))
                    return true;
            }
        }
        return false;
    }

    public boolean illegalInformationExchange(Policy policyInput) {
        for(int i=0, j=policyInput.numberOfContracts(); i<j; i++) {
            Contract contractInPolicy = policyInput.getContractNumber(i);
            if(equals(contractInPolicy) == false) {
                if(forbiddenInformationFlow(contractInPolicy) 
                        | contractInPolicy.forbiddenInformationFlow(this)) 
                    return true;
            }
        }
        return false;
    }

    public boolean allowedInformationFlow(Contract contractInput) {
        return !forbiddenInformationFlow(contractInput);
    }
    
    private boolean forbiddenInformationFlow(Contract contractInput) {
        if(allowedDirectCommunication(contractInput) == false)
            return false;         
            
        for(int i=0, j=numberOfRules(); i<j; i++) {
            Rule ruleStar = getRuleNumber(i);
                
            for(int k=0, m=contractInput.numberOfRules(); k<m; k++) {
                Rule ruleCircle = contractInput.getRuleNumber(k);
                        
                 if(ruleStar.domainIntersectsInputDomain(ruleCircle) 
                         & ruleStar.requiresIntersectsInputProvides(ruleCircle) 
                         & ruleStar.isSharesSubsetOf(ruleCircle) == false)
                     return true;
            }
        }
        
        return false;
    }
    
    private boolean allowedDirectCommunication(Contract contractInput) {
        if(directCommunication(contractInput) == false)
            return false;
        
        for(int i=0, j=numberOfRules(); i<j; i++) {
            Rule ruleStar = getRuleNumber(i);
                
            for(int k=0, m=contractInput.numberOfRules(); k<m; k++) {
                Rule ruleCircle = contractInput.getRuleNumber(k);
                    
                if(ruleStar.domainIntersectsInputDomain(ruleCircle) 
                        & ruleStar.requiresIntersectsInputProvides(ruleCircle)
                        & ruleStar.isDeviceInShares(ruleCircle) == false)
                    return false;
            }
        }
        
        return true;
    }

    private boolean directCommunication(Contract contractInput) {
        for(int i=0, j=numberOfRules(); i<j; i++) {
            Rule ruleStar = getRuleNumber(i);
            
            for(int k=0, m=contractInput.numberOfRules(); k<m; k++) {
                Rule ruleCircle = contractInput.getRuleNumber(k);
                
                if(ruleStar.domainIntersectsInputDomain(ruleCircle) 
                        & ruleStar.requiresIntersectsInputProvides(ruleCircle))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object objectInput) {
        Contract castObjectInput = (Contract) objectInput;
        if(rulesInContract.containsAll(castObjectInput.rulesInContract)
                & castObjectInput.rulesInContract.containsAll(rulesInContract))
            return true;
        return false;
    }
    
    @Override
    public String toString() {
        return rulesInContract.get(0).toString();
    }
}