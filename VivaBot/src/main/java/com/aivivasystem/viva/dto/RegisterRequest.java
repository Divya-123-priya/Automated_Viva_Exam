package com.aivivasystem.viva.dto;

public class RegisterRequest {
    private String fullName;
    private String registerNumber; 
    private String email;
    private String password;
    private String role;

    public String getFullName() { 
    	return fullName; }
    public void setFullName(String n){ 
    	this.fullName = n; }

    public String getRegisterNumber() { 
    	return registerNumber; }
    public void setRegisterNumber(String r) { 
    	this.registerNumber = r; }

    public String getEmail() { 
    	return email; }
    public void setEmail(String e){ 
    	this.email = e; }

    public String getPassword() { 
    	return password; }
    public void setPassword(String p) { 
    	this.password = p; }

    public String getRole() { 
    	return role; }
    public void setRole(String r) { 
    	this.role = r; }
    
}