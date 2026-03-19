package com.denisarruda.patientcheckout.boundary;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.denisarruda.patientcheckout.entity.PatientCheckoutEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Logger;

public class BillManagementLambda {

    private static final Logger logger = Logger.getLogger(BillManagementLambda.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handler(SNSEvent event) {
        event.getRecords().forEach(record -> {
            PatientCheckoutEvent patientCheckoutEvent;
            try {
                patientCheckoutEvent = objectMapper.readValue(record.getSNS().getMessage(), PatientCheckoutEvent.class);
                logger.info("Received " + patientCheckoutEvent);
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        
    }
}
