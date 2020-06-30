package com.talk2us.models;

public class Client {
    public String phone;
    public String counsellor_id;
    public String getPhone() {
        return phone;
    }

    public String getCounsellorId() {
        return counsellor_id;
    }

    public void setCounsellorId(String counsellorId) {
        this.counsellor_id = counsellorId;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Client(String phone, String counsellorId) {
        this.phone = phone;
        this.counsellor_id = counsellorId;
    }
}
