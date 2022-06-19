# Email Webservice
This is a Java SpringBoot based webservice to send email using Mailgun and Sendgrid, which is designed to be extensible to add new email service providers if required.  
The project uses reactive programming methodology using SpringBoot Webflux. The reactive programming was preferred as the application only forwards the incoming requests
to the email provider endpoints, without performing any CPU intensive operations.

## Key Decisions and Assumptions
1. The application imposes feature constraints based on the endpoints serviced.  
For example -  
    SendGrid requires all the senders to be verified, before using it to send email. This is however, not a constraint for MailGun.
2. Support for only plain text email body
3. No dynamic substitutions or templating for email body

## Setup and Installation

To install on Windows/Linux machine, refer section [Windows or Linux](#windows-or-linux). To use Docker, refer section [Docker](#docker).

### Windows or Linux

**Requirements** - JDK11+

Steps:
1. Clone the repository, or download and extract the zip.
2. Navigate to the home directory
3. Configure the `application-dev.properties` or `application-prod.properties`, in the directory `src/main/resources`, and set the required properties.
4. Set the Spring Boot profile in `application.properties` under `src/main/resources`
5. Run below command to build the project and generate JAR-
```
On Windows
.\mvnw_cmd

On Linux
 ./mvnw install
```
4. This will download all the dependencies and generate the JAR in `target` directory
5. Run the jar using command -
```
java -jar ./target/emailservice-1.0-SNAPSHOT.jar
```
### Docker
Configure the application properties in the file `.env.list`. Each environment variable is mapped to a corresponding property in the profile `application-docker.properties` 

```
docker build -t emailservice .
```

```
docker run -d -p 8080:8080 -e JAVA_OPTS="-Xms256m -Xmx512m" --env-file .env.list emailservice
```

## Usage
To invoke the API using the service, invoke below cURL request

### Example CURL
```curl
curl --location --request POST 'http://localhost:8080/v1/email/send' \
--header 'Content-Type: application/json' \
--data-raw '{

    "to": ["xxxx@gmail.com"],
    "cc": ["xxxxx@yahoo.com"],
    "bcc":["xxxx@gmail.com]
    "emailSubject": "This is a really cool email service",
    "emailBody": "The email service automatically switches the email service endpoint in case of a failover",
    "senderName": "Harsh Sanjanwala"
}'
```
### Example Response
**202 ACCEPTED**
```json
{
    "status": "Email Send request accepted successfully"
}
```
### Request Body Properties
| Name         | Required | Type          | Details                                                                                                                                                                                       |
|--------------|----------|---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| to           | required | Array[String] | Comma separated list of unique email addresses.<br/>**Note:** The email list should be unique across *to*, *cc*, and *bcc*. The count of addresses across all the lists must not exceed 1000. |
| cc           | optional | Array[String] | Comma separated list of unique email addresses.                                                                                                                                               |
| bcc          | optional | Array[String] | Comma separated list of unique email addresses.                                                                                                                                               |
| emailSubject | required | String        | Subject of the Email                                                                                                                                                                          |
| emailBody    | required | String        | Email body - only plain text format supported                                                                                                                                                 |
| senderName   | optional | String        | Name of a Sender                                                                                                                                                                              |

### Responses

| Code | Details                                                                                                                     |
|------|-----------------------------------------------------------------------------------------------------------------------------|
| 202  | Returned when message request is queued successfully with any email service endpoint                                        |
| 400  | Returned when the request body fails validation                                                                             |
| 500  | This is returned for any application errors, or if the email request cannot be sent using any of the email service endpoint |



## Roadmap
1. Add ability to schedule the message to be sent at later times.
2. Add user specific endpoint profiles, for providing ability to allow different sender email addresses for each user.

## TODO:
1. Add unit test cases for 100% coverage
2. Add more INFO and DEBUG logging
3. Host it on AWS or any other cloud service