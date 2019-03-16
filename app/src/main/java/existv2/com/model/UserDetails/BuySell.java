
package existv2.com.model.UserDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuySell {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("buy_price")
    @Expose
    private String buyPrice;
    @SerializedName("sell_price")
    @Expose
    private String sellPrice;
    @SerializedName("coin_price")
    @Expose
    private String coinPrice;
    @SerializedName("currency_name")
    @Expose
    private String currencyName;
    @SerializedName("add_date")
    @Expose
    private String addDate;
    @SerializedName("edit_date")
    @Expose
    private String editDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("btc_fix_price")
    @Expose
    private String btcFixPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(String coinPrice) {
        this.coinPrice = coinPrice;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getEditDate() {
        return editDate;
    }

    public void setEditDate(String editDate) {
        this.editDate = editDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBtcFixPrice() {
        return btcFixPrice;
    }

    public void setBtcFixPrice(String btcFixPrice) {
        this.btcFixPrice = btcFixPrice;
    }

}
