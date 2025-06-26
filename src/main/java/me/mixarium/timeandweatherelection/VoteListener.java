package me.mixarium.timeandweatherelection;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VoteListener extends PlayerListener {

    private static TimeAndWeatherElection plugin;
    public VoteListener(TimeAndWeatherElection instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        VoteLists.removeVoteOnLeave(event.getPlayer());
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        VoteLists.removeVoteOnLeave(event.getPlayer());
    }

}
