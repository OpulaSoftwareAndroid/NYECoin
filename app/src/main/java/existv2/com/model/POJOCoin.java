package existv2.com.model;

public class POJOCoin {
    private String strShortName, strFullName, strCurrentPrice,strCoinImageUrl,strCoinPriceChangePer;
//  "short_name": "NYE",
//          "fullname": "NYE",
//          "image": "nye.svg",
//          "current_price": "4.00",
//          "chnage_per": "-0.040",
//          "img": "https://nyecoin.io/TestginCoin/img/nye.svg"
    public POJOCoin() {
    }

    public POJOCoin(String strShortName, String strFullName, String strCurrentPrice,String strCoinImageUrl,String strCoinPriceChangePer) {
        this.strShortName = strShortName;
        this.strFullName = strFullName;
        this.strCurrentPrice = strCurrentPrice;
        this.strCoinImageUrl = strCoinImageUrl;
        this.strCoinPriceChangePer = strCoinPriceChangePer;

    }

    public String getCoinShortName() {
        return strShortName;
    }

    public void setCoinShortName(String strShortName) {
        this.strShortName = strShortName;
    }

    public String getCoinFullName() {
        return strFullName;
    }

    public void setCoingFullName(String strFullName) {
        this.strFullName = strFullName;
    }

    public String getCoinCurrentPrice() {
        return strCurrentPrice;
    }

    public void setCoinCurrentPrice(String strCurrentPrice) {
        this.strCurrentPrice = strCurrentPrice;
    }

    public String getCoinImageUrl() {
        return strCoinImageUrl;
    }

    public void setCoinImageUrl(String strCoinImageUrl) {
        this.strCoinImageUrl = strCoinImageUrl;
    }
    public String getCoinPriceChangePer() {
        return strCoinPriceChangePer;
    }

    public void setCoinPriceChangePer(String strCoinPriceChangePer) {
        this.strCoinPriceChangePer = strCoinPriceChangePer;
    }

}
