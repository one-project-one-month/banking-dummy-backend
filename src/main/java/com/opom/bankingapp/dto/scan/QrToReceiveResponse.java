package com.opom.bankingapp.dto.scan;

import com.opom.bankingapp.dto.transfer.ValidateTransferResponse;

public class QrToReceiveResponse extends ValidateTransferResponse {
    private double amount;
    private String note;

    public QrToReceiveResponse() {
    }

    public QrToReceiveResponse(ValidateTransferResponse validateTransferResponse, double amount, String note) {
        super(validateTransferResponse.getFromAccountDetails(), validateTransferResponse.getToAccountDetails());
        this.amount = amount;
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
