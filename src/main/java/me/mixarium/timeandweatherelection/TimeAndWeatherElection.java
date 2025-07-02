package me.mixarium.timeandweatherelection;

import me.mixarium.timeandweatherelection.specificstatevotecommands.*;
import me.mixarium.timeandweatherelection.util.config.ConfigUtil;
import me.mixarium.timeandweatherelection.util.log.UpdateUtil;
import me.mixarium.timeandweatherelection.votefunctionality.VoteListener;
import me.mixarium.timeandweatherelection.votefunctionality.VoteTimeoutScheduler;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import me.mixarium.timeandweatherelection.util.log.LogUtil;
import org.bukkit.event.Event.Type;

import java.util.HashMap;

public class TimeAndWeatherElection extends JavaPlugin {

    protected static final String[] namesOfCommands = {"day", "night", "rain", "sun", "thunder"};
    public static String getNamesOfCommands(int idx) {return namesOfCommands[idx];}

    protected static final String helpCommand = "tawe";

    public static String getHelpCommand() {return helpCommand;}

    private static ConfigUtil config;

    private static int PERCENTAGE_TO_SUCCESS;
    private static long SECONDS_TILL_TIMEOUT;
    private static long SECONDS_TILL_TRANSITION;

    private final static HashMap<String , VoteCommand> voteCommandHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        UpdateUtil.checkAvailablePluginUpdates(this, "https://api.github.com/repos/Mixarium/TimeAndWeatherElection/releases/latest");
        LogUtil.logConsoleInfo(String.format("[%s] v%s Enabled.", getDescription().getName(), getDescription().getVersion()));

        VoteTimeoutScheduler voteTimeoutScheduler = new VoteTimeoutScheduler(this);

        config = new ConfigUtil(this, "tawe.yml");
        config.loadAndLog();
        PERCENTAGE_TO_SUCCESS = config.getInt("percentageToSuccess", 51);
        SECONDS_TILL_TIMEOUT = config.getInt("secondsTillTimeout", 90);
        SECONDS_TILL_TRANSITION = config.getInt("secondsTillTransition", 40);
        config.save();

        voteCommandHashMap.put(namesOfCommands[0], new DayCommand(voteTimeoutScheduler));
        voteCommandHashMap.put(namesOfCommands[1], new NightCommand(voteTimeoutScheduler));
        voteCommandHashMap.put(namesOfCommands[2], new RainCommand(voteTimeoutScheduler));
        voteCommandHashMap.put(namesOfCommands[3], new SunCommand(voteTimeoutScheduler));
        voteCommandHashMap.put(namesOfCommands[4], new ThunderCommand(voteTimeoutScheduler));

        this.getCommand(helpCommand).setExecutor(new HelpCommand(this));

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

    public static ConfigUtil getConfig() {
        return config;
    }

    public static int getPercentageToSuccess() {
        return PERCENTAGE_TO_SUCCESS;
    }

    public static void setPercentageToSuccess(int percentageToSuccess) {
        PERCENTAGE_TO_SUCCESS = percentageToSuccess;
    }

    public static long getSecondsTillTimeout() {
        return SECONDS_TILL_TIMEOUT;
    }

    public static void setSecondsTillTimeout(long secondsTillTimeout) {
        SECONDS_TILL_TIMEOUT = secondsTillTimeout;
    }

    public static long getSecondsTillTransition() {
        return SECONDS_TILL_TRANSITION;
    }

    public static void setSecondsTillTransition(long secondsTillTransition) {
        SECONDS_TILL_TRANSITION = secondsTillTransition;
    }

}