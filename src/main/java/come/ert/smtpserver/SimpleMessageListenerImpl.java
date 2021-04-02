package come.ert.smtpserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.subethamail.smtp.helper.SimpleMessageListener;

/**
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
            log.info("To: {}", recipient);
            log.info("From: {}", from);
            String subject = m.getSubject();
            
            log.info("Subject: {}", subject);

            String body = new String(m.getRawInputStream().readAllBytes());
            log.info("Content: {}", body);

            log.info("received mail directory: {}", directory);
            String fileName = new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date()) + ".eml";

            log.info("save as file: {}", fileName);
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
            Logger.getLogger(SimpleMessageListenerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimpleMessageListenerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
