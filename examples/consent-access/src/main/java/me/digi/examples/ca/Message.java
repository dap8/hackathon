package me.digi.examples.ca;

/**
 * Created by Danni on 07/10/2017.
 */

public class Message {
    private String name;
    private String message;
    private String id;
    private int amount;

    public Message() {
        this.name = null;
        this.message = null;
        this.id = null;
        this.amount = 0;
    }

    public Message(String name, String message, int amount) {

        this.name = name;
        this.message = message;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
