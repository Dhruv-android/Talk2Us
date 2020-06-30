package com.talk2us.models;

import com.talk2us.utils.Constants;

public class Counsellor {
    public Boolean available;
    public int clients;
    public String id;
    private Boolean status_confirmed;

    @Override
    public String toString() {
        return "Counsellor{" +
                "available=" + available +
                ", clients=" + clients +
                ", id='" + id + '\'' +
                '}';
    }

    public Counsellor() {
        available = true;
        clients = 0;
        id = Constants.NOT_DEFINED;
        status_confirmed = true;
    }

    public Counsellor(Boolean available, int clients, String id, Boolean status_confirmed) {
        this.available = available;
        this.clients = clients;
        this.id = id;
        this.status_confirmed = status_confirmed;
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

    public Boolean getStatus_confirmed() {
        return status_confirmed;
    }

    public void setStatus_confirmed(Boolean status_confirmed) {
        this.status_confirmed = status_confirmed;
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
