package com.the9grounds.aeadditions.api;


public interface IHandlerStorageBase {

    boolean isFormatted();

    int totalBytes();

    int totalTypes();

    int usedBytes();

    int usedTypes();

    long storedCount();

}
