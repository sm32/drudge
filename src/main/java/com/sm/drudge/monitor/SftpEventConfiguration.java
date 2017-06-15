package com.sm.drudge.monitor;

import com.github.drapostolos.rdp4j.spi.FileElement;

/**
 * Created by Sreekanth Mahesala on 11/9/16.
 */


public interface SftpEventConfiguration {

    void EventProcessor(FileAction fileAction, FileElement file);
    enum FileAction {Added,Removed,Modified,Initialization}

}
