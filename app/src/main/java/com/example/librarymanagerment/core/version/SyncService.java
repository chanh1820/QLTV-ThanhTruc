package com.example.librarymanagerment.core.version;

import android.content.Context;
import android.util.Log;
import android.view.View;
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
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.BookReadingDTO;
import com.example.librarymanagerment.core.dto.StudentDTO;
import com.example.librarymanagerment.core.dto.VideoDTO;
import com.example.librarymanagerment.core.utils.NotifyUtils;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SyncService {

    VersionDAO versionDAO;

    SyncService (Context context){
        versionDAO = new VersionDAO(context);
    }
    public void processSyncData(Context context, List<VersionDTO> versionDTOListOld, List<VersionDTO> versionDTOListNew ){
        Map<String, String> versonServerNew= versionDTOListNew.stream().collect(Collectors.toMap(VersionDTO::getVersionCode, VersionDTO::getVersionIndex));
        for (VersionDTO itemOld : versionDTOListOld) {
            String versionIndexNew = versonServerNew.get(itemOld.getVersionCode());
            if(!versionIndexNew.equals(itemOld.getVersionIndex())){
                VersionDTO versionDTO = new VersionDTO();
                versionDTO.setVersionCode(itemOld.getVersionCode());
                versionDTO.setVersionIndex(versionIndexNew);
                switch (itemOld.getVersionCode()){
                    case VersionConstant.CODE_BOOK:
                        processSyncBook(context, versionDTO);
                        VersionConstant.processSyncCount ++;
                        break;
                    case VersionConstant.CODE_CATEGORY:
                        processSyncCategory(context, versionDTO);
                        VersionConstant.processSyncCount ++;
                        break;
                    case VersionConstant.CODE_VIDEO:
                        processSyncVideo(context, versionDTO);
                        VersionConstant.processSyncCount ++;
                        break;
                    case VersionConstant.CODE_STUDENT:
                        processSyncStudent(context, versionDTO);
                        VersionConstant.processSyncCount ++;
                        break;
                    case VersionConstant.CODE_BOOK_READING:
                        processSyncBookReading(context, versionDTO);
                        VersionConstant.processSyncCount ++;
                        break;
                    default:
                        NotifyUtils.defaultNotify(context, "Lỗi");
                        break;
                }
            }
        }

        // set count syncProcessCount = -1, end sync
        VersionConstant.processSyncCount --;
    }

    void processSyncBook(Context context, VersionDTO versionDTO){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                List<BookDTO> bookDTOList = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<BookDTO>>() {});
                versionDAO.deleteAllBook();
                versionDAO.saveALlBookDTO(bookDTOList);
                versionDAO.updateVersion(versionDTO);
                VersionConstant.processSyncCount --;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("sync book onErrorResponse",error.getMessage());
                Toast.makeText(context, "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> payload =  new HashMap<>();
                payload.put("action", GoogleSheetConstants.ACTION_GET_ALL_BOOK);
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

    void processSyncCategory(Context context, VersionDTO versionDTO){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                versionDAO.deleteAllBookCollection();
                List<BookCollectionDTO> bookCollectionDTOList = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<BookCollectionDTO>>() {});
                versionDAO.saveALlBookCollection(bookCollectionDTOList);
                versionDAO.updateVersion(versionDTO);
                VersionConstant.processSyncCount --;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("sync book onErrorResponse",error.getMessage());
                Toast.makeText(context, "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> payload =  new HashMap<>();
                payload.put("action", GoogleSheetConstants.ACTION_GET_ALL_CATEGORY);
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

    void processSyncVideo(Context context, VersionDTO versionDTO){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                versionDAO.deleteAllVideo();
                List<VideoDTO> videoDTOList = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<VideoDTO>>() {});
                versionDAO.saveALlVideo(videoDTOList);
                versionDAO.updateVersion(versionDTO);
                VersionConstant.processSyncCount --;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("sync book onErrorResponse",error.getMessage());
                Toast.makeText(context, "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> payload =  new HashMap<>();
                payload.put("action", GoogleSheetConstants.ACTION_GET_ALL_VIDEO);
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

    void processSyncStudent(Context context, VersionDTO versionDTO){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                versionDAO.deleteAllStudent();
                List<StudentDTO> list
                        = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<StudentDTO>>() {});
                versionDAO.saveAllStudent(list);
                versionDAO.updateVersion(versionDTO);
                VersionConstant.processSyncCount --;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("sync book onErrorResponse",error.getMessage());
                Toast.makeText(context, "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> payload =  new HashMap<>();
                payload.put("action", GoogleSheetConstants.ACTION_GET_ALL_STUDENT);
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

    void processSyncBookReading(Context context, VersionDTO versionDTO){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                versionDAO.deleteAllBookReading();
                List<BookReadingDTO> list
                        = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<BookReadingDTO>>() {});
                versionDAO.saveAllBookReading(list);
                versionDAO.updateVersion(versionDTO);
                VersionConstant.processSyncCount --;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("sync book onErrorResponse",error.getMessage());
                Toast.makeText(context, "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> payload =  new HashMap<>();
                payload.put("action", GoogleSheetConstants.ACTION_GET_ALL_BOOK_READING);
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
