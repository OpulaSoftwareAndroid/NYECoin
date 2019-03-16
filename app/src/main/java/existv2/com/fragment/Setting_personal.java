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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import existv2.com.activity.SignUp;

import existv2.com.model.CountryLista.CountryList;
import existv2.com.model.CountryLista.Info;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import existv2.com.MainActivity;
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


public class Setting_personal extends Fragment implements ImageChooserListener {

    public  static de.hdodenhof.circleimageview.CircleImageView image_PersonalInfo_DP;
    ImageView edit_edit_profile;
    com.rengwuxian.materialedittext.MaterialEditText edit_registerid,edit_fullname,edit_username,edit_email,edit_code,edit_mobile;
    Button btn_edit_save;
    SharedPreference sharedPreference;
    private String filePath = "";
    String strSelectedOldCountryCode;
    private MultipartBody.Part body;
    Spinner spinnerCountryCode;
    private int chooserType;
    private ImageChooserManager imageChooserManager;
    private ArrayList<Info> contactList;
    CountryCodePicker pickerCountryCode;
    String code;
    public Setting_personal() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*Resources res = getResources(); // need this to fetch the drawable
        Drawable draw = res.getDrawable( R.drawable.man );
        image_PersonalInfo_DP.setImageDrawable(draw);*/
        updateimage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_setting_personal, container, false);

        sharedPreference = new SharedPreference();
        image_PersonalInfo_DP = (de.hdodenhof.circleimageview.CircleImageView) rootview.findViewById(R.id.image_PersonalInfo_DP);
        edit_edit_profile = (ImageView) rootview.findViewById(R.id.edit_edit_profile);
        edit_registerid = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edit_registerid);
        edit_fullname = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edit_fullname);
        edit_username = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edit_username);
        edit_email = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edit_email);
        edit_code = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edit_code);
        edit_mobile = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edit_mobile);
        btn_edit_save = (Button) rootview.findViewById(R.id.btn_edit_save);
        pickerCountryCode = (CountryCodePicker) rootview.findViewById(R.id.pickerCountryCode);

        spinnerCountryCode =  rootview.findViewById(R.id.spinnerCountryCode);
        //getCountry();
        String registerid = sharedPreference.getValue(getActivity(), WsConstant.register_id);
        String fullname = sharedPreference.getValue(getActivity(), WsConstant.name);
        String username = sharedPreference.getValue(getActivity(), WsConstant.username);
        String email = sharedPreference.getValue(getActivity(), WsConstant.email_id);
        code = sharedPreference.getValue(getActivity(), WsConstant.country_code);
        String mobile = sharedPreference.getValue(getActivity(), WsConstant.mobile_no);
        strSelectedOldCountryCode=code;
//        String strCode=code.substring(1);

        //private method of your class

        edit_registerid.setText(registerid);
        edit_fullname.setText(fullname);
        edit_username.setText(username);
        edit_email.setText(email);
        edit_code.setText(code);
        edit_mobile.setText(mobile);




//        Log.d(TAG,"jigar the shared code value we have is "+code);
//        Log.d(TAG,"jigar the default spinner value we have is "+getIndex(spinnerCountryCode,code));
        pickerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                String strSelectedCode=pickerCountryCode.getSelectedCountryCode();
                String strSelectedName=pickerCountryCode.getSelectedCountryName();
//                String strSelectdCode=pickerCountryCode.getSelectedCountryEnglishName();

                Log.d(TAG,"jigar the selected code is  "+strSelectedCode+" and country name is "+strSelectedName);
                code=strSelectedCode;
  //              country=strSelectedName.toLowerCase();


