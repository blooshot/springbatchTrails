package com.krishnadev.InventoryManagement.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
@Component
@Getter
@Slf4j
public class JobCompletionListener implements JobExecutionListener {

    private static String errorFileUrl;

//    @Value("${server.port}")
//    private String serverPort;

    @Override
    public void afterJob(JobExecution jobExecution) {
        String errorFilePath = jobExecution.getJobParameters().getString("errorFilePath");
        log.debug("JobExecution Completed: {}", errorFilePath);

//        if (errorFilePath != null) {
//            try {
//                String hostAddress = InetAddress.getLocalHost().getHostAddress();
//                String url = String.format("http://%s:%s/api/batch/errored-data?file=%s",
//                        hostAddress, serverPort, errorFilePath);
//                errorFileUrl = url;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

//    public static String getErrorFileUrl() {
//        return errorFileUrl;
//    }
}
