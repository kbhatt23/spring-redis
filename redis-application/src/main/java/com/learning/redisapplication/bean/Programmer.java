package com.learning.redisapplication.bean;

import java.io.Serializable;
//if we are not setting value serialzer as jsonjackson serializer - > default object serializer is jdk one whihc needs implement serialzable
//all the strict implementation of serialzable will get introeduced using that henc ejackson is easier

public class Programmer /* implements Serializable */{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7470967568340216590L;
	private String name;
	private int age;
	private String company;
	
	public Programmer(String name, int age, String company) {
		this.name = name;
		this.age = age;
		this.company = company;
	}

	public Programmer() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Programmer [name=" + name + ", age=" + age + ", company=" + company + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Programmer other = (Programmer) obj;
		if (age != other.age)
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
