package com.sm.drudge.move;

import com.sm.drudge.amqp.RabbitMqConfiguration;
import com.sm.drudge.amqp.RabbitMqMessageListener;
import com.sm.drudge.domain.HdfsFileTransfer;
import com.sm.drudge.domain.LocalFileTransfer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

/**
 * Created by Sreekanth Mahesala on 11/15/16.
 */
public class TransferToHdfs  extends TransferConfiguration implements RabbitMqMessageListener {

    private final Logger logger = LoggerFactory.getLogger(TransferToHdfs.class);

    public TransferToHdfs(HdfsFileTransfer hdfsFileTransfer, Class drudgeDomainClass)  throws IOException, TimeoutException {
        this.transferSettings = hdfsFileTransfer;
        this.drudgeDomainClass = drudgeDomainClass;
        rabbitMqConfiguration = new RabbitMqConfiguration(this.transferSettings);
    }

    @Override
    public void processTransfer() {

        if(drudgeDomainClass.equals(LocalFileTransfer.class)) {
            logger.info("Transfer to HDFS initiated");

            LocalFileTransfer localFileTransfer = (LocalFileTransfer) convertToObject(LocalFileTransfer.class);
            HdfsFileTransfer hdfsFileTransfer = (HdfsFileTransfer) transferSettings;

            hdfsFileTransfer.setFileName(localFileTransfer.getFileName());


            Path srcPath = new Path(Paths.get(localFileTransfer.getWorkingDirectory(),localFileTransfer.getFileName()).toString()); // path of file on local system
            Path destPath = new Path(Paths.get(hdfsFileTransfer.getWorkingDirectory(),localFileTransfer.getFileName()).toString()); //path of file on hdfs

            Configuration configuration = new Configuration();
            configuration.addResource(new Path(hdfsFileTransfer.getCoreSite()));
            configuration.addResource(new Path(hdfsFileTransfer.getHdfsSite()));
            //configuration.set("fs.defaultFS", hdfsFileTransfer.getURL());

            logger.info("Copying file local@"+destPath+" to "+hdfsFileTransfer.getURL()+"@"+destPath);

            try {
                FileSystem fileSystem = FileSystem.get(configuration);

                fileSystem.setVerifyChecksum(false);
                fileSystem.setWriteChecksum(false);
                fileSystem.copyFromLocalFile(srcPath,destPath);
                fileSystem.close();
            } catch (IOException e) {
                logger.error("HDFS Error",e);
            }

            logger.info("Transfer to HDFS completed");
        }

    }

    @Override
    public void rabbitMqReceiveMessages() throws IOException, TimeoutException {
        this.rabbitMqReceiveMessages(this);
    }
}
