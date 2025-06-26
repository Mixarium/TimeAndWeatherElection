package me.mixarium.timeandweatherelection;

import me.mixarium.timeandweatherelection.util.log.LogUtil;
import me.mixarium.timeandweatherelection.util.misc.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class VoteCommand implements CommandExecutor {

    private final String name;
    private final String successMessage;
    private final String unnecessaryVoteMessage;
    private final String alreadyVotedMessage;
    private final VoteTimeoutScheduler voteTimeoutScheduler;
    private final String voteTimeoutMessage;


    // percentage to success, divided by 100

    public VoteCommand(String name, VoteTimeoutScheduler voteTimeoutScheduler) {
        this.name = name;
        this.successMessage = "&2Vote for &6" + name + "&2 was successful.";
        this.unnecessaryVoteMessage = "&cIt is unnecessary to vote for &6" + name + "&c.";
        this.alreadyVotedMessage = "&cYou have already voted for &6" + name + "&c.";
        this.voteTimeoutMessage = "&cVote for &6" + name + " &ctimed out.";
        this.voteTimeoutScheduler = voteTimeoutScheduler;
    }

    /* future help message
    public VoteCommand() {
        this("tawe");
    }
     */

    // refer to the specificvotecommands package because these methods vary by instance
    protected boolean checkNecessity() {return false;}
    protected void doVoteAction() {}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            LogUtil.logConsoleInfo("[TimeAndWeatherElection] You must be in-game to run this command.");
            return false;
        }

        Player player = (Player) sender;

        if (!checkNecessity()) {
            player.sendMessage(ColorUtil.translateColorCodes(unnecessaryVoteMessage));
            return false;
        }

        // this is where the vote is trying to be added
        List<Player> voteList = VoteLists.getVoteList(name);

        if (voteList.contains(player)) {
            player.sendMessage(ColorUtil.translateColorCodes(alreadyVotedMessage));
            return false;
        } else {
            voteList.add(player);
        }

        if (voteList.size() == 1 && !voteTimeoutScheduler.isScheduled(name)) {
            voteTimeoutScheduler.waitTillEnd(name, () -> {
                voteList.clear();
                Bukkit.getServer().broadcastMessage(ColorUtil.translateColorCodes(voteTimeoutMessage));
            });
        }
        double playersCount = Bukkit.getServer().getOnlinePlayers().length;
        double playersRequired = Math.ceil(TimeAndWeatherElection.DECIMAL_TO_SUCCESS * playersCount);
        int voteCount = voteList.size();

        Server server = Bukkit.getServer();

        if (voteCount == (int) playersRequired) {
            voteTimeoutScheduler.cancel(name);
            doVoteAction();
            voteList.clear();
            server.broadcastMessage(ColorUtil.translateColorCodes(successMessage));
        } else {
            final String rawProgressMessage = "&9%s/%s &evotes for &6%s&e. &f/%s &eto pass the vote.";

            final String processedProgressMessage = String.format(
                    rawProgressMessage, voteCount, (int) playersRequired, name, name);
            server.broadcastMessage(ColorUtil.translateColorCodes(processedProgressMessage));
        }

        return true;
    }
}
