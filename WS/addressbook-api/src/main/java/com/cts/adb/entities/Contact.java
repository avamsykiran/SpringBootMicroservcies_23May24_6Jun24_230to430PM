package com.cts.adb.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="contacts")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long contactId;
	
	@NotNull(message = "Full Name is a mandatory field")
	@NotBlank(message = "Full Name is a mandatory field")
	@Size(min = 5,max = 50,message = "Full Naem is expected to be between 5 and 50 chars in length")
	private String fullName;
	
	@NotNull(message = "Mobile Number is a mandatory field")
	@NotBlank(message = "Mobile Number is a mandatory field")
	@Pattern(regexp = "[1-9][0-9]{9}",message = "Mobile Number is expected to be a ten digit number")
	private String mobileNumber;
	
	@NotNull(message = "Mail Id is a mandatory field")
	@NotBlank(message = "Mail Id is a mandatory field")
	@Email(message = "A valid email id is expected")
	private String mailId;
	
	@DateTimeFormat(iso = ISO.DATE)
	@Past(message = "DoB can not be a future date")
	private LocalDate dateOfBirth;
}
