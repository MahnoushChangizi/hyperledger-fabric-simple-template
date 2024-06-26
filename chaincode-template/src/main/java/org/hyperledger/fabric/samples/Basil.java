package org.example.chaincode;

import com.google.gson.Gson;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.ArrayList;
import java.util.List;

@DataType()
public class Basil {
    @Property()
    private String qrCode;
    @Property()
    private Owner owner;
    @Property()
    private String currentState;
    @Property()
    private List<BasilLeg> history;

    public Basil() {
        // Default constructor required for deserialization
        this.history = new ArrayList<>();
    }

    public Basil(String qrCode, Owner owner, String currentState) {
        this.qrCode = qrCode;
        this.owner = owner;
        this.currentState = currentState;
        this.history = new ArrayList<>();
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public List<BasilLeg> getHistory() {
        return history;
    }

    public void setHistory(List<BasilLeg> history) {
        this.history = history;
    }

    public String toJSONString() {
        return new Gson().toJson(this);
    }

    public static Basil fromJSONString(String json) {
        return new Gson().fromJson(json, Basil.class);
    }
}