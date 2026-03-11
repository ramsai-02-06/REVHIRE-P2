package com.revhire.revhirep2.dto;

public class SecurityQuestionDTO {

    private String email;
    private String securityQuestion;

    public SecurityQuestionDTO() {}

    public SecurityQuestionDTO(String email, String securityQuestion) {
        this.email = email;
        this.securityQuestion = securityQuestion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
}