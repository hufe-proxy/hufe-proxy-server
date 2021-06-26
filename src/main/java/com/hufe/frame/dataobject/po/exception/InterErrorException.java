package com.hufe.frame.dataobject.po.exception;

import lombok.Data;

@Data
public class InterErrorException extends RuntimeException {

    private Exception exception;

    public InterErrorException(Exception exception){
        this.exception = exception;
    }

    @Override
    public String toString() {
        return this.exception.toString();
    }
}
