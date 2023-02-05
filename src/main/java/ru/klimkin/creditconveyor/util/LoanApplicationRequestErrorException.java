package ru.klimkin.creditconveyor.util;

public class LoanApplicationRequestErrorException extends RuntimeException {
    public LoanApplicationRequestErrorException(String msg){
        super(msg);
    }
}
