package com.opom.bankingapp.dto.user;

import java.sql.Date;

public record MeResponse(
	    int id,
	    String accountNumber,
	    double balance,
	    String username,
	    String email,
	    int status,
	    String fullname,
	    Date dateOfBirth,
	    boolean isPolicyAgreement,
	    boolean isAutoSaveReceipt,
	    String nationality
	) {}