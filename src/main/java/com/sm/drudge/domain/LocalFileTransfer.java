package com.sm.drudge.domain;

import java.text.SimpleDateFormat;

/**
 * Created by Sreekanth Mahesala on 11/14/16.
 */
public class LocalFileTransfer implements DrudgeDomain {

    private final String created;
    private final String monitorQueue;
    private final String forwardQueue;
    private final String workingDirectory;
    private String fileName;
    private final String filePart;
    private final String fileAction;

    public LocalFileTransfer(String monitorQueue, String forwardQueue, String workingDirectory, String fileAction, String filePart) {

        this.monitorQueue = monitorQueue;
        this.forwardQueue = forwardQueue;
        this.workingDirectory = workingDirectory;
        this.filePart = filePart;
        this.fileAction = fileAction;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd::HH:mm:ss");
        this.created = sdf.format(System.currentTimeMillis());
    }

    @Override
    public String forwardQueue() {
        return forwardQueue;
    }

    @Override
    public String monitorQueue() {
        return monitorQueue;
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
    public String created() {
        return created;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "LocalFileTransfer [monitorQueue="+monitorQueue+", forwardQueue="+forwardQueue+", workingDirectory="+workingDirectory+", filePart="+filePart+", fileAction="+fileAction+"]";
    }

    public Boolean filePartCheck() {
        return filePart != null;
    }

    public Boolean fileActionCheck() {
        return fileAction != null;
    }
}
