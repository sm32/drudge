package com.sm.drudge.application;

//import org.apache.commons.httpclient.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Created by Sreekanth Mahesala on 11/23/16.
 */
public class FileWriteToHDFS {

    public static void main(String[] args) throws Exception {

        //Path localFile = new Path("/Users/mahsr001/Downloads/HawkeyeSFDC_TESTING.csv");

        //URI bi = new URI("hdfs://lhdpdn00p03.staples.com:8020/");
        Path dst_folder = new Path("/user/mahsr001/data");

        Configuration conf = new Configuration();
        conf.addResource(new Path("/Users/mahsr001/Downloads/core-site.xml"));
        conf.addResource(new Path("/Users/mahsr001/Downloads/hdfs-site.xml"));

        FileSystem fs = FileSystem.get(conf);

        FileStatus[] fileStatus = fs.listStatus(dst_folder);
        for(FileStatus status : fileStatus){
            System.out.println(status.getPath().toString());
        }

    }

}
