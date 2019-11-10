package com.example.hellonotepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class ListNotes extends AppCompatActivity {
    ListView listView;
    List<NoteData> list;
    Button button;
    String getApi = "http://13.233.64.181:4000/api/getnotes";
    String editApi = "http://13.233.64.181:4000/api/editnote";
    String deleteApi = "http://13.233.64.181:4000/api/deletenote";
    String title = "";
    String description = "";
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.btn_allnotes);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListNotes.this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    viewNote(getApi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


    }

    public void onResume() {
        super.onResume();
        list = new ArrayList<>();
        MyAdapter myAdapter = new MyAdapter(ListNotes.this, list);
        listView.setAdapter(myAdapter);
    }

    public void onEditClicked(final String editApi) throws JSONException {

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("_id", "5db024d14a96a7082ae8d028");
        jsonObject.put("title", "edi th basqwdfeic bb dfdffdf");
        jsonObject.put("description", "anythgdfhgjhing");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, editApi, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("################", String.valueOf(response));

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dataobj = jsonArray.getJSONObject(i);
                        String title = dataobj.getString("title");
                        String description = dataobj.getString("description");
                        Log.e("success", title + "\n" + description);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(ListNotes.this, EditActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                startActivity(intent);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", String.valueOf(error));

            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }

    public void onDeleteClicked(final String deleteApi) throws JSONException {

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("_id", "5dc03e3fd3c99c7cdbc8db0e");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteApi, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("__________________", String.valueOf(response));

                try {
                    JSONObject dataobj = response.getJSONObject("data");
                    String id = dataobj.getString("_id");
                    Log.e("deleted", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", String.valueOf(error));

            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }


    public void viewNote(String get) throws JSONException {
        list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", "5dbd22e9d3c99c7cdbc8da6c");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, get, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("@@@@@@@@@@@@@@@@@", String.valueOf(response));

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        NoteData noteData = new NoteData();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String description = jsonObject.getString("description");
                        noteData.setTitle(title);
                        noteData.setDescription(description);
                        list.add(noteData);
                        Log.e("success", title + "\n" + description);

                    }

                    for (int i = 0; i < list.size(); i++) {
                        NoteData noteData = list.get(i);
                        Log.e("Note_data", noteData.getTitle() + "\n" + noteData.getDescription());
                    }

                    MyAdapter myAdapter = new MyAdapter(ListNotes.this, list);
                    listView.setAdapter(myAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick (AdapterView < ? > adapterView, View view,int i, long l){
                            NoteData noteData = list.get(i);

                            Log.e("listview",noteData.getTitle());



                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", String.valueOf(error));

            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private class MyAdapter extends BaseAdapter {

        Context context;
        List <NoteData> list;

        public MyAdapter(Context context, List<NoteData> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view=LayoutInflater.from(context).inflate(R.layout.list_out,viewGroup,false);
            final TextView title=view.findViewById(R.id.txttitle);
            final TextView description=view.findViewById(R.id.txtMemo);
            ImageView btnedit=view.findViewById(R.id.btnEdit);
            ImageView btndelete=view.findViewById(R.id.btnDelete);

            title.setText(list.get(i).getTitle());
            description.setText(list.get(i).getDescription());

            btnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        onEditClicked(editApi);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        onDeleteClicked(deleteApi);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            return view;

        }
    }
}
