package org.example.chaincode;

import com.google.gson.Gson;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class BasilLeg {
    @Property()
    private String timestamp;
    @Property()
    private String location;
    @Property()
    private String temperature;
    @Property()
    private String humidity;

    public BasilLeg() {
        // Default constructor required for deserialization
    }

    public BasilLeg(String timestamp, String location, String temperature, String humidity) {
        this.timestamp = timestamp;
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    // Getters and Setters

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String toJSONString() {
        return new Gson().toJson(this);
    }

    public static BasilLeg fromJSONString(String json) {
        return new Gson().fromJson(json, BasilLeg.class);
    }
}