package com.example.pareeya.ahelp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class HomeActivity extends AppCompatActivity {

    //Explicit
    private Button button;
    private ImageView img;
    private String truePasswordString, userPasswordString,
            idUserString, nameString, idCallString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // image animation
        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        img = (ImageView) findViewById(R.id.imageView3);
        img.setBackgroundResource(R.drawable.butalarm);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();

        //Call 1669
        call1669();

        //Img Controller
        imgController();

        //Find Id user
        findIDuser();

        //My Loop
        myLoop();


    }   // Main Method

    private void findIDuser() {
        //Find idUser
        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM userTABLE", null);
            cursor.moveToFirst();
            nameString = cursor.getString(cursor.getColumnIndex(MyManage.column_Name));
            truePasswordString = cursor.getString(cursor.getColumnIndex(MyManage.column_Password));
            Log.d("8decV3", "truePass ==> " + truePasswordString);
            cursor.close();


            FindIDuser findIDuser = new FindIDuser(HomeActivity.this,
                    nameString, truePasswordString);
            findIDuser.execute();
            String strJSON = findIDuser.get();
            Log.d("8decV3", "JSoN ==> " + strJSON);

            JSONArray jsonArray = new JSONArray(strJSON);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            idUserString = jsonObject.getString("id");

            Log.d("8decV3", "idUser ==> " + idUserString);

//            //Find idCall
//            findIDcall();

        } catch (Exception e) {
            e.printStackTrace();
        }   // try
    }

    private void myLoop() {

        //My To do
        try {

            SynAhelp synAhelp = new SynAhelp(HomeActivity.this, idUserString);
            synAhelp.execute();
            String strJSON = synAhelp.get();
            Log.d("8decV3", "JSON ==> " + strJSON);

        } catch (Exception e) {
            Log.d("8decV3", "e myLoop ==> " + e.toString());
        }


        //Delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myLoop();
            }
        }, 1000);


    }   // myLoop

    private void imgController() {

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmPassword();

            }   // onClick
        });

    }

    private void confirmPassword() {

        //Get Password from SQLite
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM userTABLE", null);
        cursor.moveToFirst();
        nameString = cursor.getString(cursor.getColumnIndex(MyManage.column_Name));
        truePasswordString = cursor.getString(cursor.getColumnIndex(MyManage.column_Password));
        Log.d("8decV2", "truePass ==> " + truePasswordString);
        cursor.close();

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.alert);
        builder.setTitle("Type Password");
        builder.setMessage("Please Type Your Password");

        final EditText editText = new EditText(HomeActivity.this);
        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userPasswordString = editText.getText().toString().trim();
                if (userPasswordString.equals(truePasswordString)) {
                    //Password True
                    callFriend();
                    dialogInterface.dismiss();
                } else {
                    passwordFalse();
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }   // confirm

    private void passwordFalse() {
        MyAlert myAlert = new MyAlert();
        myAlert.myDialog(HomeActivity.this, "Password False",
                "Please Try again Password False");
    }

    private void callFriend() {

        //Find idUser
        try {

            FindIDuser findIDuser = new FindIDuser(HomeActivity.this,
                    nameString, truePasswordString);
            findIDuser.execute();
            String strJSON = findIDuser.get();
            Log.d("8decV2", "JSoN ==> " + strJSON);

            JSONArray jsonArray = new JSONArray(strJSON);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            idUserString = jsonObject.getString("id");

            Log.d("8decV2", "idUser ==> " + idUserString);

            //Find idCall
            findIDcall();

        } catch (Exception e) {
            e.printStackTrace();
        }   // try




    }   // callFriend

    private void findIDcall() {
        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM phoneTABLE WHERE Action = '1'", null);
            cursor.moveToFirst();
            idCallString = cursor.getString(cursor.getColumnIndex(MyManage.column_idCall));
            Log.d("8decV2", "idCall ==> " + idCallString);

            //Edit AHelp
            editAhelp();

        } catch (Exception e) {
            Log.d("8decV2", "e find idCall ==> " + e.toString());
        }
    }

    private void editAhelp() {
        try {

            EditAhelp editAhelp = new EditAhelp(HomeActivity.this, idCallString, idUserString);
            editAhelp.execute();

            if (Boolean.parseBoolean(editAhelp.get())) {
                Toast.makeText(HomeActivity.this, "Call Friend OK", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "Cannot Call Friend", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("8decV2", "e edit AHelp ==> " + e.toString());
        }
    }

    private void call1669() {
        button = (Button) findViewById(R.id.call);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CallIntent = new Intent(Intent.ACTION_CALL);
                CallIntent.setData(Uri.parse("tel:=1669"));
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(CallIntent);

            }
        });
    }

    public void clickHomeGoSetting(View view) {

        try {

            CheckInternet checkInternet = new CheckInternet(HomeActivity.this);
            checkInternet.execute();

            if (Boolean.parseBoolean(checkInternet.get())) {
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
            } else {
                Toast.makeText(HomeActivity.this, "กรุณาตรวจสอบ Internet",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }   // clickHome

}   // Main Class
