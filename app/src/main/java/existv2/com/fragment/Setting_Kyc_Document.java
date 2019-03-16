package existv2.com.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import existv2.com.MainActivity;
import existv2.com.model.UserDetails.UserDetailsPojo;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import existv2.com.R;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.CommanResponse;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.kbeanie.imagechooser.api.ChooserType.REQUEST_CAPTURE_PICTURE;


public class Setting_Kyc_Document extends Fragment implements ImageChooserListener {

    com.rengwuxian.materialedittext.MaterialEditText edi_ti_username,editTextDocumentNumber,editTextFullName,edi_ti_message,editTextDocumentExpiryDate;
    RelativeLayout rel_image;
    Button btn_edit_ti_save;
    TextView ti_text;
    ArrayList <String>arrayListKycType=new ArrayList<>();
    SharedPreference sharedPreference;
    ImageView img_ti_image;
    Adapter adapterDocumentType;
    private String filePath = "";
    private MultipartBody.Part body;
    private int chooserType;
    private ImageChooserManager imageChooserManager;
    Spinner spinnerDocumentType;
    EditText editTextDocumentType;
    public Setting_Kyc_Document() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_kyc_document, container, false);

        sharedPreference = new SharedPreference();

        String usernamne = sharedPreference.getValue(getActivity(),WsConstant.username);

        edi_ti_username = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_ti_username);
        editTextFullName = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.editTextFullName);
        editTextDocumentNumber = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.editTextDocumentNumber);
        edi_ti_message = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_ti_message);
        rel_image = (RelativeLayout) rootview.findViewById(R.id.rel_image);
        btn_edit_ti_save = (Button) rootview.findViewById(R.id.btn_edit_ti_save);
        ti_text = (TextView) rootview.findViewById(R.id.ti_text);
        img_ti_image = (ImageView) rootview.findViewById(R.id.img_ti_image);
        spinnerDocumentType=rootview.findViewById(R.id.spinnerDocumentType);
        editTextDocumentType=rootview.findViewById(R.id.editTextDocumentType);
        editTextDocumentExpiryDate=rootview.findViewById(R.id.editTextDocumentExpiryDate);
        arrayListKycType.add("ID Proof");
        arrayListKycType.add("Passport Proof");


        String[] items = new String[arrayListKycType.size() + 1];
        items[0] = "Select Document Type";
        for (int i = 0; i < arrayListKycType.size(); i++) {
            items[i + 1] = arrayListKycType.get(i);
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.spinner_item_black, items) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerDocumentType.setAdapter(adapter);

        if(sharedPreference.getValue(getContext(),WsConstant.resident_status)!=null)
        {
            String strResidentStatusType=sharedPreference.getValue(getContext(),WsConstant.resident_status);
//            if(strResidentStatusType.equals("2") || strResidentStatusType.equals("0") )
                if(!(strResidentStatusType.equals("2") || strResidentStatusType.equals("0"))) {
                    editTextDocumentExpiryDate.setEnabled(false);
                    editTextFullName.setEnabled(false);
                    edi_ti_username.setEnabled(false);
                    editTextDocumentNumber.setEnabled(false);
                    rel_image.setClickable(false);
                    rel_image.setEnabled(false);
                    rel_image.setVisibility(View.GONE);
                    editTextDocumentNumber.setText(sharedPreference.getValue(getContext(), WsConstant.document_number));
                    editTextDocumentExpiryDate.setText(sharedPreference.getValue(getContext(), WsConstant.document_expiry_date));
                    editTextFullName.setText(sharedPreference.getValue(getContext(), WsConstant.document_name));
                    String strDocumentType = sharedPreference.getValue(getContext(), WsConstant.document_type);
                    Log.d(TAG, "jigar the shared code value we have is " + strDocumentType);
                    Log.d(TAG, "jigar the default spinner value we have is " + getIndex(spinnerDocumentType, strDocumentType));
                    spinnerDocumentType.setClickable(false);
                    spinnerDocumentType.setEnabled(false);
                    spinnerDocumentType.setVisibility(View.GONE);
                    spinnerDocumentType.setSelection(getIndex(spinnerDocumentType, strDocumentType));
                    editTextDocumentType.setVisibility(View.VISIBLE);
                    editTextDocumentType.setText(strDocumentType);
                    editTextDocumentType.setEnabled(false);
                    btn_edit_ti_save.setVisibility(View.GONE);
                }else
                {
                    spinnerDocumentType.setClickable(true);
                    spinnerDocumentType.setEnabled(true);
                    editTextDocumentType.setVisibility(View.GONE);
                }
        }


        TextWatcher textWatcher = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(current)) {
                    String clean = charSequence.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int j = 2; j <= cl && j < 6; j += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editTextDocumentExpiryDate.setText(current);
                    editTextDocumentExpiryDate.setSelection(sel < current.length() ? sel : current.length());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        editTextDocumentExpiryDate.addTextChangedListener(textWatcher);

        edi_ti_username.setText(usernamne);

        rel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForChosseImage();
            }
        });

        btn_edit_ti_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextFullName.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter full name as per document", Toast.LENGTH_SHORT).show();
                } else if (editTextDocumentExpiryDate.getText().toString().equalsIgnoreCase("DD/MM/YYYY")){
                    Toast.makeText(getActivity(), "Please enter document expiry date", Toast.LENGTH_SHORT).show();
                }else if (editTextDocumentExpiryDate.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter document expiry date", Toast.LENGTH_SHORT).show();
                }
                else if (editTextDocumentNumber.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter document number ", Toast.LENGTH_SHORT).show();
                }
                else if (spinnerDocumentType.getSelectedItemPosition()==0){
                    Toast.makeText(getActivity(), "Please select document type", Toast.LENGTH_SHORT).show();
                } else {
                    String strFullName = editTextFullName.getText().toString();
                    String strDocumentNumber = editTextDocumentNumber.getText().toString();
                    String strDocumentType = spinnerDocumentType.getSelectedItem().toString();
                    String strDocumentExpiryDate = editTextDocumentExpiryDate.getText().toString();
                    String regid = sharedPreference.getValue(getActivity(), WsConstant.register_id);
                    String token = sharedPreference.getValue(getActivity(),WsConstant.valid_data);

                    insertKycDocument(regid,strFullName,strDocumentNumber,strDocumentType,strDocumentExpiryDate,token);
                }
            }
        });

        return rootview;
    }
    private void getUserByIDRetro(final String regid,final String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regid);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "1");
            Log.d(TAG,"jigar the user register id we getting is "+regid);

            new RestClient(getActivity()).getInstance().get().getUserDetailByID(optioMap).
                    enqueue(new Callback<UserDetailsPojo>() {
                        @Override
                        public void onResponse(Call<UserDetailsPojo> call, Response<UserDetailsPojo> response) {
                            AppGlobal.hideProgressDialog(getActivity());
                            if (response.body() != null) {
                                if (response.body().getStatus() == 1) {

                                    Log.d(TAG,"jigar the user details by id we getting is "+new Gson().toJson(response));
                                    String amount = response.body().getInfo().getUserWallet().getAmount();
//                            String total_ticket = String.valueOf(response.body().getInfo().getTotalTicket());
//                            String pending_ticket = String.valueOf(response.body().getInfo().getPendingTicket());
//                                    txt_coin.setText(amount);
//                                    txt_card_coin.setText(amount);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserWallet().getAmount(),WsConstant.Fees);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserWallet().getCoinAddress(),WsConstant.coin_address);
//                            sharedPreference.save(getActivity(),response.body().getInfo().getUserWallet().getUserApiKey(),WsConstant.public_key);
//                            sharedPreference.save(getActivity(),response.body().getInfo().getPrivateKey(),WsConstant.private_key);
//                            sharedPreference.save(getActivity(),response.body().getInfo().getMerchantKey(),WsConstant.merchant_key);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getProfileImage(),WsConstant.Profile);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getName(),WsConstant.name);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getMobileNo(),WsConstant.mobile_no);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getResidentAddress(),WsConstant.resident_address);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getResidentStatus(),WsConstant.resident_status);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getDocumentNumber(),WsConstant.document_number);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getDocumentExpireDate(),WsConstant.document_expiry_date);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getDocumentType(),WsConstant.document_type);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getDocumentName(),WsConstant.document_name);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getCity(),WsConstant.city_id);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getState(),WsConstant.state_id);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getPincode(),WsConstant.pincode);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getCity(),WsConstant.city_name);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getState(),WsConstant.state_name);
                                    sharedPreference.save(getActivity(),response.body().getInfo().getFeeDetail().getPercentage(),WsConstant.Fees);

                                    MainActivity.headername(getActivity());


                                }
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDetailsPojo> call, Throwable t) {

                            try {
                                AppGlobal.hideProgressDialog(getActivity());
                                Toast.makeText(getActivity(),getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    });
        } else {
            Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public void insertKycDocument(final String registerID, String strFullname, String strDocumentNumber
            , String strDocumentType, String strDocumentExpiryDate, final String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            String fileName = "";
            if (!filePath.equalsIgnoreCase("")) {
                File file = new File(filePath);

                if (!file.exists()) {
                    Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody reqFile = RequestBody.create(MediaType.parse("KYCProof/*"), file);
                body = MultipartBody.Part.createFormData("Document", file.getName(), reqFile);
            }
            Log.d(TAG,"jigar the document register id is   "+registerID);

            Log.d(TAG,"jigar the upload path our storage we have is   "+filePath);
            Log.d(TAG,"jigar the upload path we body have is   "+body.body().toString());
            Log.d(TAG,"jigar the upload path header we have is   "+body.headers().toString());

            RequestBody requestBodyRegisterID = RequestBody.create(MediaType.parse("text/plain"), registerID);
            RequestBody requestBodyFullName = RequestBody.create(MediaType.parse("text/plain"), strFullname);
            RequestBody requestBodyDocumentNumber = RequestBody.create(MediaType.parse("text/plain"), strDocumentNumber);
            RequestBody requestBodyDocumentType = RequestBody.create(MediaType.parse("text/plain"), strDocumentType);
            RequestBody requestBodyDocumentExpiryDate = RequestBody.create(MediaType.parse("text/plain"), strDocumentExpiryDate);
            RequestBody atoken = RequestBody.create(MediaType.parse("text/plain"), token);
            RequestBody atype = RequestBody.create(MediaType.parse("text/plain"), "1");

            new RestClient(getActivity()).getInstance().get()
                    .insert_kyc_document(body,requestBodyDocumentNumber,requestBodyDocumentType,requestBodyDocumentExpiryDate
                            ,requestBodyRegisterID,requestBodyFullName,atoken,atype).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        Log.d(TAG,"jigar the document kyc response we have is  "+new Gson().toJson(response));

                        if (response.body().getStatus() == 1) {
                            editTextDocumentExpiryDate.setText("");
                            editTextDocumentNumber.setText("");
                            img_ti_image.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dott));
                            ti_text.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            getUserByIDRetro(registerID,token);

                            } else {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Data Null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(getActivity());
                        Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG,"jigar the error dailur in document kyc response we have is  "+t.getMessage());

                }
            });
        } else {
            Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDialogForChosseImage() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        View vi = getActivity().getLayoutInflater().inflate(R.layout.dialog_open_image, null, false);
        dialog.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);

        final TextView closeAppNo = dialog.findViewById(R.id.image_camera);
        closeAppNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                dialog.dismiss();
            }
        });
        final TextView closeAppYes = dialog.findViewById(R.id.image_gallary);
        closeAppYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("CheckResult")
    private void takePicture() {
        RxImagePicker.with(getActivity()).requestImage(Sources.CAMERA).subscribe(new io.reactivex.functions.Consumer<Uri>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Uri uri) throws Exception {
                File finalFile = new File(getRealPathFromURI(uri));
                filePath = finalFile.getAbsolutePath();
                Glide.with(getActivity()).load(filePath)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        }).into(img_ti_image);
                ti_text.setVisibility(View.GONE);
            }
        });
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                filePath = image.getFilePathOriginal();
                Glide.with(getActivity()).load(image.getFilePathOriginal())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        }).into(img_ti_image);
                ti_text.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onError(String s) {
        Log.e("Error", ":" + s);
    }

    @Override
    public void onImagesChosen(final ChosenImages images) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "On Images Chosen: " + images.size());
                onImageChosen(images.getImage(0));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "File Path : " + filePath + "\nChooser Type: " + chooserType);

        if (resultCode == RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CAPTURE_PICTURE) {
            try {
                Bitmap thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Objects.requireNonNull(thumbnail).compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;

                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                filePath = destination.getAbsolutePath();
                img_ti_image.setImageBitmap(thumbnail);
                ti_text.setVisibility(View.GONE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(Objects.requireNonNull(getActivity()), chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }
}