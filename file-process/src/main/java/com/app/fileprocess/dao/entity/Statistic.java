package com.app.fileprocess.dao.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Statistic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserFile userFile;

    @Column(nullable=false)
	private String name;

    @Column(nullable=false)
	private Long value;

    @ManyToOne(fetch = FetchType.LAZY)
	private User user;

    public Statistic() {
		super();
    }

	public Statistic(Long id, UserFile userFile, String name, Long value, User user) {
		super();
		this.id = id;
		this.userFile = userFile;
		this.name = name;
		this.value = value;
		this.user = user;
	}

	public Statistic(String name, Long value) {
		super();
		this.name = name;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserFile getUserFile() {
		return userFile;
	}

	public void setUserFile(UserFile userFile) {
		this.userFile = userFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
