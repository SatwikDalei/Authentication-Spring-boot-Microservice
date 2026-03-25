package com.financetrackerapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserDto {
	private int id;
	private String firstName;
	private String lastName;
	@NotEmpty(message="Username cannot be empty")
	private String username;
	@NotEmpty(message="password cannot be empty")
	private String password;
	@Email
	private String email;
	public UserDto() {
		super();
	}
	public UserDto(int id, String firstName, String lastName,
			@NotEmpty(message = "Username cannot be empty") @NotNull(message = "Username connot be null") String username,
			@NotEmpty(message = "Username cannot be empty") @NotNull(message = "Username connot be null") String password,
			@Email String email) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	
	
}
