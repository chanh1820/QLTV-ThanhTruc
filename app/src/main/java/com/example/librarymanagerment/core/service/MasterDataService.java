package com.example.librarymanagerment.core.service;

import com.example.librarymanagerment.core.dto.MasterDataDTO;

import java.util.List;

public class MasterDataService {
    private static MasterDataService instance;

    private static MasterDataDTO masterDataDTO;
    private MasterDataService() {
    }

    public static MasterDataService getInstance() {
        if (instance == null) {
            instance = new MasterDataService();
        }
        return instance;
    }

    public static MasterDataDTO getMasterDataDTO() {
        return masterDataDTO;
    }

    public static void setMasterDataDTO(MasterDataDTO masterDataDTO) {
        MasterDataService.masterDataDTO = masterDataDTO;
    }
}