//                spinner_country.setSelection(getIndex(spinnerCountryCode, code));
                Log.d(TAG,"jigar the shared code value we have is "+code);
                pickerCountryCode.setDefaultCountryUsingPhoneCode(Integer.parseInt(code));
           //     mCountryCodePicker.setDefault("IN");

                Log.d(TAG,"jigar the default spinner value we have is "+getIndex(spinnerCountryCode,code));
            }
        });

        edit_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForChosseImage();
            }
        });

        btn_edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_fullname.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter Fullname", Toast.LENGTH_LONG).show();
                } else if (edit_code.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter Country code", Toast.LENGTH_LONG).show();
                } else if (edit_mobile.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter Mobile number", Toast.LENGTH_LONG).show();
                } else if (spinnerCountryCode.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please select Mobile Code", Toast.LENGTH_LONG).show();
                } else {
                    String registerid = edit_registerid.getText().toString();
                    String name = edit_fullname.getText().toString();
                    String code =edit_code.getText().toString();
                    String mobile = edit_mobile.getText().toString();
                    String token = sharedPreference.getValue(getActivity(),WsConstant.valid_data);
                    MainActivity.updateData(getActivity());
                    edit_personal(registerid,name,code,mobile,token);
                }
            }
        });

        return rootview;
    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
    private void updateimage() {
        String profil = sharedPreference.getValue(getActivity(), WsConstant.Profile);
//        if (profil.equalsIgnoreCase("https://opulasoft.com/ExistV2//master/Users/profile/"))
//        {
//            Glide.with(Setting_personal.this).load(profil)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            return false;
//                        }
//                    }).placeholder(R.drawable.image_boy)
//                    .into(image_PersonalInfo_DP);
//        }
//        else
//
            {
                Picasso.with(getContext())
                        .load(profil)
                        .placeholder(R.drawable.image_boy)
                        .into(image_PersonalInfo_DP);
//            Glide.with(Setting_personal.this).load(profil)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            return false;
//                        }
//                    })
//                    .placeholder(R.drawable.image_boy)
//                    .into(image_PersonalInfo_DP);
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

                Picasso.with(getContext())
                        .load(filePath)
                        .placeholder(R.drawable.image_boy)
                        .into(image_PersonalInfo_DP);
//                Glide.with(getActivity()).load(filePath)
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                return false;
//                            }
//                        })
//                        .placeholder(R.drawable.image_boy)
//
//                        .into(image_PersonalInfo_DP);
            }
        });
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
//    private void getCountry() {
//        if (AppGlobal.isNetwork(Objects.requireNonNull(getContext()))) {
//            AppGlobal.showProgressDialog(getContext());
//            Map<String, String> optioMap = new HashMap<>();
//            new RestClient(getContext()).getInstance().get().getCountry(optioMap).enqueue(new Callback<CountryList>() {
//                @Override
//                public void onResponse(Call<CountryList> call, Response<CountryList> response) {
//                    AppGlobal.hideProgressDialog(getContext());
//                    if (response.body() != null) {
//                        Log.d(TAG,"jigar the country code response we getting is "+new Gson().toJson(response));
//
//                        if (response.body().getStatus() == 1) {
//                            contactList = (ArrayList<Info>) response.body().getInfo();
//
//                            String[] items = new String[contactList.size() + 1];
//                            // String[] imageItems = new String[contactList.size() + 1];
//                            String[] itemsCountryName = new String[contactList.size() + 1];
//
//                            ArrayList <String>arrayListCountryName=new ArrayList<>();
//                            ArrayList <String>  arrayListFlagImage=new ArrayList<>();
//                            items[0] = " Code ";
//                            arrayListFlagImage.add(0,"");
//                            ArrayList <String>imageItems = new ArrayList<>(contactList.size()+1);
//                            itemsCountryName[0] = "Select Country";
//
//                            for (int i = 0; i < contactList.size(); i++) {
//                                items[i + 1] = contactList.get(i).getCountryCode();
////                                imageItems[i + 1] = contactList.get(i).getCountryFlag();
//                                imageItems.add("");
//                                arrayListFlagImage.add(contactList.get(i).getCountryFlag());
//                                itemsCountryName[i+1]=contactList.get(i).getCountryName();
//                                arrayListCountryName.add(contactList.get(i).getCountryName());
//                            }
//                            Log.d(TAG,"jigar the array before of country flag have is "+arrayListFlagImage.get(2));
//
//
//
//
//                            SpinnerAdapter adapter = new SpinnerAdapter(getContext()
//                                    , R.layout.spinner_item_black,items, arrayListFlagImage);
////                            spinner_country.setAdapter(adapter);
//                            spinnerCountryCode.setAdapter(adapter);
//                            spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                                    if(i>0) {
////                                        code = contactList.get(i-1).getCountryCode();
////                                        Log.d(TAG, "jigar the selected code is " + code);
//                                    }
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                }
//                            });
//
////                            contactList = (ArrayList<Info>) response.body().getInfo();
////                            String[] items = new String[contactList.size() + 1];
////                            items[0] = "Choose Code";
////                            for (int i = 0; i < contactList.size(); i++) {
////                                items[i + 1] = contactList.get(i).getCountryCode();
////
////                            }
////
////                            ArrayAdapter<String> adapter;
////                            adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.spinner_item_black, items) {
////                                @Override
////                                public boolean isEnabled(int position) {
////                                    if (position == 0) {
////                                        return false;
////                                    } else {
////                                        return true;
////                                    }
////                                }
////
////                                @Override
////                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
////                                    View view = super.getDropDownView(position, convertView, parent);
////                                    TextView tv = (TextView) view;
////                                    if (position == 0) {
////                                        tv.setTextColor(Color.GRAY);
////                                    } else {
////                                        tv.setTextColor(Color.BLACK);
////                                    }
////                                    return view;
////                                }
////                            };
//                            spinnerCountryCode.setAdapter(adapter);
//                            if(strSelectedOldCountryCode.contains("#") || strSelectedOldCountryCode.contains("+") ) {
//                                strSelectedOldCountryCode = strSelectedOldCountryCode.substring(1);
//                            }
////                            pickerCountryCode.setDefaultCountryUsingNameCode(strSelectedOldCountryCode);
//                            pickerCountryCode.setDefaultCountryUsingPhoneCode(Integer.parseInt(strSelectedOldCountryCode));
//
////                            spinnerCountryCode.pref
////                            spinnerCountryCode.setSelection(getIndex(spinnerCountryCode, strSelectedOldCountryCode));
//                            Log.d(TAG,"jigar the shared code value we have is "+strSelectedOldCountryCode);
//                            Log.d(TAG,"jigar the default spinner value we have is "+getIndex(spinnerCountryCode,strSelectedOldCountryCode));
////                            spinnerCountryCode.setSelection(getIndex(spinnerCountryCode,strSelectedOldCountryCode));
//
//                        } else {
//                            Toast.makeText(getContext(), "Unable to load", Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(getContext(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CountryList> call, Throwable t) {
//                    try {
//                        AppGlobal.hideProgressDialog(getContext());
//                        Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
//        }
//
//    }

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

    public void edit_personal(String regId, final String name, final String code, final String mobile, String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            String fileName = "";
            if(filePath!=null) {
                if (!filePath.equalsIgnoreCase("")) {
                    File file = new File(filePath);

                    if (!file.exists()) {
                        Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RequestBody reqFile = RequestBody.create(MediaType.parse("ProfilePicture/*"), file);
                    body = MultipartBody.Part.createFormData("ProfilePicture", file.getName(), reqFile);
                }
            }
            RequestBody aregId = RequestBody.create(MediaType.parse("text/plain"), regId);
            RequestBody aname = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody atoken = RequestBody.create(MediaType.parse("text/plain"), token);
            RequestBody amobile = RequestBody.create(MediaType.parse("text/plain"), mobile);
            RequestBody acountrycode = RequestBody.create(MediaType.parse("text/plain"), code);
            RequestBody atype = RequestBody.create(MediaType.parse("text/plain"), "1");

            new RestClient(getActivity()).getInstance().get()
                    .edit_personal(body,aname,aregId,amobile,atoken,atype,acountrycode).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            sharedPreference.save(getActivity(), name,WsConstant.name);
                            sharedPreference.save(getActivity(), mobile,WsConstant.mobile_no);
                            sharedPreference.save(getActivity(), filePath,WsConstant.Profile);
                            sharedPreference.save(getActivity(), code,WsConstant.country_code);

                            MainActivity.updateData(getActivity());
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
                }
            });
        } else {
            Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                filePath = image.getFilePathOriginal();
                Picasso.with(getContext())
                        .load(new File(filePath))
                        .placeholder(R.drawable.image_boy)
                        .fit()
                        .into(image_PersonalInfo_DP);
//                Glide.with(getActivity()).load(image.getFilePathOriginal())
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                return false;
//                            }
//                        })
//                        .placeholder(R.drawable.image_boy)
//                        .into(image_PersonalInfo_DP);
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
                image_PersonalInfo_DP.setImageBitmap(thumbnail);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "jigar the error  is file not found : " + filePath + "\nChooser Type: " + chooserType);

                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, "jigar the error  is file not found : " + filePath + "\nChooser Type: " + chooserType);

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