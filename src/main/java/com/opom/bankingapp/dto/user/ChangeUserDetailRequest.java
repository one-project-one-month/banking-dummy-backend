package com.opom.bankingapp.dto.user;

import java.sql.Date;

public record ChangeUserDetailRequest(
		String username,
	    String email,
	    String fullname,
	    Date dateOfBirth,
	    Long nationalityId
) {}
