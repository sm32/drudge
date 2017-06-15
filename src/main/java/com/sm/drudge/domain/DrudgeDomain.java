package com.sm.drudge.domain;

/**
 * Created by Sreekanth Mahesala on 11/14/16.
 */
public interface DrudgeDomain {

    public String forwardQueue();
    public String created();
    public String monitorQueue();
    public String fileAction();
    public String filePart();
    public Boolean fileActionCheck();
    public String getFileName();
    public void setFileName(String fileName);
    public Boolean filePartCheck();
    public String toString();

}
