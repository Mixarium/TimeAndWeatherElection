package me.mixarium.timeandweatherelection.specificstatevotecommands;

import me.mixarium.timeandweatherelection.TimeAndWeatherElection;
import me.mixarium.timeandweatherelection.VoteCommand;
import me.mixarium.timeandweatherelection.VoteTimeoutScheduler;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class RainCommand extends VoteCommand {
    public RainCommand(VoteTimeoutScheduler voteTimeoutScheduler) {
        super(TimeAndWeatherElection.getNamesOfCommands(2), voteTimeoutScheduler);
    }

    @Override
    protected boolean checkNecessity() {
        World world = Bukkit.getServer().getWorld("world");
        boolean isRaining = world.hasStorm();
        return !(isRaining) || (isRaining && world.isThundering());
    }

    @Override
    protected void doVoteAction() {
        World world = Bukkit.getServer().getWorld("world");
        world.setStorm(true);
        world.setThundering(false);
    }

}
