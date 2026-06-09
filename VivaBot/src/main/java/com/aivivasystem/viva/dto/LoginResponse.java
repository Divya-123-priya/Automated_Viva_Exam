package com.aivivasystem.viva.dto;

public class LoginResponse {
    private Long   id;
    private String fullName;
    private String registerNumber;
    private String email;
    private String role;
    private String token;

    public Long getId()  { 
    	return id; }
    public void setId(Long id) { 
    	this.id = id; }

    public String getFullName() { 
    	return fullName; }
    public void setFullName(String n) { 
    	this.fullName = n; }

    public String getRegisterNumber() { 
    	return registerNumber; }
    public void setRegisterNumber(String r){ 
    	this.registerNumber = r; }

    public String getEmail() { 
    	return email; }
    public void setEmail(String e){ 
    	this.email = e; }

    public String getRole() { 
    	return role; }
    public void setRole(String r) { 
    	this.role = r; }

    public String getToken(){ 
    	return token; }
    public void setToken(String t){ 
    	this.token = t; }

}