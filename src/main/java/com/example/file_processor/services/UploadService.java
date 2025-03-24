package com.example.file_processor.services;

import com.example.file_processor.configurations.RabbitMqConfiguration;
import com.example.file_processor.dtos.ContentRowData;
import com.example.file_processor.interfaces.UploadServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UploadService implements UploadServiceInterface {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private Gson gson;

    UploadService(RabbitTemplate rabbitTemplate, Gson gson){
        this.rabbitTemplate = rabbitTemplate;
        this.gson = gson;
    }

    @Override
    public void uploadFile(MultipartFile file) {
        //process file
        System.out.println("file received");
        System.out.println(file.getContentType() );

        List<ContentRowData> contentRowData;

        if (file.isEmpty())
            throw new IllegalStateException("File is empty..");

        try {
            contentRowData = processFile(file);
        }catch (IOException e){
            e.printStackTrace();
            throw new IllegalStateException("Could not parse the file..");
        }


        //construct a json object
        try {
            for (ContentRowData data: contentRowData){
                //send message
                rabbitTemplate.convertAndSend(RabbitMqConfiguration.QUEUE_NAME, gson.toJson(data));
                System.out.println("Sent: " + gson.toJson(data));

                //wait for one second then send another email
                Thread.sleep(1000);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
            throw new IllegalStateException("Could not parse the file..");
        }
    }

    private List<ContentRowData> processFile(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<ContentRowData> data = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                System.out.println("looping through record for "+row.getCell(0).getStringCellValue());

                ContentRowData model = new ContentRowData();
                model.setName(row.getCell(0).getStringCellValue());
                model.setEmail(row.getCell(1).getStringCellValue());
                model.setMessage(row.getCell(2).getStringCellValue());
                data.add(model);
            }

            System.out.println("finished parsing data.. found "+data.size()+" records...");
            return data;
        }
    }
}
