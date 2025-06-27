package me.mixarium.timeandweatherelection;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoteLists {
    private static final List<Player> dayList = new ArrayList<>();
    private static final List<Player> nightList = new ArrayList<>();
    private static final List<Player> rainList = new ArrayList<>();
    private static final List<Player> sunList = new ArrayList<>();
    private static final List<Player> thunderList = new ArrayList<>();

    private static final HashMap<String, List<Player>> nameCommandToVoteList = new HashMap<>();
    static {
        nameCommandToVoteList.put(TimeAndWeatherElection.getNamesOfCommands(0), VoteLists.dayList);
        nameCommandToVoteList.put(TimeAndWeatherElection.getNamesOfCommands(1), VoteLists.nightList);
        nameCommandToVoteList.put(TimeAndWeatherElection.getNamesOfCommands(2), VoteLists.rainList);
        nameCommandToVoteList.put(TimeAndWeatherElection.getNamesOfCommands(3), VoteLists.sunList);
        nameCommandToVoteList.put(TimeAndWeatherElection.getNamesOfCommands(4), VoteLists.thunderList);
    }

    public static List<Player> getVoteList(String key) {
        return nameCommandToVoteList.get(key);
    }

    public static void removeVoteOnLeave(Player player) {
        dayList.removeIf(p -> p.equals(player));
        nightList.removeIf(p -> p.equals(player));
        rainList.removeIf(p -> p.equals(player));
        sunList.removeIf(p -> p.equals(player));
        thunderList.removeIf(p -> p.equals(player));
    }
}
