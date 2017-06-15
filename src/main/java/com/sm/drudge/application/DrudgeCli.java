package com.sm.drudge.application;

import com.sm.drudge.domain.HdfsFileTransfer;
import com.sm.drudge.domain.LocalFileTransfer;
import com.sm.drudge.domain.SftpFileMonitor;
import com.sm.drudge.monitor.SftpMonitorConfiguration;
import com.sm.drudge.move.TransferToHdfs;
import com.sm.drudge.move.TransferToLocal;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Sreekanth Mahesala on 11/19/16.
 */
public class DrudgeCli {

    private final Logger logger = LoggerFactory.getLogger(DrudgeCli.class);
    private String[] args = null;
    private Options options = new Options();
    private CommandLine commandLine;

    private final String transferOperation = "job";


    public DrudgeCli(String[] args) {

        this.args = args;

        //Mutually exclusive - Operation
        Option transferOption = Option.builder("t").hasArg(false).required().longOpt("transfer").desc("Transfer option").build();
        Option monitorOption = Option.builder("m").hasArg(false).required().longOpt("com/sm/drudge/monitor").desc("Monitor option").build();
        Option emailOption = Option.builder("e").hasArg(false).required().longOpt("email").desc("Email option").build();

        OptionGroup operationGroup = new OptionGroup();
        operationGroup.setRequired(true);
        operationGroup.addOption(transferOption);
        operationGroup.addOption(monitorOption);
        operationGroup.addOption(emailOption);

        options.addOptionGroup(operationGroup);

        //Mandatory option
        Option sourceOption = Option.builder("L").hasArg().required().longOpt("type").desc("Location: sftp,local,hdfs").build();
        Option monitorQueueOption = Option.builder("qm").hasArg().required().longOpt("monitor-queue").desc("Rabbitmq queue to monitor").build();
        Option forwardQueueOption = Option.builder("qf").hasArg().required().longOpt("forward-queue").desc("Rabbitmq queue to forward").build();
        Option workDirOption = Option.builder("sd").required().hasArg().longOpt("source-directory").desc("Source directory to monitor | Source directory for transfer").build();

        //Optional parameters
        Option helpOption = Option.builder("h").hasArg(false).required(false).longOpt("help").desc("Show help").build();

        //Common parameters
        options.addOption(sourceOption);
        options.addOption(monitorQueueOption);
        options.addOption(forwardQueueOption);
        options.addOption(helpOption);

        //Specific Optional parameters
        Option filePartOption = Option.builder("fp").hasArg().required(false).longOpt("file-part").desc("File part").build();
        Option fileActionOption = Option.builder("fa").hasArg().required(false).longOpt("file-action").desc("File action- Added, Removed, Modified").build();
        Option userOption = Option.builder("u").required(false).hasArg().longOpt("user").desc("Username").build();
        Option passwordOption = Option.builder("p").required(false).hasArg().longOpt("password").desc("Password").build();
        Option serverOption = Option.builder("s").required(false).hasArg().longOpt("server").desc("Server").build();
        Option djuOption = Option.builder("dju").required(false).hasArg().longOpt("db-jdbc-url").desc("JDBC DB Url").build();
        Option dcnOption = Option.builder("dcn").required(false).hasArg().longOpt("db-class-name").desc("JDBC driver class name").build();
        Option localDirOption = Option.builder("ld").required(false).hasArg().longOpt("local-directory").desc("Local working directory").build();
        Option monitorFreqOption = Option.builder("mf").required(false).hasArg().longOpt("monitor-frequency").desc("Monitoring frequency in seconds").build();
        Option transferSftpFileOption = Option.builder("T").required(false).hasArg(false).longOpt("monitor-transfer").desc("Monitor with file transfer").build();
        Option coreSiteHdfsOption = Option.builder("hcs").required(false).hasArg().longOpt("core-site").desc("core-site.xml file").build();
        Option hdfsSiteHdfsOption = Option.builder("hhs").required(false).hasArg().longOpt("hdfs-site").desc("hdfs-site.xml file").build();

        options.addOption(filePartOption);
        options.addOption(fileActionOption);
        options.addOption(workDirOption);
        options.addOption(userOption);
        options.addOption(passwordOption);
        options.addOption(serverOption);
        options.addOption(djuOption);
        options.addOption(dcnOption);
        options.addOption(localDirOption);
        options.addOption(transferSftpFileOption);
        options.addOption(monitorFreqOption);
        options.addOption(coreSiteHdfsOption);
        options.addOption(hdfsSiteHdfsOption);

    }


