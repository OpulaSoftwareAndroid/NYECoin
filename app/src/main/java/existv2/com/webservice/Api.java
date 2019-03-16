package existv2.com.webservice;


import java.util.Map;

import existv2.com.model.UserDetails.UserDetailsPojo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import existv2.com.model.AllTicketInfoa.AllTicketInfo;
import existv2.com.model.CityLista.CityList;
import existv2.com.model.CommanResponse;
import existv2.com.model.CountryLista.CountryList;
import existv2.com.model.Logina.Login;
import existv2.com.model.Registera.Register;
import existv2.com.model.Senda.Send;
import existv2.com.model.StateLista.StateList;
import existv2.com.model.TicketInfoa.TicketInfo;
import existv2.com.model.TransactionInfoa.TransactionInfo;
import existv2.com.model.Wallatea.Wallate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

@SuppressWarnings("All")
public interface Api {

    @POST("CountryList.php")
    Call<CountryList> getCountry(@Body Map<String, String> params);

    @POST("UserRegister.php")
    Call<Register> signup(@Body Map<String, String> params);

    @POST("UserLogin.php")
    Call<Login> signin(@Body Map<String, String> params);

    @POST("ResendMail.php")
    Call<CommanResponse> resendmail(@Body Map<String, String> params);

    @POST("UserLogout.php")
    Call<CommanResponse> logout(@Body Map<String, String> params);

    @POST("ForgotPassword.php")
    Call<CommanResponse> forgotpass(@Body Map<String, String> params);

    @POST("WalletAmount.php")
    Call<Wallate> wallate(@Body Map<String, String> params);

    @POST("UserDetailByID.php")
    Call<UserDetailsPojo> getUserDetailByID(@Body Map<String, String> params);

    @Multipart
    @POST("UpdateProfile.php")
    Call<CommanResponse> edit_personal(@Part MultipartBody.Part image,
                                      @Part("FullName") RequestBody department,
                                      @Part("RegisterId") RequestBody subject,
                                      @Part("MobileNo") RequestBody message,
                                      @Part("ValidData") RequestBody project_id,
                                      @Part("Type") RequestBody register_id,
                                      @Part("CountryCode") RequestBody country_code);



    @POST("UpdateContact.php")
    Call<CommanResponse> edit_contact(@Body Map<String, String> params);

    @POST("ChangePassword.php")
    Call<CommanResponse> edit_password(@Body Map<String, String> params);

    @POST("TransactionPassword.php")
    Call<CommanResponse> edit_transaction_password(@Body Map<String, String> params);

    @POST("SendOtherTransaction.php")
    Call<Send> sendNYECoin(@Body Map<String, String> params);

    @POST("ReceiveOtherTransaction.php")
    Call<CommanResponse> receivebitalgo(@Body Map<String, String> params);

    @POST("ReceiveOtherTransactionCoin.php")
    Call<CommanResponse> receiveOtherTransactionCoin(@Body Map<String, String> params);

    @POST("ReceiveOtherTransactionMultiple.php")
    Call<CommanResponse> receiveOtherTransactionMultiple(@Body Map<String, String> params);

    @Multipart
    @POST("CreateTicket.php")
    Call<CommanResponse> create_ticket(@Part MultipartBody.Part image,
                                       @Part("Subject") RequestBody subject,
                                       @Part("RegisterId") RequestBody regid,
                                       @Part("Message") RequestBody message,
                                       @Part("ValidData") RequestBody validdata,
                                       @Part("Type") RequestBody type);

    @Multipart
    @POST("KYCUpdate.php")
    Call<CommanResponse> insert_kyc_document(@Part MultipartBody.Part image,
                                             @Part("RegisterId") RequestBody regid,
                                             @Part("FullName") RequestBody fullname,
                                             @Part("DocumentType") RequestBody documenttype,
                                             @Part("DocumentNumber") RequestBody documentnumber,
                                             @Part("DocumentExpiredDate") RequestBody documentexpireddate,
                                             @Part("ValidData") RequestBody validdata,
                                             @Part("Type") RequestBody type);

    @POST("TransactionHistory.php")
    Call<TransactionInfo> transactionhistory(@Body Map<String, String> params);

    @POST("TicketHistory.php")
    Call<TicketInfo> tickethistory(@Body Map<String, String> params);

    @Multipart
    @POST("ReplyTicket.php")
    Call<CommanResponse> reply_ticket(@Part MultipartBody.Part image,
                                       @Part("RegisterId") RequestBody regid,
                                       @Part("Message") RequestBody message,
                                       @Part("ValidData") RequestBody validdata,
                                       @Part("TicketNumber") RequestBody number,
                                       @Part("Type") RequestBody type);

    @POST("AllTicketHistory.php")
    Call<AllTicketInfo> alltickethistory(@Body Map<String, String> params);

    @POST("CityList.php")
    Call<CityList> getCity(@Body Map<String, String> params);

    @POST("StateList.php")
    Call<StateList> getState(@Body Map<String, String> params);

    @GET("AndroidVersion.php")
    Call<CommanResponse> getVersion();
}
