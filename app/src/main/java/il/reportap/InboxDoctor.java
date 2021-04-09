package il.reportap;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.loginregister.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InboxDoctor extends OptionsMenu {

    private RecyclerView recyclerView;
    private AdapterActivity adapter;
    private List<ModelActivity> modelActivityList;

    private HashMap<Integer,String> repMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_doctor);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelActivityList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_INBOXDR,
                //lambda expression
                response -> {


                    //repMap = new HashMap<Integer,String>();

                    try {
                        JSONObject repObj = new JSONObject(response);
                        JSONArray repArray = repObj.getJSONArray("report");
                        JSONObject jObg = new JSONObject();
                        ModelActivity modelActivity = new ModelActivity();

                        for(int i=0; i<=2; i++){

                            jObg = repArray.getJSONObject(i);
                            modelActivity.setId(jObg.getInt("id"));
                            modelActivity.setSentTime(jObg.getString("sent_time"));
                            modelActivity.setPatientId(jObg.getString("patient_id"));
                            modelActivity.setTestName(jObg.getString("name"));
                            modelActivity.setUrgent(jObg.getInt("is_urgent"));
                            modelActivityList.add(modelActivity);
                            System.out.println(modelActivityList.get(i).getId());
                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter = new AdapterActivity(modelActivityList,getApplicationContext());
                    recyclerView.setAdapter(adapter);

                },
                //lambda expression
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show())
        {
            @Nullable
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("department", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getDepartment()));
                System.out.println(params.get("department"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
