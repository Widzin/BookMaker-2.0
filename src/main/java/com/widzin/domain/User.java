package com.widzin.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends AbstractDomainClass  {

    private String username;

    @Transient
    private String password;

    private String encryptedPassword;
    private Boolean enabled;
    private double moneyNow;
    private double winMoney;
    private double lostMoney;
    private double insertedMoney;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    // ~ defaults to @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "user_id"),
    //     inverseJoinColumns = @joinColumn(name = "role_id"))
    private List<Role> roles;
    private Integer failedLoginAttempts;
    private String mainRole;

	public User () {
		roles = new ArrayList<>();
		failedLoginAttempts = 0;
		enabled = true;
		moneyNow = 0;
		winMoney = 0;
		lostMoney = 0;
		insertedMoney = 0;
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

    public String getEncryptedPassword() {
    return encryptedPassword;
    }

	public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

	public double getMoneyNow () {
		return moneyNow;
	}

	public void setMoneyNow (double moneyNow) {
		this.moneyNow += moneyNow;
	}

	public double getWinMoney () {
		return winMoney;
	}

	public void setWinMoney (double winMoney) {
		this.winMoney += winMoney;
	}

	public double getLostMoney () {
		return lostMoney;
	}

	public void setLostMoney (double lostMoney) {
		this.lostMoney += lostMoney;
	}

	public double getInsertedMoney () {
		return insertedMoney;
	}

	public void setInsertedMoney (double insertedMoney) {
		this.insertedMoney += insertedMoney;
	}

	public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getMainRole() {return mainRole;	}

	public void setMainRole (String mainRole) {
		this.mainRole = mainRole;
	}

	public void addRole(Role role){
        if(!this.roles.contains(role)){
            this.roles.add(role);
        }

        if(!role.getUsers().contains(this)){
            role.getUsers().add(this);
        }
    }

    public void removeRole(Role role){
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }
}
