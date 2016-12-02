package com.example.pareeya.ahelp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    //Explicit
    private TextView phone1TextView, phone2TextView, phone3TextView,
            phone4TextView, phone5TextView;
    private ImageView addPhone1ImageView, addPhone2ImageView,
            addPhone3ImageView, addPhone4ImageView, addPhone5ImageView;
    private ImageView deletePhone1ImageView, deletePhone2ImageView,
            deletePhone3ImageView, deletePhone4ImageView, deletePhone5ImageView;
    private RadioGroup radioGroup;
    private RadioButton phone1RadioButton, phone2RadioButton,
            phone3RadioButton, phone4RadioButton, phone5RadioButton;
    private ListView listView;
    private Button button;
    private String urlJSON = "http://swiftcodingthai.com/fai/get_User_kanyarat.php";
    private String[] nameStrings, phoneStrings, passwordStrings;
    private String nameChooseString, phoneChooseString, passwordChooseString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Bind Widget
        bindWidget();

        //Create ListView
        SynUser synUser = new SynUser(SettingActivity.this);
        synUser.execute(urlJSON);

        //Image Controller
        addPhone1ImageView.setOnClickListener(SettingActivity.this);
        addPhone2ImageView.setOnClickListener(SettingActivity.this);
        addPhone3ImageView.setOnClickListener(SettingActivity.this);
        addPhone4ImageView.setOnClickListener(SettingActivity.this);
        addPhone5ImageView.setOnClickListener(SettingActivity.this);
        deletePhone1ImageView.setOnClickListener(SettingActivity.this);
        deletePhone2ImageView.setOnClickListener(SettingActivity.this);
        deletePhone3ImageView.setOnClickListener(SettingActivity.this);
        deletePhone4ImageView.setOnClickListener(SettingActivity.this);
        deletePhone5ImageView.setOnClickListener(SettingActivity.this);


    }//Main Method


    @Override
    public void onClick(View view) {

        Log.d("29octV2", "Click ImageAdd");

        switch (view.getId()) {

            case R.id.imageView6:

                confirmPassword(phone1TextView, nameChooseString,
                        phoneChooseString, passwordChooseString);

                break;
            case R.id.imageView7:

                confirmPassword(phone2TextView, nameChooseString,
                        phoneChooseString, passwordChooseString);

                break;
            case R.id.imageView8:

                confirmPassword(phone3TextView, nameChooseString,
                        phoneChooseString, passwordChooseString);

                break;
            case R.id.imageView9:

                confirmPassword(phone4TextView, nameChooseString,
                        phoneChooseString, passwordChooseString);

                break;
            case R.id.imageView10:

                confirmPassword(phone5TextView, nameChooseString,
                        phoneChooseString, passwordChooseString);

                break;
            case R.id.imageView11:
                phone1TextView.setText("");
                break;
            case R.id.imageView12:
                phone2TextView.setText("");
                break;
            case R.id.imageView13:
                phone3TextView.setText("");
                break;
            case R.id.imageView14:
                phone4TextView.setText("");
                break;
            case R.id.imageView15:
                phone5TextView.setText("");
                break;

        }   // switch


    }   // onClick

    private void confirmPassword(final TextView phoneTextView,
                                 final String nameChooseString,
                                 final String phoneChooseString,
                                 final String passwordChooseString) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.alert);
        builder.setTitle("Password " + nameChooseString);
        builder.setMessage("โปรดกรอก Password ของ " + nameChooseString);

        final EditText editText = new EditText(SettingActivity.this);
        builder.setView(editText);

        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String strPassword = editText.getEditableText().toString().trim();

                if (strPassword.equals(passwordChooseString)) {

                    //Password True
                    phoneTextView.setText(nameChooseString);
                    dialogInterface.dismiss();

                } else {
                    //Password False
                    Toast.makeText(SettingActivity.this,
                            "Password ผิด กรุณากรอกใหม่",
                            Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }   // if

            }   // onClick
        });
        builder.show();


    }   // confirm

    private class SynUser extends AsyncTask<String, Void, String> {

        private Context context;


        public SynUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("29octV1", "e doInBack ==> " + e.toString());
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("29octV1", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);

                nameStrings = new String[jsonArray.length()];
                phoneStrings = new String[jsonArray.length()];
                passwordStrings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    nameStrings[i] = jsonObject.getString("Name");
                    phoneStrings[i] = jsonObject.getString("Phone");
                    passwordStrings[i] = jsonObject.getString("Password");

                }   // for

                PhoneAdapter phoneAdapter = new PhoneAdapter(context,
                        nameStrings, phoneStrings);
                listView.setAdapter(phoneAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Toast.makeText(context, "คุณเลือก " + nameStrings[i],
                                Toast.LENGTH_SHORT).show();

                        nameChooseString = nameStrings[i];
                        phoneChooseString = phoneStrings[i];
                        passwordChooseString = passwordStrings[i];

                    }   // onItmeClick
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }   // SynUser Class


    private void bindWidget() {
        phone1TextView = (TextView) findViewById(R.id.textView5);
        phone2TextView = (TextView) findViewById(R.id.textView6);
        phone3TextView = (TextView) findViewById(R.id.textView9);
        phone4TextView = (TextView) findViewById(R.id.textView10);
        phone5TextView = (TextView) findViewById(R.id.textView11);


        addPhone1ImageView = (ImageView) findViewById(R.id.imageView6);
        addPhone2ImageView = (ImageView) findViewById(R.id.imageView7);
        addPhone3ImageView = (ImageView) findViewById(R.id.imageView8);
        addPhone4ImageView = (ImageView) findViewById(R.id.imageView9);
        addPhone5ImageView = (ImageView) findViewById(R.id.imageView10);

        deletePhone1ImageView = (ImageView) findViewById(R.id.imageView11);
        deletePhone2ImageView = (ImageView) findViewById(R.id.imageView12);
        deletePhone3ImageView = (ImageView) findViewById(R.id.imageView13);
        deletePhone4ImageView = (ImageView) findViewById(R.id.imageView14);
        deletePhone5ImageView = (ImageView) findViewById(R.id.imageView15);


        phone1RadioButton = (RadioButton) findViewById(R.id.radioButton6);
        phone2RadioButton = (RadioButton) findViewById(R.id.radioButton7);
        phone3RadioButton = (RadioButton) findViewById(R.id.radioButton8);
        phone4RadioButton = (RadioButton) findViewById(R.id.radioButton9);
        phone5RadioButton = (RadioButton) findViewById(R.id.radioButton10);

        listView = (ListView) findViewById(R.id.livFriend);
        radioGroup = (RadioGroup) findViewById(R.id.ragPhone);


    }//bindWidget

    public void clickSetting(View view) {
        //startActivity(new Intent(SettingActivity.this,HomeActivity.class));

    }//clickSetting
}//MAIN Class
