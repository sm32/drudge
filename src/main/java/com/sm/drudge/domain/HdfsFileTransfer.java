package com.sm.drudge.domain;

import java.text.SimpleDateFormat;

/**
 * Created by Sreekanth Mahesala on 11/17/16.
 */
public class HdfsFileTransfer implements DrudgeDomain {

    private final String monitorQueue;
    private final String forwardQueue;
    private final String created;
    private final String server;
    private final String workingDirectory;
    private final String fileAction;
    private final String filePart;

    public String getCoreSite() {
        return coreSite;
    }

    public String getHdfsSite() {
        return hdfsSite;
    }

    private final String coreSite;
    private final String hdfsSite;

    public String getHdfsUser() {
        return hdfsUser;
    }

    private final String hdfsUser;
    private String fileName;

    public HdfsFileTransfer(String hdfsUser, String monitorQueue, String forwardQueue, String server, String workingDirectory, String fileAction, String filePart, String coreSite, String hdfsSite) {
        this.hdfsUser = hdfsUser;
        this.monitorQueue = monitorQueue;
        this.forwardQueue = forwardQueue;
        this.server = server;
        this.workingDirectory = workingDirectory;
        this.fileAction = fileAction;
        this.filePart = filePart;
        this.coreSite = coreSite;
        this.hdfsSite = hdfsSite;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd::HH:mm:ss");
        this.created = sdf.format(System.currentTimeMillis());
    }


    public String getURL() {
        return "hdfs://"+server;
    }

    public String getServer() {
        return server;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public String forwardQueue() {
        return this.forwardQueue;
    }

    @Override
    public String created() {
        return this.created;
    }

    @Override
    public String monitorQueue() {
        return this.monitorQueue;
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
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Boolean filePartCheck() {
        return this.filePart != null;
    }

    @Override
    public String toString() {
        return  "HdfsFileTransfer [monitorQueue="+monitorQueue+", forwardQueue="+forwardQueue+", workingDirectory="+workingDirectory+", server="+getURL()+", filePart="+filePart+", fileAction="+fileAction+"]";
    }

}
