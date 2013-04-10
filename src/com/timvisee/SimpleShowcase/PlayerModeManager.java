package com.timvisee.simpleshowcase;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerModeManager {
	
	public List<PlayerMode> modes = new ArrayList<PlayerMode>();

	/**
	 * Check if a player is in any mode
	 * @param p the player name
	 * @return false if the player isn't in any mode
	 */
	public boolean isInPlayerMode(Player p) {
		for(PlayerMode m : this.modes) {
			if(m.getPlayerName().equals(p.getName())) {
				return (!m.getMode().equals(PlayerModeType.NONE) && !m.getMode().equals(PlayerModeType.UNKNOWN));
			}
		}
		return false;
	}
	
	/**
	 * Get the current mode type of a player
	 * @param p the player name
	 * @return the player mode. Returns None if no player mode has been selected
	 */
	public PlayerModeType getPlayerMode(Player p) {
		for(PlayerMode m : this.modes) {
			if(m.getPlayerName().equals(p.getName()))
				// Get the mode of the player
				return m.getMode();
		}
		// The player wasn't on the modes list jet, return NONE
		return PlayerModeType.NONE;
	}
	
	/**
	 * Put the player into any mode
	 * @param p the player name
	 * @param mode the player mode
	 */
	public void setPlayerMode(Player p, PlayerModeType mode, boolean showModeChangeMessages) {
		for(PlayerMode m : this.modes) {
			if(m.getPlayerName().equals(p.getName())) {
				if(!m.getMode().equals(mode)) {
					if(!m.getMode().equals(PlayerModeType.NONE) && !m.getMode().equals(PlayerModeType.UNKNOWN))
						if(showModeChangeMessages)
							p.sendMessage(ChatColor.GOLD + m.getMode().getName() + ChatColor.YELLOW + " mode has been " + ChatColor.DARK_RED + "Disabled");
					m.setMode(mode);
					if(showModeChangeMessages)
						p.sendMessage(ChatColor.GOLD + mode.getName() + ChatColor.YELLOW + " mode has been " + ChatColor.GREEN + "Enabled");
						
				}
				return;
			}
		}
		
		// The current player isn't in the player mode list yet, add it now
		this.modes.add(new PlayerMode(p.getName(), mode));
		if(showModeChangeMessages)
			p.sendMessage(ChatColor.GOLD + mode.getName() + ChatColor.YELLOW + " mode has been " + ChatColor.GREEN + "Enabled");
	}
	
	/**
	 * Get any player out of any player mode
	 * @param p the player name
	 */
	public void resetPlayerMode(Player p, boolean showModeChangeMessages) {
		if(isInPlayerMode(p)) {
			for(PlayerMode m : this.modes) {
				if(m.getPlayerName().equals(p.getName())) {
					if(!m.getMode().equals(PlayerModeType.NONE) && !m.getMode().equals(PlayerModeType.UNKNOWN))
						if(showModeChangeMessages)
							p.sendMessage(ChatColor.GOLD + m.getMode().getName() + ChatColor.YELLOW + " mode has been " + ChatColor.DARK_RED + "Disabled");
					this.modes.remove(m);
					return;
				}
			}
		}
	}
	
	/**
	 * Get the current player mode step
	 * @param p the player name
	 * @return the current step. -1 if the player isn't in the modes list yet
	 */
	public int getPlayerModeStep(Player p) {
		if(isInPlayerMode(p)) {
			for(PlayerMode m : this.modes) {
				if(m.getPlayerName().equals(p.getName()))
					// Get the current mode step of the player
					return m.getCurrentStep();
			}
		}
		return -1;
	}
	
	/**
	 * Set the current player mode step
	 * @param p the player name
	 * @param s the step
	 */
	public void setPlayerModeStep(Player p, int s) {
		if(isInPlayerMode(p)) {
			for(PlayerMode m : this.modes) {
				if(m.getPlayerName().equals(p.getName()))
					// Set the mode step of the curent player
					m.setCurrentStep(s);
			}
		} else {
			// The player isn't in the modes list yet, add it now
			PlayerMode pm = new PlayerMode(p.getName(), PlayerModeType.NONE);
			pm.setCurrentStep(s);
			this.modes.add(pm);
		}
	}
}
