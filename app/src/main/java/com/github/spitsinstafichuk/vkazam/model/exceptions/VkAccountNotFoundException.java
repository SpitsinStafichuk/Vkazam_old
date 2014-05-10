package com.github.spitsinstafichuk.vkazam.model.exceptions;

public class VkAccountNotFoundException extends Exception{

    public VkAccountNotFoundException(String message) {
        super(message);
    }

    public VkAccountNotFoundException() {
        super();
    }
}
