package me.mixarium.timeandweatherelection;

import me.mixarium.timeandweatherelection.util.config.ConfigUtil;
import me.mixarium.timeandweatherelection.util.misc.AboutUtil;
import me.mixarium.timeandweatherelection.util.misc.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class HelpCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    public HelpCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            StringBuilder cmdListMessage = new StringBuilder("&3Vote commands available:&b");
            String percentageMessage = "&3Percentage required to pass vote: &b" + TimeAndWeatherElection.getPercentageToSuccess() + "%";
            String expirationMessage = "&3Seconds before vote session is expired: &b" + TimeAndWeatherElection.getSecondsTillTimeout();
            String transitionMessage = "&3In how many seconds one can vote before time transition: &b" + TimeAndWeatherElection.getSecondsTillTransition();

            for (int i = 0; i <= 4; i++) {
                cmdListMessage.append(" /").append(TimeAndWeatherElection.getNamesOfCommands(i));
                cmdListMessage.append((i != 4) ? "," : "");
            }
            sender.sendMessage(ColorUtil.translateColorCodes(String.valueOf(cmdListMessage)));
            sender.sendMessage(ColorUtil.translateColorCodes(percentageMessage));
            sender.sendMessage(ColorUtil.translateColorCodes(expirationMessage));
            sender.sendMessage(ColorUtil.translateColorCodes(transitionMessage));
            return true;
        }

        if (args[0].equalsIgnoreCase("about")) {
            AboutUtil.aboutPlugin(sender, plugin, null);
            return true;
        }

        if (args[0].equalsIgnoreCase("config")) {

            String helpCmdName = TimeAndWeatherElection.getHelpCommand();
            String percentageLine = String.format("&9/%s percentage [integer] - sets percentage of voters required", helpCmdName);
            String expirationLine = String.format("&9/%s expiration [integer] - sets time that evaluation of votes lasts for", helpCmdName);
            String transitionLine = String.format("&9/%s beforeTransition [integer] - sets time before day -> night or vice versa transition when players can vote", helpCmdName);

            sender.sendMessage(ColorUtil.translateColorCodes(percentageLine));
            sender.sendMessage(ColorUtil.translateColorCodes(expirationLine));
            sender.sendMessage(ColorUtil.translateColorCodes(transitionLine));
            return true;
        }

        boolean moreThanOneArgPassed = args.length > 1;
        String noOpPermsMessage = "&cYou do not have operator permissions!";
        ConfigUtil config = TimeAndWeatherElection.getConfig();

        if (moreThanOneArgPassed && args[0].equalsIgnoreCase("percentage")) {
            try {
                int percentage = Integer.parseInt(args[1]);
                if (!sender.isOp()) {
                    sender.sendMessage(ColorUtil.translateColorCodes(noOpPermsMessage));
                    return true;
                }
                if (percentage < 0 || percentage > 100) {
                    sender.sendMessage(ColorUtil.translateColorCodes("&cPercentage must be between 0 and 100."));
                    return true;
                }

                TimeAndWeatherElection.getConfig().setProperty("percentageToSuccess", percentage);
                sender.sendMessage(ColorUtil.translateColorCodes("&2Percentage changed successfully."));
                config.save();
                TimeAndWeatherElection.setPercentageToSuccess(percentage);
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage(ColorUtil.translateColorCodes("&cPercentage must be an integer ranging from 0 to 100."));
                return true;
            }
        }

        if (moreThanOneArgPassed && args[0].equalsIgnoreCase("expiration")) {
            try {
                int seconds = Integer.parseInt(args[1]);
                if (!sender.isOp()) {
                    sender.sendMessage(ColorUtil.translateColorCodes(noOpPermsMessage));
                    return true;
                }
                if (seconds < 30 || seconds > 300) {
                    sender.sendMessage(ColorUtil.translateColorCodes("&cExpiration time must be between 30 and 300 seconds."));
                    return true;
                }

                TimeAndWeatherElection.getConfig().setProperty("secondsTillTimeout", seconds);
                sender.sendMessage(ColorUtil.translateColorCodes("&2Expiration time changed successfully."));
                config.save();
                TimeAndWeatherElection.setSecondsTillTimeout(seconds);
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage(ColorUtil.translateColorCodes("&cExpiration time must be an integer ranging from 30 to 300 seconds."));
                return true;
            }
        }

        if (moreThanOneArgPassed && args[0].equalsIgnoreCase("beforeTransition")) {
            try {
                int seconds = Integer.parseInt(args[1]);
                if (!sender.isOp()) {
                    sender.sendMessage(ColorUtil.translateColorCodes(noOpPermsMessage));
                    return true;
                }
                if (seconds < 15 || seconds > 60) {
                    sender.sendMessage(ColorUtil.translateColorCodes("&cBeforeTransition time must be between 15 and 60 seconds."));
                    return true;
                }

                TimeAndWeatherElection.getConfig().setProperty("secondsTillTransition", seconds);
                sender.sendMessage(ColorUtil.translateColorCodes("&2BeforeTransition time changed successfully."));
                config.save();
                TimeAndWeatherElection.setSecondsTillTransition(seconds);
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage(ColorUtil.translateColorCodes("&cBeforeTransition time must be an integer ranging from 15 to 60 seconds."));
                return true;
            }
        }

        return false;
    }
}
