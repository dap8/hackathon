package me.digi.examples.ca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Danni on 07/10/2017.
 */

public class Profile {
    private String id;
    private String name;
    private String diagnosis;
    private String account;
    private int goalAmount;
    private int raisedAmount;
    private Map<String, Message> messages;

    public Profile(){
        this.id = null;
        this.name = null;
        this.diagnosis = null;
        this.account = null;
        this.goalAmount = 0;
        this.raisedAmount = 0;
        this.messages = null;
    }

    public Profile(String name, String diagnosis, String account, int goalAmount){
        this.name = name;
        this.diagnosis = diagnosis;
        this.account = account;
        this.goalAmount = goalAmount;
        this.raisedAmount = 0;
        this.messages = new HashMap<>();
    }

    public Profile(String id, String name, String diagnosis, String account, int goalAmount, int raisedAmount, Map<String, Message> messages){
        this.id = id;
        this.name = name;
        this.diagnosis = diagnosis;
        this.account = account;
        this.goalAmount = goalAmount;
        this.raisedAmount = raisedAmount;
        this.messages = messages;
    }

    public Profile(String name, String diagnosis, String account, int goalAmount, int raisedAmount, Map<String, Message> messages){
        this.name = name;
        this.diagnosis = diagnosis;
        this.account = account;
        this.goalAmount = goalAmount;
        this.raisedAmount = raisedAmount;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public int getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(int goalAmount) {
        this.goalAmount = goalAmount;
    }

    public int getRaisedAmount() {
        return raisedAmount;
    }

    public void setRaisedAmount(int raisedAmount) {
        this.raisedAmount = raisedAmount;
    }

    public Map<String, Message> getMessages() {
        return messages;
    }

    public List<Message> getListedMessages() {
        if(this.messages != null) return new ArrayList<Message>(this.messages.values());
        return null;
    }

    public void setMessages(Map<String, Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message)
    {
        this.messages.put(message.getId(),message);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
