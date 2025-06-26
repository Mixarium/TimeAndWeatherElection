package me.mixarium.timeandweatherelection;

import me.mixarium.timeandweatherelection.specificstatevotecommands.*;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import me.mixarium.timeandweatherelection.util.log.LogUtil;
import org.bukkit.event.Event.Type;


public class TimeAndWeatherElection extends JavaPlugin {

    protected static final String[] namesOfCommands = {"day", "night", "rain", "sun", "thunder"};
    public static String getNamesOfCommands(int idx) {return namesOfCommands[idx];}

    public static double DECIMAL_TO_SUCCESS = 0.51D;
    public static long SECONDS_TILL_TIMEOUT = 90L;

    @Override
    public void onEnable() {
        // Plugin startup logic
        LogUtil.logConsoleInfo(String.format("[%s] v%s Enabled.", getDescription().getName(), getDescription().getVersion()));

        VoteTimeoutScheduler voteTimeoutScheduler = new VoteTimeoutScheduler(this);

        this.getCommand(namesOfCommands[0]).setExecutor(new DayCommand(voteTimeoutScheduler));
        this.getCommand(namesOfCommands[1]).setExecutor(new NightCommand(voteTimeoutScheduler));
        this.getCommand(namesOfCommands[2]).setExecutor(new RainCommand(voteTimeoutScheduler));
        this.getCommand(namesOfCommands[3]).setExecutor(new SunCommand(voteTimeoutScheduler));
        this.getCommand(namesOfCommands[4]).setExecutor(new ThunderCommand(voteTimeoutScheduler));

        this.getServer().getPluginManager().registerEvent(Type.PLAYER_QUIT, new VoteListener(this), Event.Priority.Low, this);
        this.getServer().getPluginManager().registerEvent(Type.PLAYER_KICK, new VoteListener(this), Event.Priority.Low, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LogUtil.logConsoleInfo(String.format("[%s] v%s Disabled.", getDescription().getName(), getDescription().getVersion()));
    }

}