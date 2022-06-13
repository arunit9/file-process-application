package com.app.fileprocess.response;

import java.io.Serializable;

public class UserFileResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;

	private Long id;

	private String message;

	public UserFileResponse(Long id, String name) {
		super();
		this.name = name;
		this.id = id;
	}

	public UserFileResponse(String name, Long id, String message) {
		super();
		this.name = name;
		this.id = id;
		this.message = message;
	}

	public UserFileResponse(String message) {
		super();
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
