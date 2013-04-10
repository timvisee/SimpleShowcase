package com.timvisee.simpleshowcase;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PlayerMode {

	private String p;
	private PlayerModeType mode = PlayerModeType.UNKNOWN;
	private int step = 1;
	
	PlayerMode(String p, PlayerModeType mode) {
		this.p = p;
		this.mode = mode;
		this.step = 1;
	}
	
	/**
	 * Get the name of the player
	 * @return the player name
	 */
	public String getPlayerName() {
		return this.p;
	}
	
	/**
	 * Get the player
	 * @param s the server
	 * @return the player
	 */
	public Player getPlayer(Server s) {
		return s.getPlayer(this.p);
	}
	
	/**
	 * Get the mode of a player
	 * @return the player mode
	 */
	public PlayerModeType getMode() {
		return this.mode;
	}
	
	/**
	 * Set the player mode
	 * @param mode the mode
	 */
	public void setMode(PlayerModeType mode) {
		setMode(mode, true);
	}
	
	/**
	 * Set the player mode
	 * @param mode the mode
	 * @param resetSteps true if you want to reset the steps
	 */
	public void setMode(PlayerModeType mode, boolean resetSteps) {
		this.mode = mode;
		
		// Reset the steps if needed
		if(resetSteps)
			this.step = 1;
	}
	
	/**
	 * Get the currents step
	 * @return the current step
	 */
	public int getCurrentStep() {
		return this.step;
	}
	
	/**
	 * Set the current step the player is in
	 * @param s the step
	 */
	public void setCurrentStep(int s) {
		// The step has to be 1 or higher
		if(s >= 1)
			this.step = s;
	}
	
	/**
	 * Reset the current step. Basically put it back to 1
	 */
	public void resetCurrentStep() {
		this.step = 1;
	}
}
