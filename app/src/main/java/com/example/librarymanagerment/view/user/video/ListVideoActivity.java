package com.example.librarymanagerment.view.user.video;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.KeyConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.OrderPickedDTO;
import com.example.librarymanagerment.core.dto.VideoDTO;
import com.example.librarymanagerment.core.sco.VideoSCO;
import com.example.librarymanagerment.view.user.docsach.ReadBookActivity;
import com.example.librarymanagerment.view.user.docsach.ReadBookViewActivity;
import com.example.librarymanagerment.view.user.muonsach.sach.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ListVideoActivity extends AppCompatActivity {
    Spinner spnChooseClass, spnChooseCategory;
    TextView tvQuantity;
    RecyclerView rvListVideo;
    Button btnBack;

    SPNChooseClassAdapter spnChooseClassAdapter;
    SPNChooseCategoryAdapter spnChooseCategoryAdapter;
    ListVideoAdapter listVideoAdapter;
    GeneralDAO generalDAO;
    List<String> categoryList = new ArrayList<>();
    List<String> classRoomList = new ArrayList<>();
    List<VideoDTO> videoDTOList = new ArrayList<>();
    VideoSCO videoSCO = new VideoSCO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);
        initView();
        action();
    }

    private void action() {
        spnChooseClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                videoSCO.setClassRoom((String) view.getTag());
                updateRecyclerView(videoSCO);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnChooseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                videoSCO.setCategory((String) view.getTag());
                updateRecyclerView(videoSCO);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        spnChooseCategory = findViewById(R.id.spn_list_video_choose_category);
        spnChooseClass = findViewById(R.id.spn_list_video_choose_class_room);
        tvQuantity = findViewById(R.id.tv_list_video_quantity);
        rvListVideo = findViewById(R.id.rv_list_video);
        btnBack = findViewById(R.id.btn_back_list_video);

        generalDAO = new GeneralDAO(getApplicationContext());
        categoryList = generalDAO.getCategoryByListVideo();
        classRoomList = generalDAO.getClassRoomByListVideo();

        spnChooseCategoryAdapter = new SPNChooseCategoryAdapter(getApplicationContext(), categoryList);
        spnChooseCategory.setAdapter(spnChooseCategoryAdapter);
        spnChooseCategory.setSelection(getIntent().getIntExtra(KeyConstants.INTENT_POSITION, 0));

        spnChooseClassAdapter = new SPNChooseClassAdapter(getApplicationContext(), classRoomList);
        spnChooseClass.setAdapter(spnChooseClassAdapter);

        updateRecyclerView(videoSCO);
    }

    private void updateRecyclerView(VideoSCO videoSCO) {
        videoDTOList = generalDAO.findVideo(videoSCO);
        tvQuantity.setText(String.valueOf(videoDTOList.size()));
        listVideoAdapter = new ListVideoAdapter(getApplicationContext(), videoDTOList, new OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                VideoDTO videoDTO = (VideoDTO) item;
                Intent intent = new Intent(ListVideoActivity.this, VideoViewActivity.class);
                intent.putExtra(KeyConstants.INTENT_WEB_VIEW_VIDEO_ID, videoDTO.getLink());
                intent.putExtra(KeyConstants.INTENT_WEB_VIEW_VIDEO_WEB, videoDTO.getLinkWeb());
                startActivity(intent);
            }
            @Override
            public void onItemClickIncreaseButton(OrderPickedDTO item) {

            }

            @Override
            public void onItemClickDecreaseButton(OrderPickedDTO item) {

            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvListVideo.setLayoutManager(llm);
        rvListVideo.setAdapter(listVideoAdapter);

    }
}