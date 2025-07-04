package me.mixarium.timeandweatherelection.votefunctionality;

import me.mixarium.timeandweatherelection.TimeAndWeatherElection;
import me.mixarium.timeandweatherelection.VoteCommand;
import me.mixarium.timeandweatherelection.util.misc.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class VoteListener extends PlayerListener {

    private final VoteTimeoutScheduler voteTimeoutScheduler;
    public VoteListener(VoteTimeoutScheduler voteTimeoutScheduler) {
        this.voteTimeoutScheduler = voteTimeoutScheduler;
    }

    public void evaluateOnLeave() {
        for (int i = 0; i <= 4; i++) {
            String cmdName = TimeAndWeatherElection.getNamesOfCommands(i);
            List<Player> evaluatedVoteList = VoteLists.getVoteList(cmdName);

            // bug fix, this was done to prevent the initiation of all votes when the only player on the server leaves
            if (evaluatedVoteList.isEmpty()) {continue;}

            Server server = Bukkit.getServer();
            double playersCount = server.getOnlinePlayers().length - 1;
            double coefficient = (double) TimeAndWeatherElection.getPercentageToSuccess() / 100D;
            double playersRequired = Math.ceil(coefficient * playersCount);
            int voteCount = evaluatedVoteList.size();

            if (voteCount >= playersRequired) {
                String successMessage = "&2Vote for &6" + cmdName + "&2 was successful.";
                voteTimeoutScheduler.cancel(cmdName);
                VoteCommand cmdInstance = TimeAndWeatherElection.getVoteCommandHashMapEntry(cmdName);
                cmdInstance.doVoteAction();
                evaluatedVoteList.clear();
                server.broadcastMessage(ColorUtil.translateColorCodes(successMessage));
            }

        }

    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        VoteLists.removeVoteOnLeave(event.getPlayer());
        evaluateOnLeave();
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        VoteLists.removeVoteOnLeave(event.getPlayer());
        evaluateOnLeave();
    }

}
