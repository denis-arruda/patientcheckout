package com.denisarruda.patientcheckout.boundary;

import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;

public class ErrorHandler {

    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());

    public void handleError(SNSEvent event) {
        event.getRecords().forEach(record -> {
            logger.severe("DLQ event: " + record.toString());
        });        
    }
}