    public void parseOptions() throws IOException, TimeoutException {

        CommandLineParser parser = new DefaultParser();

        try {

            commandLine = parser.parse(options,args);

            if(commandLine.hasOption("m")) {
                logger.info("Monitor");
                monitor();
            } else if (commandLine.hasOption("t")) {
                logger.info("Transfer");
                transfer();
            }

        } catch (ParseException e) {
            logger.error("Failed to parse command line parameters",e);
            help(null);
        }

    }

    private void monitor() {

        //SFTP Monitoring
        if(getOptionValue("L").equalsIgnoreCase("sftp")) {
            logger.info("Sftp");
            String localDir = null;
            if (commandLine.hasOption("T")) {
                if (commandLine.hasOption("ld")) {
                    localDir = getOptionValue("ld");
                } else {
                    logger.error("Monitor with file transfer error");
                    help("ld");
                }
            }

            final SftpFileMonitor sftpFileMonitor = new SftpFileMonitor(getOptionValue("fp"), getOptionValue("fa"), getOptionValue("s"), getOptionValue("u"), getOptionValue("p"), getOptionValue("sd"), Long.parseLong(getOptionValue("mf")), getOptionValue("qm"),getOptionValue("qf"), localDir);

            SftpMonitorConfiguration sftpMonitorConfiguration = new SftpMonitorConfiguration(sftpFileMonitor);
            sftpMonitorConfiguration.monitor();
        } else if (getOptionValue("L").equalsIgnoreCase("local")) {
            logger.info("Local");
        } else {
            logger.info(getOptionValue("L") + " is not a valid location for com.sm.drudge.monitor");
            help("L");
        }
    }


    private void transfer() throws IOException, TimeoutException {

        if(getOptionValue("L").equalsIgnoreCase("hdfs")) {
            logger.info("Hdfs");
            HdfsFileTransfer hdfsFileTransfer = new HdfsFileTransfer(getOptionValue("u"),getOptionValue("qm"),getOptionValue("qf"),getOptionValue("s"),getOptionValue("sd"),getOptionalValue("fa"),getOptionalValue("fp"), getOptionValue("hcs"), getOptionValue("hhs"));
            new TransferToHdfs(hdfsFileTransfer,LocalFileTransfer.class).rabbitMqReceiveMessages();
        } else if(getOptionValue("L").equalsIgnoreCase("local")) {
            logger.info("Local");
            LocalFileTransfer localFileTransfer = new LocalFileTransfer(getOptionValue("qm"), getOptionValue("qf"), getOptionValue("sd"), getOptionalValue("fa"),getOptionalValue("fp"));
            new TransferToLocal(localFileTransfer, SftpFileMonitor.class).rabbitMqReceiveMessages();
        }

    }


    private String getOptionalValue(String optionChar) {
        if(!commandLine.hasOption(optionChar)) {
            return null;
        }
        return commandLine.getOptionValue(optionChar);
    }

    private String getOptionValue(String optionChar) {
        if(!commandLine.hasOption(optionChar)) {
            help(optionChar);
        }

        return commandLine.getOptionValue(optionChar);
    }

    private void help(String optionChar) {
        if(optionChar != null) {
            logger.error("Missing option/args '"+optionChar+"'");
        }
        String header = "Drudge client uses RabbitMq for transfer, monitor and emailing options";
        String footer = "Please report issues at sreekanth.mahesala@gmail.com";
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.setLeftPadding(5);
        formatter.setOptionComparator(null);
        formatter.printHelp("Drudge",header,options,footer,true);
        System.exit(0);
    }
}
