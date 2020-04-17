package org.dev4u.hv.my_diagnostic;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import db.Disease;
import db.User;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.DiseaseUtilitesSingleton;
import utils.SaveImage;

public class UserDataActivity extends AppCompatActivity {

    private final int RESULT_FROM_MAIN=2;
    private Animatable cursiveAvd;
    private ImageView hearth;
    private CircleImageView circleImageView;
    private int PICK_PHOTO_FOR_AVATAR=3;
    private ImageView imageViewDialog;
    private Bitmap activePicture;
    private Button btnDate, btnCountry,btnBlood;
    private FloatingActionButton btnSave;
    private LinearLayout container;
    private AnimationDrawable anim;
    private int date[] = new int[3];
    private Handler handler = new Handler();
    private User user=null;
    private ProgressDialog progressDialog;
    private TextView txtName;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editorPreferences;
    private RadioButton rbnMale;
    private RadioButton rbnFemale;
    private int status;
    private int flag_id;
    //private int Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Profile");
        setContentView(R.layout.activity_user_data);
        preferences         = getSharedPreferences("Data", Context.MODE_PRIVATE);
        editorPreferences   = preferences.edit();

        status              = preferences.getInt("STATUS",0);

        flag_id             = preferences.getInt("FLAG",R.drawable.flag_sv);

        DiseaseUtilitesSingleton.getInstance().init(this);

        String userStatus   = preferences.getString("USERNAME","null");
        if(!userStatus.equals("null")){
            user = DiseaseUtilitesSingleton.getInstance().getUser(userStatus);
        }

        activePicture       = ((BitmapDrawable) getBaseContext().getDrawable(R.drawable.ic_profile)).getBitmap();
        circleImageView     = (CircleImageView)findViewById(R.id.profile_image);
        //buttons
        btnDate             = (Button)findViewById(R.id.btnDate);
        btnCountry          = (Button)findViewById(R.id.btnCountry);
        btnBlood            = (Button) findViewById(R.id.btnBlood);
        btnSave             = (FloatingActionButton) findViewById(R.id.btnSave);
        //edit text
        txtName             = (EditText) findViewById(R.id.input_name);
        //radio buttons
        rbnMale             = (RadioButton) findViewById(R.id.rbnMale);
        rbnFemale           = (RadioButton) findViewById(R.id.rbnFemale);

        Country country     = Country.getCountryByLocale(getResources().getConfiguration().locale);
        btnCountry.setCompoundDrawables(getDrawable(country.getFlag()),null,null,null);

