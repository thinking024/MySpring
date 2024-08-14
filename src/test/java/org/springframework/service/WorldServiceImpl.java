package org.springframework.service;

public class WorldServiceImpl implements WorldService {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void explode() {
		System.out.println("The Earth is going to explode");
		System.out.println("name: " + name);
	}

}