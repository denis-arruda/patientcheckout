# patientcheckout

## Project Description

This project is an AWS serverless application for processing patient checkout events. It leverages AWS Lambda, S3, and SNS to automate the handling of patient checkout data:

- Patient checkout event files are uploaded to an S3 bucket.
- An AWS Lambda function is triggered by new S3 objects, reads and parses the event data, and publishes each event to an SNS topic.
- The solution is built with Java and uses AWS SAM (Serverless Application Model) for deployment and management.


This architecture enables scalable, event-driven processing of patient checkout information in a secure and automated manner.

## Error Handling

The project includes an `ErrorHandler` Lambda function that processes events sent to the Dead Letter Queue (DLQ). If a patient checkout event cannot be processed successfully, it is sent to the DLQ, where the `ErrorHandler` logs the failed event for further investigation.

**ErrorHandler Lambda:**
- Triggered by SNS events from the DLQ
- Logs the details of failed events for troubleshooting

This ensures that failed events are not lost and can be reviewed and addressed as needed.

## Deployment

This project uses AWS SAM (Serverless Application Model) for deployment.

### Prerequisites
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html) configured with appropriate credentials
- [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html)

### Deploy the Stack

To build and deploy the application to AWS, run:

```bash
sam build
sam deploy --guided
```

Or, to use the default parameters from `samconfig.toml`:

```bash
sam deploy
```

### Delete the Stack

To delete the deployed stack and all associated resources, run:

```bash
sam delete --stack-name patientcheckout
```

Replace `patientcheckout` with your actual stack name if different. Follow the prompts to confirm deletion.