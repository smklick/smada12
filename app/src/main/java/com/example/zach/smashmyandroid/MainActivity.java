
package com.example.zach.smashmyandroid;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.zach.smashmyandroid.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import javax.sql.DataSource;

public class MainActivity extends AppCompatActivity {

    TextView membersTextView = findViewById(R.id.membersTextView);
    String userName, password;
    RequestQueue requestQueue;
    String baseUrl = "http://localhost:5002/api/";
    String url = "https://api.github.com/users/DadMuscles";
    Button submitButton = findViewById(R.id.submitButton);
/*    Button userLoginButton = findViewById(R.id.loginButton);
    EditText usernameTextField = findViewById(R.id.user_name);
    EditText userPasswordTextField = findViewById(R.id.user_password);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //getMembersList();
            }
        });
    }

   /* private void clearMembersList() {
        this.membersTextView.setText("");
    }

    private void addMemberToList(String memberFirstName, String memberLastName){
        String strRow = memberFirstName + " / " + memberLastName;
        String currentText = membersTextView.getText().toString();
        this.membersTextView.setText(currentText + "\n" + strRow);
    }

    private void setMemberList(String message) {
        this.membersTextView.setText(message);
    }
    private void getMembersList() {
        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>(){
            @Override
                    public void onResponse(JSONArray response){
                if(response.length() > 0) {
                    for(int i = 0; i < response.length(); i++) {
                        try{
                            JSONObject jsonObj = response.getJSONObject(i);
*//*                            String memberFirstName = jsonObj.get("firstName").toString();
                            String memberLastName = jsonObj.get("lastName").toString();*//*
                            String repoName = jsonObj.get("name").toString();
                            String lastUpdate = jsonObj.get("updated_at").toString();
                            addMemberToList(repoName, lastUpdate);
                        }catch (JSONException e) {
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                } else {
                    setMemberList("No Members Found");
                }
            }
                },
        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setMemberList("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
        });
        requestQueue.add(arrReq);
    }*/

}
