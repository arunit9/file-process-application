package com.app.fileprocess.dao.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class UserFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(nullable=false)
	private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
	private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(	name = "user_file_statistics", 
				joinColumns = @JoinColumn(name = "user_file_id"), 
				inverseJoinColumns = @JoinColumn(name = "statistic_id"))
	private Set<Statistic> statistics = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Statistic> getStatistics() {
		return statistics;
	}

	public void setStatistics(Set<Statistic> statistics) {
		this.statistics = statistics;
	}

}
