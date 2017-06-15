package com.sm.drudge.move;

import com.sm.drudge.amqp.RabbitMqConfiguration;
import com.sm.drudge.amqp.RabbitMqMessageListener;
import com.sm.drudge.domain.LocalFileTransfer;
import com.sm.drudge.domain.SftpFileMonitor;
import com.sm.drudge.monitor.SftpMonitorConfiguration;
import org.slf4j.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Created by Sreekanth Mahesala on 11/14/16.
 */
public class TransferToLocal extends TransferConfiguration implements RabbitMqMessageListener {

    private final Logger logger = LoggerFactory.getLogger(TransferToLocal.class);

    public TransferToLocal(LocalFileTransfer localFileTransfer, Class drudgeDomainClass) throws IOException, TimeoutException {
        this.transferSettings = localFileTransfer;
        this.drudgeDomainClass = drudgeDomainClass;
        rabbitMqConfiguration = new RabbitMqConfiguration(this.transferSettings);

    }

    @Override
    public void rabbitMqReceiveMessages() throws IOException, TimeoutException {
        this.rabbitMqReceiveMessages(this);
    }

    @Override
    public void processTransfer() {

        if(drudgeDomainClass.equals(SftpFileMonitor.class)) {
            logger.info("Transfer to local initiated");
            SftpFileMonitor sftpFileMonitor = (SftpFileMonitor) convertToObject(SftpFileMonitor.class);

            if(filePartCheck(sftpFileMonitor.getFileName()) && fileActionCheck(sftpFileMonitor.fileAction())) {
                transferSettings.setFileName(sftpFileMonitor.getFileName());

                SftpMonitorConfiguration sftpMonitorConfiguration = new SftpMonitorConfiguration(sftpFileMonitor);

                if(sftpMonitorConfiguration.copyToLocal(((LocalFileTransfer)transferSettings).getWorkingDirectory())){
                    sendToRabbitMq();
                }

            }
            logger.info("Transfer process completed");
        }

    }

}
