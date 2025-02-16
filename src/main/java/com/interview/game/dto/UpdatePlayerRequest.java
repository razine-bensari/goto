package com.interview.game.dto;

import lombok.Getter;

@Getter
public class UpdatePlayerRequest {
	private String name;
	private String lastName;
	private String username;
	private String phoneNumber;
}
