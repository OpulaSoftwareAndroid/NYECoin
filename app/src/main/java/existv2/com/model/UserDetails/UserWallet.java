
package existv2.com.model.UserDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserWallet {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("register_id")
    @Expose
    private String registerId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("btc_amount")
    @Expose
    private String btcAmount;
    @SerializedName("eth_amount")
    @Expose
    private String ethAmount;
    @SerializedName("fees_amount")
    @Expose
    private String feesAmount;
    @SerializedName("coin_address")
    @Expose
    private String coinAddress;
    @SerializedName("btc_address")
    @Expose
    private String btcAddress;
    @SerializedName("eth_address")
    @Expose
    private String ethAddress;
    @SerializedName("user_api_key")
    @Expose
    private String userApiKey;
    @SerializedName("coin_api_key")
    @Expose
    private String coinApiKey;
    @SerializedName("eth_api_key")
    @Expose
    private String ethApiKey;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("androidstatus")
    @Expose
    private String androidstatus;
    @SerializedName("role_id")
    @Expose
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBtcAmount() {
        return btcAmount;
    }

    public void setBtcAmount(String btcAmount) {
        this.btcAmount = btcAmount;
    }

    public String getEthAmount() {
        return ethAmount;
    }

    public void setEthAmount(String ethAmount) {
        this.ethAmount = ethAmount;
    }

    public String getFeesAmount() {
        return feesAmount;
    }

    public void setFeesAmount(String feesAmount) {
        this.feesAmount = feesAmount;
    }

    public String getCoinAddress() {
        return coinAddress;
    }

    public void setCoinAddress(String coinAddress) {
        this.coinAddress = coinAddress;
    }

    public String getBtcAddress() {
        return btcAddress;
    }

    public void setBtcAddress(String btcAddress) {
        this.btcAddress = btcAddress;
    }

    public String getEthAddress() {
        return ethAddress;
    }

    public void setEthAddress(String ethAddress) {
        this.ethAddress = ethAddress;
    }

    public String getUserApiKey() {
        return userApiKey;
    }

    public void setUserApiKey(String userApiKey) {
        this.userApiKey = userApiKey;
    }

    public String getCoinApiKey() {
        return coinApiKey;
    }

    public void setCoinApiKey(String coinApiKey) {
        this.coinApiKey = coinApiKey;
    }

    public String getEthApiKey() {
        return ethApiKey;
    }

    public void setEthApiKey(String ethApiKey) {
        this.ethApiKey = ethApiKey;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAndroidstatus() {
        return androidstatus;
    }

    public void setAndroidstatus(String androidstatus) {
        this.androidstatus = androidstatus;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
