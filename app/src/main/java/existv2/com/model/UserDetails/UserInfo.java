
package existv2.com.model.UserDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("transaction_password")
    @Expose
    private String transactionPassword;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("register_id")
    @Expose
    private String registerId;
    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("private_key")
    @Expose
    private String privateKey;
    @SerializedName("login_status")
    @Expose
    private String loginStatus;
    @SerializedName("secret_key")
    @Expose
    private String secretKey;
    @SerializedName("2fa_status")
    @Expose
    private String _2faStatus;
    @SerializedName("email_verification_status")
    @Expose
    private String emailVerificationStatus;
    @SerializedName("verify_date")
    @Expose
    private String verifyDate;
    @SerializedName("user_api_key")
    @Expose
    private String userApiKey;
    @SerializedName("register_date")
    @Expose
    private String registerDate;
    @SerializedName("active_date")
    @Expose
    private String activeDate;
    @SerializedName("deactive_date")
    @Expose
    private String deactiveDate;
    @SerializedName("delete_date")
    @Expose
    private String deleteDate;
    @SerializedName("delete_status")
    @Expose
    private String deleteStatus;
    @SerializedName("register_device")
    @Expose
    private String registerDevice;
    @SerializedName("register_location")
    @Expose
    private String registerLocation;
    @SerializedName("register_ip_address")
    @Expose
    private String registerIpAddress;
    @SerializedName("register_mac_address")
    @Expose
    private String registerMacAddress;
    @SerializedName("register_ipv4_address")
    @Expose
    private String registerIpv4Address;
    @SerializedName("last_updated_date")
    @Expose
    private String lastUpdatedDate;
    @SerializedName("resident_address")
    @Expose
    private String residentAddress;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("sponsor_id")
    @Expose
    private String sponsorId;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("androidstatus")
    @Expose
    private String androidstatus;
    @SerializedName("FCM")
    @Expose
    private String fCM;
    @SerializedName("resident_submit_date")
    @Expose
    private String residentSubmitDate;
    @SerializedName("resident_confirm_date")
    @Expose
    private String residentConfirmDate;
    @SerializedName("resident_cancelled_date")
    @Expose
    private String residentCancelledDate;
    @SerializedName("resident_status")
    @Expose
    private String residentStatus;
    @SerializedName("resident_cancelled_reason")
    @Expose
    private String residentCancelledReason;
    @SerializedName("resident_proof_image")
    @Expose
    private String residentProofImage;
    @SerializedName("document_type")
    @Expose
    private String documentType;
    @SerializedName("document_name")
    @Expose
    private String documentName;
    @SerializedName("document_number")
    @Expose
    private String documentNumber;
    @SerializedName("document_expire_date")
    @Expose
    private String documentExpireDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTransactionPassword() {
        return transactionPassword;
    }

    public void setTransactionPassword(String transactionPassword) {
        this.transactionPassword = transactionPassword;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String get2faStatus() {
        return _2faStatus;
    }

    public void set2faStatus(String _2faStatus) {
        this._2faStatus = _2faStatus;
    }

    public String getEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmailVerificationStatus(String emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public String getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(String verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getUserApiKey() {
        return userApiKey;
    }

    public void setUserApiKey(String userApiKey) {
        this.userApiKey = userApiKey;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getDeactiveDate() {
        return deactiveDate;
    }

    public void setDeactiveDate(String deactiveDate) {
        this.deactiveDate = deactiveDate;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getRegisterDevice() {
        return registerDevice;
    }

    public void setRegisterDevice(String registerDevice) {
        this.registerDevice = registerDevice;
    }

    public String getRegisterLocation() {
        return registerLocation;
    }

    public void setRegisterLocation(String registerLocation) {
        this.registerLocation = registerLocation;
    }

    public String getRegisterIpAddress() {
        return registerIpAddress;
    }

    public void setRegisterIpAddress(String registerIpAddress) {
        this.registerIpAddress = registerIpAddress;
    }

    public String getRegisterMacAddress() {
        return registerMacAddress;
    }

    public void setRegisterMacAddress(String registerMacAddress) {
        this.registerMacAddress = registerMacAddress;
    }

    public String getRegisterIpv4Address() {
        return registerIpv4Address;
    }

    public void setRegisterIpv4Address(String registerIpv4Address) {
        this.registerIpv4Address = registerIpv4Address;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getResidentAddress() {
        return residentAddress;
    }

    public void setResidentAddress(String residentAddress) {
        this.residentAddress = residentAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProfileImage() {
        return profile;
    }

    public void setProfileImage(String profile) {
     //   this.profileImage = profileImage;
        this.profile = profile;

    }

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getAndroidstatus() {
        return androidstatus;
    }

    public void setAndroidstatus(String androidstatus) {
        this.androidstatus = androidstatus;
    }

    public String getFCM() {
        return fCM;
    }

    public void setFCM(String fCM) {
        this.fCM = fCM;
    }

    public String getResidentSubmitDate() {
        return residentSubmitDate;
    }

    public void setResidentSubmitDate(String residentSubmitDate) {
        this.residentSubmitDate = residentSubmitDate;
    }

    public String getResidentConfirmDate() {
        return residentConfirmDate;
    }

    public void setResidentConfirmDate(String residentConfirmDate) {
        this.residentConfirmDate = residentConfirmDate;
    }

    public String getResidentCancelledDate() {
        return residentCancelledDate;
    }

    public void setResidentCancelledDate(String residentCancelledDate) {
        this.residentCancelledDate = residentCancelledDate;
    }

    public String getResidentStatus() {
        return residentStatus;
    }

    public void setResidentStatus(String residentStatus) {
        this.residentStatus = residentStatus;
    }

    public String getResidentCancelledReason() {
        return residentCancelledReason;
    }

    public void setResidentCancelledReason(String residentCancelledReason) {
        this.residentCancelledReason = residentCancelledReason;
    }

    public String getResidentProofImage() {
        return residentProofImage;
    }

    public void setResidentProofImage(String residentProofImage) {
        this.residentProofImage = residentProofImage;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentExpireDate() {
        return documentExpireDate;
    }

    public void setDocumentExpireDate(String documentExpireDate) {
        this.documentExpireDate = documentExpireDate;
    }

}
