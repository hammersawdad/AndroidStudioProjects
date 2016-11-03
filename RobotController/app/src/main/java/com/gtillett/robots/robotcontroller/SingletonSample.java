package com.gtillett.robots.robotcontroller;

/**
 * Created by gtillett on 2/16/2016.
 */
public class SingletonSample {

    // Declare class as a SINGLETON
    private static SingletonSample mInstance = null;

    public static SingletonSample getInstance(){
        if(mInstance == null)
        {
            mInstance = new SingletonSample();
        }
        return mInstance;
    }

    // Members
    private String mString;

    private SingletonSample(){
        mString = "Hello";
    }

    public String getString(){
        return this.mString;
    }

    public void setString(String value){
        mString = value;
    }
}
