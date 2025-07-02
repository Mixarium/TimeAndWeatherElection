package me.mixarium.timeandweatherelection.specificstatevotecommands;

import me.mixarium.timeandweatherelection.TimeAndWeatherElection;
import me.mixarium.timeandweatherelection.VoteCommand;
import me.mixarium.timeandweatherelection.votefunctionality.VoteTimeoutScheduler;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ThunderCommand extends VoteCommand {
    public ThunderCommand(VoteTimeoutScheduler voteTimeoutScheduler) {
        super(TimeAndWeatherElection.getNamesOfCommands(4), voteTimeoutScheduler);
    }

    @Override
    protected boolean checkNecessity() {
        World world = Bukkit.getServer().getWorld("world");
        return !world.isThundering();
    }

    @Override
    public void doVoteAction() {
        World world = Bukkit.getServer().getWorld("world");
        world.setStorm(true);
        world.setThundering(true);
        world.setThunderDuration(world.getWeatherDuration());
    }

}
