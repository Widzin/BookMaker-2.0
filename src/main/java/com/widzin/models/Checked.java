package com.widzin.models;

import java.util.List;

public class Checked {

	private List<Integer> checkedGames;

    public Checked() {
    }

    public Checked(List<Integer> checkedGames) {
        this.checkedGames = checkedGames;
    }

    public List<Integer> getCheckedGames() {
		return checkedGames;
	}

	public void setCheckedGames(List<Integer> checkedGames) {
		this.checkedGames = checkedGames;
	}
}
