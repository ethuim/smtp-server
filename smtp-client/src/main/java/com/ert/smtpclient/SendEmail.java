package com.ert.smtpclient;

import com.sun.mail.smtp.SMTPTransport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Eric Liang
 */
public class SendEmail {
    private static final Logger log = LoggerFactory.getLogger(SendEmail.class);
    
    private static String SMTP_SERVER = "localhost";
    private static String SMTP_PORT = "26";
    private static String USERNAME = "";
    private static String PASSWORD = "";

    private static String EMAIL_FROM = "test-sender@test-mail.com";
    private static String EMAIL_TO = "test-recipient@test-mail.com";
    private static final String EMAIL_TO_CC = "";

    private static String EMAIL_SUBJECT = "Test email via SMTP client";
    private static String EMAIL_TEXT = "This is a test email.";

    private static String CONFIG_PATH = "smtp-client.config.json";

    public static void main(String... args) throws IOException {
        String argPort = "";
        String argServer = "";
        String argConfigPath = "";

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                log.info("Argument " + i + ": " + args[i]);
                if (i < args.length - 1) {
                    if ("-c".equals(args[i])) {
                        log.info("Config file: " + args[i + 1]);
                        Path path = Paths.get(args[i + 1]);
                        log.info("Config File: " + path.toString());
                        argConfigPath = path.toString();
                    } else if ("-p".equals(args[i])) {
                        argPort = args[i + 1];
                    } else if ("-s".equals(args[i])) {
                        argServer = args[i + 1];
                    } else if ("-h".equals(args[i])) {
                        log.info("Usage: java -jar sendEmail.jar [-h ][-c <smtp-client.config.json> ][-p <port> ][-s <servername> ]");
                        log.info("-h display usage");
                        log.info("-c json config file (smtp-client.config.json)");
                        log.info("-p port number (26)");
                        log.info("-s server name (localhost)");
                        return;
                    }
                }
            }
        }

        //Read config file
        String port = "";
        String server = "";

        try {
            log.info("Look for config file:" + CONFIG_PATH);
            Path path = Paths.get(CONFIG_PATH);

            JsonReader reader = Json.createReader(new FileInputStream(path.toString()));
            JsonObject configobj = reader.readObject();

            String tmp = "";
            try {
                tmp = configobj.getString("mail.smtp.port");
                if (!tmp.isBlank()) {
                    port = tmp;
                }
            } catch (NullPointerException e) {
            }
            try {
                tmp = configobj.getString("mail.smtp.host");
                if (!tmp.isBlank()) {
                    server = tmp;
                }
            } catch (NullPointerException e) {
            }
            try {
                tmp = configobj.getString("smtpclient.email.to");
                if (!tmp.isBlank()) {
                    EMAIL_TO = tmp;
                }
            } catch (NullPointerException e) {
            }

            try {
                tmp = configobj.getString("smtpclient.email.from");
                if (!tmp.isBlank()) {
                    EMAIL_FROM = tmp;
                }
            } catch (NullPointerException e) {
            }
            try {
                tmp = configobj.getString("smtpclient.email.subject");
                if (!tmp.isBlank()) {
                    EMAIL_SUBJECT = tmp;
                }
            } catch (NullPointerException e) {
            }

            try {
                tmp = configobj.getString("smtpclient.email.text");
                if (!tmp.isBlank()) {
                    EMAIL_TEXT = tmp;
                }
            } catch (NullPointerException e) {
            }

            try {
                tmp = configobj.getString("smtpclient.username");
                if (!tmp.isBlank()) {
                    USERNAME = tmp;
                }
            } catch (NullPointerException e) {
            }
            try {
                tmp = configobj.getString("smtpclient.password");
                if (!tmp.isBlank()) {
                    PASSWORD = tmp;
                }
            } catch (NullPointerException e) {
            }
        } catch (FileNotFoundException ex) {
            log.info("Config file not found.");
        }

        SMTP_PORT = argPort.isBlank() ? port.isBlank() ? SMTP_PORT : port : argPort;
        SMTP_SERVER = argServer.isBlank() ? server.isBlank() ? SMTP_SERVER : server : argServer;

        log.info("Sending a test email to server: " + SMTP_SERVER + " port: " + SMTP_PORT);

        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.put("mail.smtp.port", SMTP_PORT); // default port 25

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {

            // from
            msg.setFrom(new InternetAddress(EMAIL_FROM));

            // to 
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));

            // cc
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(EMAIL_TO_CC, false));

            // subject
            msg.setSubject(EMAIL_SUBJECT);

            // content 
            msg.setText(EMAIL_TEXT);

            msg.setSentDate(new Date());

            // Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

            // connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);

            // send
            t.sendMessage(msg, msg.getAllRecipients());

            log.info("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
