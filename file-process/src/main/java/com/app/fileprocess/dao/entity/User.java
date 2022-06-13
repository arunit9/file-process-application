package com.app.fileprocess.dao.entity;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users",
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = "username")
	})
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinTable(	name = "user_statistics", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "statistic_id"))
	private Set<Statistic> statistics = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinTable(	name = "user_user_files", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "user_file_id"))
	private Set<UserFile> userFiles = new HashSet<>();

    public User() {
    }

    public User(String username, String password) {
      this.username = username;
      this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

	public Set<Statistic> getStatistics() {
		return statistics;
	}

	public void setStatistics(Set<Statistic> statistics) {
		this.statistics = statistics;
	}

	public Set<UserFile> getUserFiles() {
		return userFiles;
	}

	public void setUserFiles(Set<UserFile> userFiles) {
		this.userFiles = userFiles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, password, passwordConfirm, roles, statistics, userFiles, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id) && Objects.equals(password, other.password)
				&& Objects.equals(passwordConfirm, other.passwordConfirm) && Objects.equals(roles, other.roles)
				&& Objects.equals(statistics, other.statistics) && Objects.equals(userFiles, other.userFiles)
				&& Objects.equals(username, other.username);
	}
    
}
