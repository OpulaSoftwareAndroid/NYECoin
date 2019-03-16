
package existv2.com.model.UserDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo;
    @SerializedName("user_wallet")
    @Expose
    private UserWallet userWallet;
    @SerializedName("buy_sell")
    @Expose
    private BuySell buySell;
    @SerializedName("fee_detail")
    @Expose
    private FeeDetail feeDetail;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserWallet getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(UserWallet userWallet) {
        this.userWallet = userWallet;
    }

    public BuySell getBuySell() {
        return buySell;
    }

    public void setBuySell(BuySell buySell) {
        this.buySell = buySell;
    }

    public FeeDetail getFeeDetail() {
        return feeDetail;
    }

    public void setFeeDetail(FeeDetail feeDetail) {
        this.feeDetail = feeDetail;
    }

}
