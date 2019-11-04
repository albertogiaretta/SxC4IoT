import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Rule {
    private Device device;
    private String domain;
    protected ArrayList<Device> shares;
    protected ArrayList<Service> requires;
    protected ArrayList<Service> provides;

    public Rule() {
        shares = new ArrayList<Device>();
        requires = new ArrayList<Service>();
        provides = new ArrayList<Service>();
    }

    public Rule(String pathInput) {
        this();
        try {
            JsonObject ruleAsJson = getRuleAsJsonObject(pathInput);
            copyElementsFromJson(ruleAsJson);
        } catch (JsonIOException | JsonSyntaxException 
                | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
    
    public Rule(JsonObject jsonObjInput) { //USED BY CONTRACT CONSTRUCTOR
        this();
        copyElementsFromJson(jsonObjInput);
    }
    
    private JsonObject getRuleAsJsonObject(String filePathAsString) 
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        JsonParser jsonParser = new JsonParser();
        FileReader fileRead = new FileReader(filePathAsString);
        return jsonParser.parse(fileRead).getAsJsonObject();
    }
    
    private void copyElementsFromJson(JsonObject jsonObjInput) {
        domain = getDomainFromJsonRule(jsonObjInput);
        device = getDeviceFromJsonRule(jsonObjInput);
        shares = getSharesFromJsonRule(jsonObjInput);
        requires = getRequiresFromJsonRule(jsonObjInput);
        provides = getProvidesFromJsonRule(jsonObjInput);
    }
    
    private String getDomainFromJsonRule(JsonObject jsonObjInput) {
        return jsonObjInput.get("domain").getAsString();
    }
    
    private Device getDeviceFromJsonRule(JsonObject jsonObjInput) {
        return new Device(jsonObjInput.get("device").getAsString());
    }
    
    private ArrayList<Device> getSharesFromJsonRule(JsonObject jsonObjInput) {
        JsonArray tempJsonArray = jsonObjInput.get("shares").getAsJsonArray();
        ArrayList<Device> tempArrayList = new ArrayList<Device>();     
 
        if (tempJsonArray != null) {
           for (int i=0;i<tempJsonArray.size();i++) {
               Device tempDevice = 
                       new Device(tempJsonArray.get(i).getAsString());
               tempArrayList.add(tempDevice);
           }
        }
        return tempArrayList;   
    }
    
    private ArrayList<Service> getRequiresFromJsonRule(JsonObject jsonObjInput) {
        JsonArray tempJsonArray = jsonObjInput.get("requires").getAsJsonArray();
        ArrayList<Service> tempArrayList = new ArrayList<Service>();     
 
        if (tempJsonArray != null) { 
           for (int i=0;i<tempJsonArray.size();i++) {
               Service tempService = 
                       new Service(tempJsonArray.get(i).getAsString());
               tempArrayList.add(tempService);
           }
        }
        return tempArrayList;   
    }
    
    private ArrayList<Service> getProvidesFromJsonRule(JsonObject jsonObjInput) {
        JsonArray tempJsonArray = jsonObjInput.get("provides").getAsJsonArray();
        ArrayList<Service> tempArrayList = new ArrayList<Service>();     
 
        if (tempJsonArray != null) { 
           for (int i=0;i<tempJsonArray.size();i++) {
               Service tempService = 
                       new Service(device, tempJsonArray.get(i).getAsString());
               tempArrayList.add(tempService);
           }
        }
        return tempArrayList;
    }
    
    public boolean isWellFormedRule() {
        if(provides.isEmpty() == false & shares.isEmpty()) {
            return false;
        }
        return true;
    }
    
    public boolean isCoreRule(ArrayList<Rule> setOfRulesInput) {
        Rule rule = this;
        
        if(setOfRulesInput.isEmpty())   //In paper we assume Set is not empty
            return true;                //but here we check for it
        
        for(int i=0, j=setOfRulesInput.size(); i<j; i++) {
            Rule ruleStar = setOfRulesInput.get(i);

            if(rule.equals(ruleStar))
                continue;
            
            if(rule.device.equals(ruleStar.device) == false)
                continue;
            
            if(rule.domain.equals(ruleStar.domain) == false)
                continue;

            if(ruleStar.shares.isEmpty()) 
                continue;
                
            if(ruleStar.isSharesSubsetOf(rule) == false)
                continue;

            if(ruleStar.isProvidesSubsetOf(rule) == false)
                continue;
            
            return false;
        }
        return true;
    }
    
    public boolean isSharesSubsetOf(Rule inputRule) {
        for(int i=0, j=shares.size(); i<j; i++) {
            Device deviceInRStarShares = shares.get(i);
        
            if(deviceInRStarShares.isDeviceInSet(inputRule.shares) 
                    == false)
                return false;
            
        }
        return true;
    }
    
    //PACKAGE VISIBILITY FOR TESTING
    boolean isProvidesSubsetOf(Rule inputRule) {
        for(int i=0, j=provides.size(); i<j; i++) {
            Service serviceInRStarProvides = provides.get(i);
        
            if(serviceInRStarProvides.isServiceInSet(inputRule.provides)
                    == false)
                return false;
            
        }
        return true;
    }
    
    public boolean requiresIntersectsInputProvides(Rule inputRule) {
        for(int i=0, j=requires.size(); i<j; i++) {
            Service serviceInRStarRequires = requires.get(i);
            if(serviceInRStarRequires.isServiceInSet(inputRule.provides))
                return true;
        }
        return false;
    }
    
    public boolean isDeviceInShares(Rule ruleInput) {
        return device.isDeviceInShares(ruleInput);
    }

    public boolean domainIntersectsInputDomain(Rule ruleInput) {
        if(ruleInput.domain.equals("*")
                | domain.equals("*")
                | domain.equals(ruleInput.domain)) {
            return true;
        }
        return false;
    }
    
    public boolean criticalForInputContract(Contract inputContract) {
        for(int i=0, j=inputContract.numberOfRules(); i<j; i++) {
            Rule ruleInPolicy = inputContract.getRuleNumber(i);
            
            if(ruleInPolicy.requiresIntersectsInputProvides(this))
                return true;
        }
        return false;
    }
    
    @Override
    public boolean equals(Object objectInput) {
        Rule castObjIn = (Rule) objectInput;
        if(device.equals(castObjIn.device)
                & domain.equals(castObjIn.domain)
                & shares.equals(castObjIn.shares)
                & requires.containsAll(castObjIn.requires)
                & castObjIn.requires.containsAll(requires) 
                & provides.containsAll(castObjIn.provides)
                & castObjIn.provides.containsAll(provides)) {
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return device.toString();
    }
}
