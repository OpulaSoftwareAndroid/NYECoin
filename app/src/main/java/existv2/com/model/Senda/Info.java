package existv2.com.model.Senda;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("transaction_fees")
    @Expose
    private String transactionFees;
    @SerializedName("transaction_hash")
    @Expose
    private String transactionHash;

    public String getTransactionFees() {
        return transactionFees;
    }

    public void setTransactionFees(String transactionFees) {
        this.transactionFees = transactionFees;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

}