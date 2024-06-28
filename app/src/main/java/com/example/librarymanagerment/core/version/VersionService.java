package com.example.librarymanagerment.core.version;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VersionService {
    public static void processCheckVersion(Context context) {
        Log.e("log", "star processCheckVersion");
        VersionDAO versionDAO = new VersionDAO(context);
        List<VersionDTO> versionDTOListOld = versionDAO.findAllVersion();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                List<VersionDTO> versionDTOListNew = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<VersionDTO>>() { });
                SyncService syncService = new SyncService(context);
                syncService.processSyncData(context, versionDTOListOld, versionDTOListNew);
                Log.e("log", "end processCheckVersion");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.getMessage());
                Log.e("log", "error processCheckVersion" + error.getMessage());
                Toast.makeText(context, "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> payload = new HashMap<>();
                payload.put("action", GoogleSheetConstants.ACTION_GET_ALL_VERSION);
                return payload;
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
