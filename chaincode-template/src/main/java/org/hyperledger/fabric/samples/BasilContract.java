package org.example.chaincode;

import com.google.gson.Gson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

@Contract(
        name = "BasilContract",
        info = @Info(
                title = "Basil Contract",
                description = "Contract for tracking basil plants",
                version = "0.0.1",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "s.avola@example.com",
                        name = "Stefano Avola",
                        url = "https://hyperledger.example.com")))
public class BasilContract implements ContractInterface {

    private final Gson gson = new Gson();

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Basil createNewPlant(final Context ctx, final String qrCode, final String orgId) {
        ChaincodeStub stub = ctx.getStub();

        if (basilExists(ctx, qrCode)) {
            String errorMessage = String.format("Plant with QR code %s already exists", qrCode);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Owner owner = new Owner(orgId);
        Basil basil = new Basil(qrCode, owner, "Newly Created");
        String basilJSON = gson.toJson(basil);
        stub.putStringState(qrCode, basilJSON);

        return basil;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Basil deletePlant(final Context ctx, final String qrCode, final String orgId) {
        ChaincodeStub stub = ctx.getStub();
        String basilJSON = stub.getStringState(qrCode);

        if (basilJSON == null || basilJSON.isEmpty()) {
            String errorMessage = String.format("Plant with QR code %s does not exist", qrCode);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Basil basil = gson.fromJson(basilJSON, Basil.class);

        if (!basil.getOwner().getOrganizationId().equals(orgId)) {
            String errorMessage = String.format("Organization %s is not the owner of plant %s", orgId, qrCode);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        stub.delState(qrCode);
        return basil;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Basil updatePlantState(final Context ctx, final String qrCode, final String newState, final String location,
                                   final String temperature, final String humidity, final String timestamp, final String orgId) {
        ChaincodeStub stub = ctx.getStub();
        String basilJSON = stub.getStringState(qrCode);

        if (basilJSON == null || basilJSON.isEmpty()) {
            String errorMessage = String.format("Plant with QR code %s does not exist", qrCode);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Basil basil = gson.fromJson(basilJSON, Basil.class);

        if (!basil.getOwner().getOrganizationId().equals(orgId)) {
            String errorMessage = String.format("Organization %s is not the owner of plant %s", orgId, qrCode);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        basil.setCurrentState(newState);
        BasilLeg newLeg = new BasilLeg(timestamp, location, temperature, humidity);
        basil.getHistory().add(newLeg);

        String updatedBasilJSON = gson.toJson(basil);
        stub.putStringState(qrCode, updatedBasilJSON);

        return basil;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Basil getPlantState(final Context ctx, final String qrCode) {
        ChaincodeStub stub = ctx.getStub();
        String basilJSON = stub.getStringState(qrCode);

        if (basilJSON == null || basilJSON.isEmpty()) {
            String errorMessage = String.format("Plant with QR code %s does not exist", qrCode);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        return gson.fromJson(basilJSON, Basil.class);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getPlantHistory(final Context ctx, final String qrCode) {
        ChaincodeStub stub = ctx.getStub();
        String basilJSON = stub.getStringState(qrCode);

        if (basilJSON == null || basilJSON.isEmpty()) {
            String errorMessage = String.format("Plant with QR code %s does not exist", qrCode);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Basil basil = gson.fromJson(basilJSON, Basil.class);
        return gson.toJson(basil.getHistory());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Basil transferPlantOwnership(final Context ctx, final String qrCode, final String newOrgId, final String currentOrgId) {
        ChaincodeStub stub = ctx.getStub();
        String basilJSON = stub.getStringState(qrCode);

        if (basilJSON == null || basilJSON.isEmpty()) {
            String errorMessage = String.format("Plant with QR code %s does not exist", qrCode);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Basil basil = gson.fromJson(basilJSON, Basil.class);

        if (!basil.getOwner().getOrganizationId().equals(currentOrgId)) {
            String errorMessage = String.format("Organization %s is not the owner of plant %s", currentOrgId, qrCode);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Owner newOwner = new Owner(newOrgId);
        basil.setOwner(newOwner);

        String updatedBasilJSON = gson.toJson(basil);
        stub.putStringState(qrCode, updatedBasilJSON);

        return basil;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean basilExists(final Context ctx, final String qrCode) {
        ChaincodeStub stub = ctx.getStub();
        String basilJSON = stub.getStringState(qrCode);
        return (basilJSON != null && !basilJSON.isEmpty());
    }
}