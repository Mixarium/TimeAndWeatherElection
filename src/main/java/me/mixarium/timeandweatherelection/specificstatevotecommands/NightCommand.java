package me.mixarium.timeandweatherelection.specificstatevotecommands;

import me.mixarium.timeandweatherelection.TimeAndWeatherElection;
import me.mixarium.timeandweatherelection.VoteCommand;
import me.mixarium.timeandweatherelection.VoteTimeoutScheduler;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class NightCommand extends VoteCommand {
    public NightCommand(VoteTimeoutScheduler voteTimeoutScheduler) {
        super(TimeAndWeatherElection.getNamesOfCommands(1), voteTimeoutScheduler);
    }

    @Override
    protected boolean checkNecessity() {
        World world = Bukkit.getServer().getWorld("world");
        return world.getTime() < 13000L;
    }

    @Override
    protected void doVoteAction() {
        World world = Bukkit.getServer().getWorld("world");
        world.setTime(13000L);
    }

}
