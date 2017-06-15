# Drudge Application

###Amqp based monitoring/transfer process

##Monitoring
1. Sftp, Local - the application can monitor for Added/Removed/Modified events
2. Based on the filepart/fileaction settings, appropriate message is forwarded to queue for further processing
drudge.jar -m -qm sftp-monitor -qf sftp-monitor -sd ftp/Directory -fp FilePart_ -fa Added -L sftp -u ftp_user -p XXXXX -s localhost -mf 1

##Local Transfer
1. Sftp to local - the application can transfer file using the information from monitoring queue
drudge.jar -t -L locaL -qm sftp-monitor -qf local-transfer -fa Added -fp FilePart_ -sd /Users/mahsr001/Downloads/

##Hdfs Transfer
1. local to HDFS - the application can transfer file using the information from monitoring queue
drudge.jar -t -L hdfs -u mahsr001 -qm local-transfer -qf null -s namenode:8020 -sd /user/dir -hcs core-site.xml -hhs hdfs-site.xml

##Options
     -t,--transfer                   Transfer option
     -e,--email                      Email option
     -m,--com/sm/drudge/monitor      Monitor option
     -L,--type <arg>                 Location: sftp,local,hdfs
     -qm,--monitor-queue <arg>       Rabbitmq queue to monitor
     -qf,--forward-queue <arg>       Rabbitmq queue to forward
     -h,--help                       Show help
     -fp,--file-part <arg>           File part
     -fa,--file-action <arg>         File action- Added, Removed, Modified
     -sd,--source-directory <arg>    Source directory to monitor | Source directory for transfer
     -u,--user <arg>                 Username
     -p,--password <arg>             Password
     -s,--server <arg>               Server
     -dju,--db-jdbc-url <arg>        JDBC DB Url
     -dcn,--db-class-name <arg>      JDBC driver class name
     -ld,--local-directory <arg>     Local working directory
     -T,--monitor-transfer           Monitor with file transfer
     -mf,--monitor-frequency <arg>   Monitoring frequency in seconds
     -hcs,--core-site <arg>          core-site.xml file
     -hhs,--hdfs-site <arg>          hdfs-site.xml file

Please report issues at smahesala@gmail.com
