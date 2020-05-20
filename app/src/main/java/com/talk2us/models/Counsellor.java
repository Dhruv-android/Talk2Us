package com.talk2us.models;

public class Counsellor {
    public Boolean available;
    public int clients;
    public String id;
    public Counsellor(){
        available=true;
        clients=0;
        id="Not available";
    }
    public Counsellor(Boolean available, int clients, String id) {
        this.available = available;
        this.clients = clients;
        this.id = id;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
