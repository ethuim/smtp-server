# SMTP Client

Sends a test email.

## Run

Use bat file or run command line:

`java -jar %cd%\target\send-email-jar-with-dependencies.jar`

> Defaults to localhost at port 26



## JSON file configuration

Client looks for configuration file named `smtp-client.config.json` and uses it if available.

``` json
{
    "mail.smtp.host":"localhost",
    "mail.smtp.port":"26",
    "smtpclient.email.to":"local-test@mail.com",
    "smtpclient.email.from":"local-client@mail.com",
    "smtpclient.email.subject":"Test email.",
    "smtpclient.email.text":"This is a test email.\nThank you."
}
```

## command line arguments

Command line arguments will override json file configuration.

`Usage: java -jar sendEmail.jar [-h ][-c <config file> ][-p <port> ][-s <servername> ]`

- `-h` display usage message
- `-c` configuration file name (smtp-client.config.json)
- `-p` port number (26)
- `-s` servername (localhost)
