package com.sm.drudge.application;

/**
 * Created by Sreekanth Mahesala on 11/19/16.
 */
public class DrudgeApplication {

    public static void main(String args[]) throws Exception {
        new DrudgeCli(args).parseOptions();
    }
}
