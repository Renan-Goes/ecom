package com.ecom.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")
	@Column(name="USER_ID")
    private String userId;

    @Column(unique=true)
    private String email;
    private String password;
    @Column(unique=true)
    private String userName;    

    public User(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
    }
}
