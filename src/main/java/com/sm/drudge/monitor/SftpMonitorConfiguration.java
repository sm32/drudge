package com.sm.drudge.monitor;

import com.github.drapostolos.rdp4j.DirectoryPoller;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;
import com.sm.drudge.domain.SftpFileMonitor;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sreekanth Mahesala on 11/9/16.
 */

public class SftpMonitorConfiguration {

    private final SftpFileMonitor sftpFileMonitor;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(SftpMonitorConfiguration.class);

    public SftpMonitorConfiguration(SftpFileMonitor sftpFileMonitor) {
        this.sftpFileMonitor = sftpFileMonitor;
    }

    private SftpOperations sftpOperations() {
        return new SftpOperations(this.sftpFileMonitor);
    }


    private SftpListener sftpListener() {
        return new SftpListener(this.sftpFileMonitor);
    }

    public void monitor(){
        PolledDirectory polledDirectory = sftpOperations();
        DirectoryPoller dp = DirectoryPoller.newBuilder()
                .addPolledDirectory(polledDirectory)
                .addListener(sftpListener())
                .enableFileAddedEventsForInitialContent()
                .setPollingInterval(sftpFileMonitor.getMonitorFrequencySeconds(), TimeUnit.SECONDS)
                .start();
        logger.info("[x] Monitoring @ "+ sftpFileMonitor.toString());
    }

    public Boolean copyToLocal(String localDirectory) {
        SftpOperations sftpOperations = sftpOperations();
        return sftpOperations.copyToLocal(localDirectory);
    }
}
