package me.mixarium.timeandweatherelection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class VoteTimeoutScheduler {
    private final Plugin plugin;

    private final Map<String, Integer> taskIDs = new HashMap<>();

    public VoteTimeoutScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    public void waitTillEnd(String cmdName, Runnable onTimeout) {
        cancel(cmdName);

        int taskID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            taskIDs.remove(cmdName);
            onTimeout.run();
        }, 20L * TimeAndWeatherElection.SECONDS_TILL_TIMEOUT);

        taskIDs.put(cmdName, taskID);
    }

    public void cancel(String cmdName) {
        Integer taskID = taskIDs.remove(cmdName);
        if (taskID != null) {
            plugin.getServer().getScheduler().cancelTask(taskID);
        }
    }

    public boolean isScheduled(String cmdName) {
        return taskIDs.containsKey(cmdName);
    }
}
