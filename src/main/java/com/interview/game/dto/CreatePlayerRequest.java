package com.interview.game.dto;

import lombok.Getter;

@Getter
public class CreatePlayerRequest {
	private String name;
	private String lastName;
	private String email;
	private String username;
	private String phoneNumber;
}
