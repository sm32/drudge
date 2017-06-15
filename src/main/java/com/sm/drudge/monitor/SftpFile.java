package com.sm.drudge.monitor;

import com.github.drapostolos.rdp4j.spi.FileElement;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import java.io.IOException;


public class SftpFile implements FileElement {
    private final  LsEntry file;


    public SftpFile(LsEntry file) {
        this.file = file;
    }


    public long lastModified() throws IOException {
    	return file.getAttrs().getMTime();
    }

    public boolean isDirectory() {
        return false;
    }


    public String getName() {
        return file.getFilename();
    }

    //  @Override
    public String toString() {
        return getName();
    }


}