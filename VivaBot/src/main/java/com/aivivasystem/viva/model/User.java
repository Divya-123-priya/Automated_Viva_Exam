package com.aivivasystem.viva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "register_number")
    private String registerNumber;

    @Email @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    public User() {}

    public Long getId() { 
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
    public void setEmail(String e) { 
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