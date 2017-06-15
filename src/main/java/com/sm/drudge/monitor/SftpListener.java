package com.sm.drudge.monitor;

import com.sm.drudge.amqp.RabbitMqConfiguration;
import com.github.drapostolos.rdp4j.*;
import com.github.drapostolos.rdp4j.spi.FileElement;
import com.sm.drudge.domain.SftpFileMonitor;


public class SftpListener implements DirectoryListener, IoErrorListener, InitialContentListener {

    private final SftpFileMonitor sftpFileMonitor;
    private boolean initialized;

    public SftpListener(SftpFileMonitor sftpFileMonitor) {
        this.sftpFileMonitor = sftpFileMonitor;
    }

    @Override
    public void fileAdded(FileAddedEvent event) {
        sendToRabbitMq(FileAction.Added, event.getFileElement());
    }

    @Override
    public void fileRemoved(FileRemovedEvent event) {
        sendToRabbitMq(FileAction.Removed,event.getFileElement());
    }

    @Override
    public void fileModified(FileModifiedEvent event) {
        sendToRabbitMq(FileAction.Modified, event.getFileElement());
    }

    @Override
    public void ioErrorCeased(IoErrorCeasedEvent event) {
        System.out.println("I/O error ceased.");
    }

    @Override
    public void ioErrorRaised(IoErrorRaisedEvent event) {
        System.out.println("I/O error raised!");
        event.getIoException().printStackTrace();
    }

    @Override
    public void initialContent(InitialContentEvent event) {
        this.initialized = true;
    }


    public enum FileAction {
        Added, Removed, Modified
    }

    public void sendToRabbitMq(FileAction fileAction, FileElement file) {
        if (this.initialized && (fileAction.toString().equals(sftpFileMonitor.fileAction()) && file.getName().contains(sftpFileMonitor.filePart()))) {
            try {

                sftpFileMonitor.setFileName(file.getName());
                RabbitMqConfiguration rabbitMqConfiguration = new RabbitMqConfiguration(sftpFileMonitor);
                rabbitMqConfiguration.convertAndSend();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}