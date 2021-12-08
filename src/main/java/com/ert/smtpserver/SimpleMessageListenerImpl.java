package com.ert.smtpserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.subethamail.smtp.helper.SimpleMessageListener;

/**
 * Write the received email file to directory
 * 
 * @author Eric Liang
 */
@Slf4j
@Getter
@Setter
public class SimpleMessageListenerImpl implements SimpleMessageListener {

    public SimpleMessageListenerImpl() {
    }

    public SimpleMessageListenerImpl(String directory) {
        log.debug("Starting Message Listener and writing to directory: {}",directory);
        this.directory = directory;
    }

    private String directory;

    @Override
    public boolean accept(String from, String recipient) {
        return true;
    }

    @Override
    public void deliver(String from, String recipient, InputStream data) {
        try {
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage m = new MimeMessage(session, data);
            log.debug("To: {}", recipient);
            log.debug("From: {}", from);
            String subject = m.getSubject();
            
            log.debug("Subject: {}", subject);

            String body = new String(m.getRawInputStream().readAllBytes());
            log.debug("Content: {}", body);

            log.debug("received mail directory: {}", directory);
            String fileName = new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date()) + ".eml";

            log.debug("save as file: {}", fileName);
            Path writedirectory = Paths.get(directory);
            Path writepath = Paths.get(directory, fileName);
            if (java.nio.file.Files.isWritable(writedirectory)) {
                FileOutputStream outputStream = new FileOutputStream(writepath.toString());
                m.writeTo(outputStream);
                outputStream.close();
            } else {
                log.error("Path not writable: {}", writedirectory.toString());
            }

        } catch (MessagingException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
