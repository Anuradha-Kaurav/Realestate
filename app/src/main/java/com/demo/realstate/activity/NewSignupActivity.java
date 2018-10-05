package com.demo.realstate.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demo.realstate.R;
import com.demo.realstate.extra.CommonMethod;
import com.demo.realstate.extra.Const;
import com.demo.realstate.extra.FileUtils;
import com.demo.realstate.extra.RequestQueueSingleton;
import com.demo.realstate.extra.URLConstants;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewSignupActivity extends AppCompatActivity {

    TextView agent, builder;
    EditText name, phoneNum, email, password;
    Button submit;
    ImageView showHidePassword, pan_img_add, pan_img, adhaar_img_front_add, adhaar_img_front, adhaar_img_back_add, adhaar_img_back;
    ImageView pan_remove, adhaar_front_remove, adhaar_back_remove;
    RelativeLayout panLayout;
    LinearLayout adhaarLayout, adhaar_add_front_layout, adhaar_add_back_layout, pan_add_layout;
    String userName, userEmail, userPassword, userPhoneNum, userRole, doctype;
    boolean isPasswordVisible;
    KProgressHUD hud;
    AlertDialog alert;
    RequestQueue requestQueue;
    private static String root = null;
    private static String imageFolderPath = null;
    private String imageName = null;
    private static Uri fileUri = null;
    String imagePath="", addDocType, selectedID;
    Bitmap bm;
    MaterialSpinner spinner;
    String[] id_cat = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_signup);
        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        hud = KProgressHUD.create(NewSignupActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        initUi();
    }

    private void initUi(){

        builder = findViewById(R.id.builder);
        agent = findViewById(R.id.agent);
        name = findViewById(R.id.name);
        phoneNum = findViewById(R.id.ph);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        pan_add_layout = findViewById(R.id.pan_add_layout);
        panLayout = findViewById(R.id.pan_layout);

        adhaar_add_front_layout = findViewById(R.id.adhaar_add_front_layout);
        adhaar_add_back_layout = findViewById(R.id.adhaar_add_back_layout);
        adhaarLayout = findViewById(R.id.adhaar_layout);
        submit = findViewById(R.id.submit);
        showHidePassword  = findViewById(R.id.visibility);

        pan_img_add = findViewById(R.id.pan_img_add);
        pan_img = findViewById(R.id.pan_img);
        pan_remove = findViewById(R.id.pan_remove);

        adhaar_img_front_add = findViewById(R.id.adhaar_img_front_add);
        adhaar_img_front = findViewById(R.id.adhaar_img_front);
        adhaar_front_remove = findViewById(R.id.adhaar_front_remove);

        adhaar_img_back_add = findViewById(R.id.adhaar_img_back_add);
        adhaar_img_back = findViewById(R.id.adhaar_img_back);
        adhaar_back_remove = findViewById(R.id.adhaar_back_remove);

        if(!isPasswordVisible){
            showHidePassword.setImageDrawable(getDrawable(R.drawable.ic_visibility));
        }else{
            showHidePassword.setImageDrawable(getDrawable(R.drawable.ic_visibility_off));
        }

        showHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHidePassword(isPasswordVisible);
            }
        });

        getDocTypes();

        spinner = findViewById(R.id.id_spinner);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                selectedID = item;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        askPermission_gallery();

                        if( Const.PERMISSION_GALLERY_GRANTED){
                            manageIdLayouts(item);
                        }
                    } else{
                        manageIdLayouts(item);
                    }
                }else{
                    manageIdLayouts(item);
                }
            }
        });

        agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agent.setBackgroundResource(R.color.yelp);
                builder.setBackgroundResource(0);
                userRole = "Agent";
                agent.setTextColor(getResources().getColor(R.color.white));
                builder.setTextColor(getResources().getColor(R.color.black));
            }
        });

        builder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setBackgroundResource(R.color.yelp);
                agent.setBackgroundResource(0);
                userRole = "Builder";
                builder.setTextColor(getResources().getColor(R.color.white));
                agent.setTextColor(getResources().getColor(R.color.black));
            }
        });

        pan_img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDocType = "Pan";
                showDialog();
            }
        });

        pan_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pan_add_layout.setVisibility(View.VISIBLE);
                //pan_img_add.setVisibility(View.GONE);
                pan_img.setVisibility(View.GONE);
                pan_img.setImageBitmap(null);
                pan_remove.setVisibility(View.GONE);
            }
        });

        adhaar_front_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adhaar_add_front_layout.setVisibility(View.VISIBLE);
                // adhaar_img_front_add.setVisibility(View.GONE);
                adhaar_img_front.setVisibility(View.GONE);
                adhaar_img_front.setImageBitmap(null);
                adhaar_front_remove.setVisibility(View.GONE);
            }
        });

        adhaar_back_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adhaar_add_back_layout.setVisibility(View.VISIBLE);
                // adhaar_img_back_add.setVisibility(View.GONE);
                adhaar_img_back.setVisibility(View.GONE);
                adhaar_img_back.setImageBitmap(null);
                adhaar_back_remove.setVisibility(View.GONE);
            }
        });

        adhaar_img_front_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDocType = "Adhaar_front";
                showDialog();
            }
        });

        adhaar_img_back_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDocType = "Adhaar_back";
                showDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonMethod.isNetworkAvailable(NewSignupActivity.this)) {
                    if(credentialValidation()){
                        registerService(userName, userEmail, userPassword, userPhoneNum, userRole, doctype);
                    }
                }else{
                    CommonMethod.snackBarWithAction(NewSignupActivity.this, getString(R.string.no_internet));
                }
            }
        });
    }

    private void manageIdLayouts(String item){
        if(item.equalsIgnoreCase("pan card")){
            panLayout.setVisibility(View.VISIBLE);
            adhaarLayout.setVisibility(View.GONE);
           // doctype = "Pan";
            doctype = item;
        }else {//if (item.equalsIgnoreCase("Adhaar card") || item.equalsIgnoreCase("Aadhaar card")){
            panLayout.setVisibility(View.GONE);
            adhaarLayout.setVisibility(View.VISIBLE);
           // doctype = "Adhaar";
            doctype = item;
        }
        //else{
//            panLayout.setVisibility(View.GONE);
//            adhaarLayout.setVisibility(View.GONE);
//            doctype = null;
//        }
    }

    private void showHidePassword(boolean passwordVisibility){
          if(!passwordVisibility){
            // show password
              password.setTransformationMethod(PasswordTransformationMethod.getInstance());
              showHidePassword.setImageDrawable(getDrawable(R.drawable.ic_visibility));
              isPasswordVisible = true;
          }else{
            // hide password
              password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
              showHidePassword.setImageDrawable(getDrawable(R.drawable.ic_visibility_off));
              isPasswordVisible = false;
          }
    }

    private boolean credentialValidation() {
        boolean result =  false;
        try {
            //Hiding keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        userName = name.getText().toString().trim();
        userEmail = email.getText().toString().trim();
        userPassword = password.getText().toString().trim();
        userPhoneNum = phoneNum.getText().toString().trim();

        if (!TextUtils.isEmpty(userRole) && !TextUtils.isEmpty(userName) && CommonMethod.isValidEmail(userEmail)
                && !TextUtils.isEmpty(userPassword) && userPhoneNum.length()==10 && !TextUtils.isEmpty(doctype)) {
            result = true;
        } else {
            if(userRole == null){
                CommonMethod.snackBarWithAction(this, getString(R.string.select_role));
            } else if(userName.length() <= 0) {
                name.setError("Please Enter Your UserName");
            } else if(!CommonMethod.isValidEmail(userEmail)){
                email.setError("Please enter valid Email id");
            } else if(userPassword.length() <= 0) {
                password.setError("Please Enter Your Password");
            }else if(userPhoneNum.length() != 10){
                phoneNum.setError("Please enter 10 digit phone number");
            }else if(doctype == null){
                CommonMethod.snackBarWithAction(this, getString(R.string.select_doc_type));
            }
        }
        return result;
    }

    private void registerService(String userName, String userEmail, String userPassword, String userPhoneNum, String userRole, String docType){
        hud.show();
        JSONObject json = new JSONObject();
        try {
            json.put("emailId", userEmail);
            json.put("name", userName);
            json.put("mobileno", userPhoneNum);
            json.put("password", userPassword);
            json.put("acctype", userRole);
            json.put("doctype", docType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = URLConstants.REGISTER;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hud.dismiss();
                        try {
                           // String status = (String) response.get("status");
                            if (response.getString("status").equalsIgnoreCase("success")) {

                                //Utils.snackBarWithAction(Register.this, "Please check inbox for email verification" );
                                Intent intent = new Intent(NewSignupActivity.this, SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("msg","Please check inbox for email verification" );
                                startActivity(intent);
                                finish();

                            } else {
                                if(response.getString("message").equalsIgnoreCase("Email id is already registered.")){
                                    CommonMethod.simpleSnackBar(NewSignupActivity.this, "Email id is already registered");
                                }else if(response.getString("message").equalsIgnoreCase("User Name not available.")){
                                    CommonMethod.simpleSnackBar(NewSignupActivity.this, "This user name is not available");
                                }else{
                                    CommonMethod.simpleSnackBar(NewSignupActivity.this, response.getString("message"));
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hud.dismiss();
                Log.e("Volley Error", error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getDocTypes() {
        hud.show();
        JSONObject json = new JSONObject();
        try {
            json.put("setId", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json: ", json.toString());
        String url = URLConstants.SETTINGS;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hud.dismiss();
                        try {
                            id_cat = (response.getJSONArray("setval").get(0).toString()).split(",");
                            spinner.setItems(id_cat);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hud.dismiss();
                Log.e("Volley Error", error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    void showDialog() {
        // TODO Auto-generated method stub

        AlertDialog.Builder builder = new AlertDialog.Builder(NewSignupActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();

        View convertView = (View) inflater.inflate(R.layout.dialog_upload_id, null);

        TextView from_gallery = (TextView) convertView.findViewById(R.id.from_gallery);
        TextView from_camera = (TextView) convertView.findViewById(R.id.from_camera);

        from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        askPermission_gallery();
                    } else {


                        try {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                            intent.setType("image/*");

                            startActivityForResult(
                                    Intent.createChooser(intent, "Choose Picture"),
                                    Const.PERMISSION_GALLERY);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        alert.dismiss();
                    }

                }
                else
                {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                        intent.setType("image/*");

                        startActivityForResult(
                                Intent.createChooser(intent, "Choose Picture"),
                                Const.PERMISSION_GALLERY);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    alert.dismiss();
                }


            }
        });

        from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        askPermission_gallery();
                        askPermission_camera();
                    } else {
                        // fetching the root directory
                        root = Environment.getExternalStorageDirectory().toString()
                                + "/RealState/ProfilePictures";

                        // Creating folders for Image
                        //imageFolderPath = root + "/camera_images";
                        imageFolderPath = root;
                        File imagesFolder = new File(imageFolderPath);
                        if (!imagesFolder.exists()) {
                            imagesFolder.mkdirs();
                        }

                        // Generating file name
                        imageName = "user.jpg";

                        // Creating image here

                        File image = new File(imageFolderPath, imageName);

                        fileUri = Uri.fromFile(image);

                        //imageView.setTag(imageFolderPath + File.separator + imageName);

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        startActivityForResult(cameraIntent,  Const.PERMISSION_CAMERA);

                        alert.dismiss();
                    }

                }
                else
                {
                   /* Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, Const.PERMISSION_CAMERA);*/

                    // fetching the root directory
                    root = Environment.getExternalStorageDirectory().toString()
                            + "/RealState/ProfilePictures";

                    // Creating folders for Image
                    //imageFolderPath = root + "/camera_images";
                    imageFolderPath = root;
                    File imagesFolder = new File(imageFolderPath);
                    imagesFolder.mkdirs();

                    // Generating file name
                    imageName = "user.jpg";

                    // Creating image here

                    File image = new File(imageFolderPath, imageName);

                    fileUri = Uri.fromFile(image);

                    //imageView.setTag(imageFolderPath + File.separator + imageName);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    startActivityForResult(cameraIntent,  Const.PERMISSION_CAMERA);

                    alert.dismiss();
                }

            }
        });



        builder.setView(convertView);
        alert = builder.create();
        alert.show();

    }

    void askPermission_gallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if ((ActivityCompat.shouldShowRequestPermissionRationale(NewSignupActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    ActivityCompat.requestPermissions(NewSignupActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            Const.PERMISSION_GALLERY);

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(NewSignupActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            Const.PERMISSION_GALLERY);


                }
            }
        }


    }

    void askPermission_camera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if ((ActivityCompat.shouldShowRequestPermissionRationale(NewSignupActivity.this,
                        android.Manifest.permission.CAMERA))) {

                    ActivityCompat.requestPermissions(NewSignupActivity.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            Const.PERMISSION_CAMERA);


                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(NewSignupActivity.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            Const.PERMISSION_CAMERA);


                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Const.PERMISSION_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Const.PERMISSION_GALLERY_GRANTED = true;
                    askPermission_camera();

                    manageIdLayouts(selectedID);
                } else {
                    //code for deny
                    Const.PERMISSION_GALLERY_GRANTED = false;
                }
                break;
            case Const.PERMISSION_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Const.PERMISSION_CAMERA_GRANTED = true;
                    manageIdLayouts(selectedID);
                }else{
                    Const.PERMISSION_CAMERA_GRANTED = false ;
                }
                break;

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {

            case Const.PERMISSION_GALLERY:
                if(resultCode == RESULT_OK){

                    Uri selectedImage = imageReturnedIntent.getData();

                    try {
                        imagePath = FileUtils.getPath(getApplicationContext(),selectedImage);
                        System.out.println("IMAGEPATH gallery = "+imagePath);
                        bm = uploadphoto(FileUtils.getPath(getApplicationContext(), selectedImage));

                        switch(addDocType){
                            case "Pan":
                                pan_add_layout.setVisibility(View.GONE);
                                //pan_img_add.setVisibility(View.GONE);
                                pan_img.setVisibility(View.VISIBLE);
                                pan_img.setImageBitmap(bm);
                                pan_remove.setVisibility(View.VISIBLE);
                                break;
                            case "Adhaar_front":
                                adhaar_add_front_layout.setVisibility(View.GONE);
                               // adhaar_img_front_add.setVisibility(View.GONE);
                                adhaar_img_front.setVisibility(View.VISIBLE);
                                adhaar_img_front.setImageBitmap(bm);
                                adhaar_front_remove.setVisibility(View.VISIBLE);
                                break;
                            case "Adhaar_back":
                                adhaar_add_back_layout.setVisibility(View.GONE);
                                //adhaar_img_back_add.setVisibility(View.GONE);
                                adhaar_img_back.setVisibility(View.VISIBLE);
                                adhaar_img_back.setImageBitmap(bm);
                                adhaar_back_remove.setVisibility(View.VISIBLE);
                                break;
                            default:
                                 break;
                        }

					/*	mSpinner.setMessage("Uploading...");
						mSpinner.show();

						new UploadFile().execute(imagePath);*/

                        //bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                        //MainSingleton.bitmapMapSet.put("bitmap1", bm);

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                break;

            case Const.PERMISSION_CAMERA:
                if(resultCode == RESULT_OK){
                    //	Bitmap bitmap = null;
                    try {
                        imagePath = FileUtils.getPath(getApplicationContext(),fileUri);
                        System.out.println("IMAGEPATH camera = "+imagePath);
                        bm = uploadphoto(FileUtils.getPath(getApplicationContext(), fileUri));

                        switch(addDocType){
                            case "Pan":
                                pan_add_layout.setVisibility(View.GONE);
                                //pan_img_add.setVisibility(View.GONE);
                                pan_img.setVisibility(View.VISIBLE);
                                pan_img.setImageBitmap(bm);
                                pan_remove.setVisibility(View.VISIBLE);
                                break;
                            case "Adhaar_front":
                                adhaar_add_front_layout.setVisibility(View.GONE);
                                // adhaar_img_front_add.setVisibility(View.GONE);
                                adhaar_img_front.setVisibility(View.VISIBLE);
                                adhaar_img_front.setImageBitmap(bm);
                                adhaar_front_remove.setVisibility(View.VISIBLE);
                                break;
                            case "Adhaar_back":
                                adhaar_add_back_layout.setVisibility(View.GONE);
                                //adhaar_img_back_add.setVisibility(View.GONE);
                                adhaar_img_back.setVisibility(View.VISIBLE);
                                adhaar_img_back.setImageBitmap(bm);
                                adhaar_back_remove.setVisibility(View.VISIBLE);
                                break;
                            default:
                                break;
                        }
                        //	new UploadFile().execute(imagePath);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public Bitmap uploadphoto(String pathName) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 420, 620);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return   BitmapFactory.decodeFile(pathName, options);

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public class CustomArrayAdapter extends ArrayAdapter<String>{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<String> items;
    private final int mResource;

    private final String[] id_cat = {"Select Id", "Pan Card", "Adhaar card"};

    public CustomArrayAdapter( Context context, int resource, List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView offTypeTv = view.findViewById(R.id.spinner_item);

        offTypeTv.setText(items.get(position).toString());

        if(position==0){
            offTypeTv.setTextColor(getColor(R.color.grey));
        }

        return view;
    }
 }
}