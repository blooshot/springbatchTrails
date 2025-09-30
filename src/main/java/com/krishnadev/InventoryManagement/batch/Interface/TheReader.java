package com.krishnadev.InventoryManagement.batch.Interface;

import com.opencsv.bean.CsvToBean;

public interface TheReader {

    CsvToBean createReader(String uploadedFile);

}
