package com.talk2us.models;

public class Client {
    public String phone;
    public String clientId;
    public String counsellorId;
    public String getPhone() {
        return phone;
    }

    public String getCounsellorId() {
        return counsellorId;
    }

    public void setCounsellorId(String counsellorId) {
        this.counsellorId = counsellorId;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Client(String client_id, String phone, String counsellorId) {
        this.phone = phone;
        this.counsellorId = counsellorId;
    }

    public Client(){
    }
}
