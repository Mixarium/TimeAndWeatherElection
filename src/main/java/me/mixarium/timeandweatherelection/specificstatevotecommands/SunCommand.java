package me.mixarium.timeandweatherelection.specificstatevotecommands;

import me.mixarium.timeandweatherelection.TimeAndWeatherElection;
import me.mixarium.timeandweatherelection.VoteCommand;
import me.mixarium.timeandweatherelection.VoteTimeoutScheduler;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class SunCommand extends VoteCommand {
    public SunCommand(VoteTimeoutScheduler voteTimeoutScheduler) {
        super(TimeAndWeatherElection.getNamesOfCommands(3), voteTimeoutScheduler);
    }

    @Override
    protected boolean checkNecessity() {
        World world = Bukkit.getServer().getWorld("world");
        return world.hasStorm();
    }

    @Override
    protected void doVoteAction() {
        World world = Bukkit.getServer().getWorld("world");
        world.setThundering(false);
        world.setStorm(false);
    }

}
