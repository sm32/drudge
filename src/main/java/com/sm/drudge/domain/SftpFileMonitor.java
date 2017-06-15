package com.sm.drudge.domain;

import java.text.SimpleDateFormat;

/**
 * Created by Sreekanth Mahesala on 11/7/16.
 */

public class SftpFileMonitor implements DrudgeDomain {

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public Long getMonitorFrequencySeconds() {
        return monitorFrequencySeconds;
    }

    public String getDirectory() {
        return directory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String filePart;
    private String fileAction;
    private String user;
    private String password;
    private String host;
    private Long monitorFrequencySeconds;
    private String directory;
    private String created;
    private String fileName;
    private String monitorQueue;
    private String forwardQueue;
    private String localDirectory;

    public SftpFileMonitor(String filePart, String fileAction, String host, String user, String password, String directory, Long monitorFrequencySeconds, String monitorQueue, String forwardQueue, String localDirectory) {

        this.fileAction = fileAction;
        this.filePart = filePart;
        this.host = host;
        this.user = user;
        this.password = password;
        this.directory = directory;
        this.monitorFrequencySeconds = monitorFrequencySeconds;
        this.monitorQueue = monitorQueue;
        this.forwardQueue = forwardQueue;
        this.localDirectory = localDirectory;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd::HH:mm:ss");
        this.created = sdf.format(System.currentTimeMillis());

    }

    @Override
    public String created() {
        return created;
    }

    @Override
    public String toString() {
        return "SftpMonitor [host="+host+", user="+user+", password="+password+", fileaction="+fileAction+", filepart="+filePart+", monitorseconds="+ monitorFrequencySeconds +"]";
    }

    @Override
    public String forwardQueue() {
        return this.monitorQueue;
    }

    @Override
    public String monitorQueue() {
        return this.forwardQueue;
    }

    @Override
    public String fileAction() {
        return this.fileAction;
    }

    @Override
    public String filePart() {
        return this.filePart;
    }

    @Override
    public Boolean fileActionCheck() {
        return this.fileAction != null;
    }

    @Override
    public Boolean filePartCheck() {
        return this.filePart != null;
    }
}
