package com.opom.bankingapp.repository;

import com.opom.bankingapp.dto.common.OptionDto;
import java.util.List;

public interface CommonRepository {
    List<OptionDto> getGenderOptions();
    List<OptionDto> getNationalityOptions();
}
