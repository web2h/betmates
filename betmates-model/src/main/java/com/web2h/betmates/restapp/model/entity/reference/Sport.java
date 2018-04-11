package com.web2h.betmates.restapp.model.entity.reference;

public enum Sport {

	BASKET_BALL("Basket-Ball"),
	FOOTBALL("Football"),
	HOCKEY("Hockey"),
	SOCCER("Soccer");

	private String value;

	private Sport(String value) {
		this.value = value;
	}

	public String getLogValue() {
		return value;
	}
}