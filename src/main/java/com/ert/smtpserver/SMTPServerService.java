package com.ert.smtpserver;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Eric Liang
 */
@Service
@Slf4j
public class SMTPServerService {

    @Value("${smtpserver.enabled}")
    String enabled = "";

    @Value("${smtpserver.hostName}")
    String hostName = "";

    @Value("${smtpserver.port}")
    String port = "";

    @Value("${smtpserver.directory}")
    String directory = "";

    SMTPServer smtpServer;

    public SMTPServerService() {
    }

    @PostConstruct
    public void start() {
        if (enabled.equalsIgnoreCase("true")) {
            SimpleMessageListenerImpl l = new SimpleMessageListenerImpl(directory);
            smtpServer = new SMTPServer(new SimpleMessageListenerAdapter(l));
            smtpServer.setHostName(this.hostName);
            smtpServer.setPort(Integer.valueOf(port));
            smtpServer.start();
            log.info("****** SMTP Server is running for domain " + smtpServer.getHostName() 
                    + " on port " + smtpServer.getPort() 
                    + " writing to directory " + directory);
        } else {
            log.info("****** SMTP Server NOT ENABLED by settings ");
        }
    }

    @PreDestroy
    public void stop() {
        if (enabled.equalsIgnoreCase("true")) {
            log.info("****** Stopping SMTP Server for domain " + smtpServer.getHostName() + " on port "
                    + smtpServer.getPort());
            smtpServer.stop();
        }
    }

    public boolean isRunning() {
        return smtpServer.isRunning();
    }
}
