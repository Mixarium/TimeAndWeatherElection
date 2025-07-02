package me.mixarium.timeandweatherelection.specificstatevotecommands;

import me.mixarium.timeandweatherelection.TimeAndWeatherElection;
import me.mixarium.timeandweatherelection.VoteCommand;
import me.mixarium.timeandweatherelection.votefunctionality.VoteTimeoutScheduler;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class DayCommand extends VoteCommand {
    public DayCommand(VoteTimeoutScheduler voteTimeoutScheduler) {
        super(TimeAndWeatherElection.getNamesOfCommands(0), voteTimeoutScheduler);
    }

    @Override
    protected boolean checkNecessity() {
        World world = Bukkit.getServer().getWorld("world");
        return world.getTime() >= 13000L - 20L * TimeAndWeatherElection.getSecondsTillTransition();
    }

    @Override
    public void doVoteAction() {
        World world = Bukkit.getServer().getWorld("world");
        world.setTime(0L);
    }

}
