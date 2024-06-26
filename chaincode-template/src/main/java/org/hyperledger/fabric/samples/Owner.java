package org.example.chaincode;

import com.google.gson.Gson;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class Owner {
    @Property()
    private String organizationId;

    public Owner() {
        // Default constructor required for deserialization
    }

    public Owner(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String toJSONString() {
        return new Gson().toJson(this);
    }

    public static Owner fromJSONString(String json) {
        return new Gson().fromJson(json, Owner.class);
    }
}