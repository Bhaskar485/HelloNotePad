package com.example.hellonotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    EditText editText,editTextt;
    Button btnSave;
    Button btnCancel;
    Button buttonViewnotes;
    String save="http://13.233.64.181:4000/api/createnote";
    NoteData noteData;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = (EditText) findViewById(R.id.etText);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        editTextt=(EditText) findViewById(R.id.extext);
        buttonViewnotes=findViewById(R.id.btn_view);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(EditActivity.this);
        editor =sharedPreferences.edit();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
             noteData= (NoteData) bundle.get("title");
             noteData=(NoteData) bundle.get("description");
            if(noteData != null) {
                this.editText.setText(noteData.getTitle());

            }
            if (noteData != null){
                this.editTextt.setText(noteData.getDescription());
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText.getText().toString();
                String description = editTextt.getText().toString();
                String userId=sharedPreferences.getString("userId","");
                try {
                    Log.e("printing",save);
                    setBtnSave(save, title, description, userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onCancelClicked();

            }
        });

        buttonViewnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(EditActivity.this,ListNotes.class);
                startActivity(intent);

            }
        });
    }

    public void setBtnSave (String save, String title, String description, final String userId) throws JSONException {

        Log.e("created",save);
        final JSONObject jsonObject=new JSONObject();
        jsonObject.put("user_id",userId);
        jsonObject.put("title",title);
        jsonObject.put("description",description);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, save,jsonObject ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("created", String.valueOf(response));


                try {
                    JSONObject dataobj=response.getJSONObject("data");
                    String userId=dataobj.getString("user_id");
                    String title=dataobj.getString("title");
                    String description=dataobj.getString("description");
                    String id=dataobj.getString("_id");

                    editor.putString("id",id);
                    editor.putString("title",title);
                    editor.putString("description",description);
                    editor.apply();

                    Log.e("notedatataa",userId+"\n"+title+"\n"+description+"\n"+id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(EditActivity.this,"Note Saved Successfully",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", String.valueOf(error));


            }
        });
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    public void onCancelClicked(){
        this.finish();
    }


}



