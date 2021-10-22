package com.ecom.Handlers;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class ExceptionDetails {

    protected String title;
    protected int status;
    protected String details;
    protected String developerMessage;
    protected Date timestamp;
    
}
