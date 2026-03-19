package com.denisarruda.patientcheckout.boundary;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.denisarruda.patientcheckout.entity.PatientCheckoutEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PatientCheckoutLambda {

    private static final Logger logger = Logger.getLogger(PatientCheckoutLambda.class.getName());
    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    private final AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handler(S3Event event) {
        event.getRecords().forEach(record -> {
            String bucketName = record.getS3().getBucket().getName();
            String objectKey = record.getS3().getObject().getKey();
            S3ObjectInputStream s3ObjectInputStream = s3Client.getObject(bucketName, objectKey).getObjectContent();
            try {
                List<PatientCheckoutEvent> patientCheckoutEvents = Arrays.asList(objectMapper.readValue(s3ObjectInputStream, PatientCheckoutEvent[].class));
                logger.info("Received " + patientCheckoutEvents);
                s3ObjectInputStream.close();
                patientCheckoutEvents.forEach(patientCheckoutEvent -> {
                    try {
                        snsClient.publish(System.getenv("PATIENT_CHECKOUT_TOPIC"), objectMapper.writeValueAsString(patientCheckoutEvent));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error processing S3 object: " + e.getMessage(), e);
                throw new RuntimeException("Error processing S3 object", e);
            }
        });
    }
}