package me.mixarium.timeandweatherelection;

import me.mixarium.timeandweatherelection.specificstatevotecommands.*;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import me.mixarium.timeandweatherelection.util.log.LogUtil;
import org.bukkit.event.Event.Type;

import java.util.HashMap;


public class TimeAndWeatherElection extends JavaPlugin {

    protected static final String[] namesOfCommands = {"day", "night", "rain", "sun", "thunder"};
    public static String getNamesOfCommands(int idx) {return namesOfCommands[idx];}

    public static double DECIMAL_TO_SUCCESS = 0.51D;
    public static long SECONDS_TILL_TIMEOUT = 90L;

    private final static HashMap<String , VoteCommand> voteCommandHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        LogUtil.logConsoleInfo(String.format("[%s] v%s Enabled.", getDescription().getName(), getDescription().getVersion()));

        VoteTimeoutScheduler voteTimeoutScheduler = new VoteTimeoutScheduler(this);

        voteCommandHashMap.put(namesOfCommands[0], new DayCommand(voteTimeoutScheduler));
        voteCommandHashMap.put(namesOfCommands[1], new NightCommand(voteTimeoutScheduler));
        voteCommandHashMap.put(namesOfCommands[2], new RainCommand(voteTimeoutScheduler));
        voteCommandHashMap.put(namesOfCommands[3], new SunCommand(voteTimeoutScheduler));
        voteCommandHashMap.put(namesOfCommands[4], new ThunderCommand(voteTimeoutScheduler));

        for (int i = 0; i <= 4; i++) {
            String cmdName = namesOfCommands[i];
            this.getCommand(cmdName).setExecutor(voteCommandHashMap.get(cmdName));
        }

        this.getServer().getPluginManager().registerEvent(Type.PLAYER_QUIT, new VoteListener(voteTimeoutScheduler), Event.Priority.Low, this);
        this.getServer().getPluginManager().registerEvent(Type.PLAYER_KICK, new VoteListener(voteTimeoutScheduler), Event.Priority.Low, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LogUtil.logConsoleInfo(String.format("[%s] v%s Disabled.", getDescription().getName(), getDescription().getVersion()));
    }

    public static VoteCommand getVoteCommandHashMapEntry(String name) {
        return voteCommandHashMap.get(name);
    }
}