package com.app.ExamManager.DTO;

import com.app.ExamManager.model.eUserRole;

public record RegisterDTO(String login, String password, String dateBirth, eUserRole role) {
    
}