        //day - month - year
        date[0] = 1;date[1] = 8;date[2] = 1994;



        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPicture();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        btnCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCountry();
            }
        });
        btnBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBlood();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton();
            }
        });

        Bitmap b = loadImageFromStorage("Profile","profile.png");
        if(b!=null) {
            activePicture = b;
            circleImageView.setImageBitmap(Bitmap.createScaledBitmap (b,(int) (b.getWidth() * .4), (int) (b.getHeight() * .4),true));
        }

        DiseaseUtilitesSingleton.getInstance().fillPrimaryData();

        if(user!=null){
            txtName.setText(user.getFullname());
            String bday = user.getBirthday();
            //YYYY-MM-DD date format
            String parts[] = bday.split("-");
            date[2] = Integer.parseInt(parts[0]);//year
            date[1] = Integer.parseInt(parts[1]);//month
            date[0] = Integer.parseInt(parts[2]);//day
            if(user.getGenre().equals("male")){
                rbnMale.setChecked(true);
            }else{
                rbnFemale.setChecked(true);
            }
            btnCountry.setText(user.getName_country());
            btnBlood.setText(user.getName_bloodtype());
        }
        btnDate.setText(new StringBuilder()
                .append(date[0]).append("/").append(date[1]).append("/")
                .append(date[2]).append(" "));
    }

    private void showDialogPicture(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //dialogBuilder.setTitle("Profile Picture");
        dialogBuilder.setPositiveButton(R.string.change_picture, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pickImage();
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_picture, null);
        dialogBuilder.setView(dialogView);
        imageViewDialog = (ImageView) dialogView.findViewById(R.id.imageViewDialog);
        imageViewDialog.setImageBitmap(activePicture);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation_Window;
        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }


    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    public void pickDate(){
        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date[0] = dayOfMonth;
                        date[1] = month+1;
                        date[2] = year;
                        btnDate.setText(new StringBuilder()
                                // Month is 0 based so add 1
                                .append(date[0]).append("/").append(date[1]).append("/")
                                .append(date[2]).append(" "));
                    }
                }, date[2], date[1]-1, date[0]);//yyyy , mm+1 , dd
        dialog.show();
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public void pickCountry(){
        final CountryPicker picker = CountryPicker.newInstance(getString(R.string.select_your_country));  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                if(name.contains(",")){
                    name = name.split(",")[0];
                }
                flag_id = flagDrawableResID;
                btnCountry.setText(name);
                picker.dismiss();

            }
        });
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
    }

    public void pickBlood(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(R.string.select_your_blood_type);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        arrayAdapter.add("O+");
        arrayAdapter.add("O-");
        arrayAdapter.add("A+");
        arrayAdapter.add("A-");
        arrayAdapter.add("B+");
        arrayAdapter.add("B-");
        arrayAdapter.add("AB+");
        arrayAdapter.add("AB-");
        builderSingle.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                btnBlood.setText(strName);
            }
        });
        AlertDialog alertDialog = builderSingle.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation_Window;
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                Toast.makeText(this, R.string.error_loading_image,Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
            if(bmp!=null){
                activePicture = bmp;
                circleImageView.setImageBitmap(Bitmap.createScaledBitmap (bmp,(int) (bmp.getWidth() * .4), (int) (bmp.getHeight() * .4),true));
                new SaveImage(this,"Profile","profile.png",btnSave).execute(bmp);
            }

        }
    }



    private Bitmap loadImageFromStorage(String path,String name)
    {
        try {
            ContextWrapper cw = new ContextWrapper(this);
            File directory = cw.getDir(path, Context.MODE_PRIVATE);
            File f=new File(directory, name);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;

    }


    private void saveButton(){
        if(!saveValid()){
            return;
        }
        String str_date  = date[2]+"-"+date[1]+"-"+date[0];
        String gender = (rbnMale.isChecked())?"male":"female";
        String id_country = DiseaseUtilitesSingleton.getInstance().getIdCountry(btnCountry.getText().toString());
        String id_bloodtype = DiseaseUtilitesSingleton.getInstance().getIdBloodType(btnBlood.getText().toString());
        String username;
        if(user==null){
            username = txtName.getText().toString().substring(0,4);
            //primer inicio de sesion es necesario crear un id de usario
            username+=str_date+gender.substring(0,2)+id_bloodtype+id_country;
            editorPreferences.putString("USERNAME",username);
            editorPreferences.commit();
        }else{
            username = user.getUsername();
        }
        user = new User(
                username,
                txtName.getText().toString(),
                gender,
                str_date,
                id_country,
                id_bloodtype,
                btnCountry.getText().toString(),
                btnBlood.getText().toString()
        );

        //saving the flag
        editorPreferences.putInt("FLAG",flag_id);
        editorPreferences.commit();

        DiseaseUtilitesSingleton.getInstance().updateUser(user);
        //hideProgressDialog();
        if(status==0){

            editorPreferences.putInt("STATUS",1);//status saved
            editorPreferences.commit();
            Intent gotoBeginning = new Intent(this,MainActivity.class);
            startActivity(gotoBeginning);
            this.finish();
        }else{
            setResult(RESULT_FROM_MAIN);
            this.finish();
        }

    }

    private boolean saveValid(){
        if(txtName.getText().length()<4){
            Toast.makeText(this, R.string.the_name_should_contain,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!rbnMale.isChecked() && !rbnFemale.isChecked()){
            Toast.makeText(this, R.string.please_select_your_genre,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }



}
