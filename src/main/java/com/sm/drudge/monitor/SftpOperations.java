package com.sm.drudge.monitor;

import com.github.drapostolos.rdp4j.spi.FileElement;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;
import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.sm.drudge.domain.SftpFileMonitor;
import org.slf4j.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;


public class SftpOperations implements PolledDirectory {

    private final SftpFileMonitor sftpFileMonitor;
    private Session session;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(SftpOperations.class);

    SftpOperations(SftpFileMonitor sftpFileMonitor) {
        this.sftpFileMonitor = sftpFileMonitor;
    }

    private ChannelSftp channelSftp() throws JSchException {
        JSch jsch = new JSch();

        session = jsch.getSession(sftpFileMonitor.getUser(), sftpFileMonitor.getHost(), 22 );
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(sftpFileMonitor.getPassword());
        session.connect();

        Channel channel;
        channel = session.openChannel("sftp");
        channel.connect();

        return (ChannelSftp) channel;
    }


    @Override
    public Set<FileElement> listFiles() {
        try {
            ChannelSftp channelSftp = channelSftp();
            Set<FileElement> fileElements = new LinkedHashSet<FileElement>();

            Vector<LsEntry> filesList = channelSftp.ls(sftpFileMonitor.getDirectory());

            for(LsEntry file : filesList) {
                fileElements.add(new SftpFile(file));
            }

            channelSftp.exit();
            disconnectSession();

            return fileElements;
        } catch (Exception e) {
            logger.error("Sftp directory list operation failed", e);
            return null;
        }
    }

    private void disconnectSession() {
        session.disconnect();
    }

    public boolean copyToLocal(String localDirectory) {
        try {
            ChannelSftp channelSftp = channelSftp();
            Path path = Paths.get(sftpFileMonitor.getDirectory(), sftpFileMonitor.getFileName());

            logger.info("Copying to local directory "+path.toString());
            channelSftp.get(path.toString(),localDirectory);

            channelSftp.exit();
            disconnectSession();

            return true;
        } catch (Exception e) {
            logger.error("Sftp copy failed please see the error for more details",e);
            return false;
        }
    }
}