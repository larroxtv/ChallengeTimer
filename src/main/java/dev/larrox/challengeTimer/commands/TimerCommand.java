package dev.larrox.challengeTimer.commands;

import dev.larrox.challengeTimer.ChallengeTimer;
import dev.larrox.challengeTimer.utils.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TimerCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prfx = Prefix.PREFIX;
        var timer = ChallengeTimer.getInstance().getTimer();

        if (timer == null) {
            sender.sendMessage(ChatColor.RED + prfx + " Timer is not initialized.");
            return true;
        }

        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "resume":
                if (timer.isRunning()) {
                    sender.sendMessage(ChatColor.RED + prfx + " The timer is already running.");
                    return true;
                }
                timer.setRunning(true);
                String messageResume = (timer.getTime() != 0)
                        ? ChatColor.GRAY + prfx + " Timer resumed by §f§l" + sender.getName() + "§7."
                        : ChatColor.GRAY + prfx + " Timer started by §f§l" + sender.getName() + "§7.";
                Bukkit.broadcastMessage(messageResume);
                break;

            case "pause":
                if (!timer.isRunning()) {
                    sender.sendMessage(ChatColor.RED + prfx + " The timer is not running.");
                    return true;
                }
                timer.setRunning(false);
                timer.save();
                Bukkit.broadcastMessage(ChatColor.GRAY + prfx + " Timer paused by §f§l" + sender.getName() + "§7.");
                break;

            case "settime":
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.GRAY + prfx + " Usage" + ChatColor.DARK_GRAY + ": " +
                            "§6/timer §8settime §f<time>");
                    return true;
                }
                try {
                    int time = Integer.parseInt(args[1]);
                    timer.setRunning(false);
                    timer.setTime(time);
                    String msgTime = (time != 1)
                            ? ChatColor.GRAY + prfx + " Timer set to §6§l" + time + "§f seconds by §f§l" + sender.getName() + "."
                            : ChatColor.GRAY + prfx + " Timer set to §6§l" + time + "§f second by §f§l" + sender.getName() + ".";
                    Bukkit.broadcastMessage(msgTime);
                    timer.setRunning(true);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + prfx + " Your second parameter must be a number.");
                }
                break;

            case "reset":
                timer.setRunning(false);
                timer.setTime(0);
                Bukkit.broadcastMessage(ChatColor.GRAY + prfx + " Timer reset by §f§l" + sender.getName() + "§7.");
                break;

            case "hide":
                if (timer.isRunning()) {
                    sender.sendMessage(prfx + " Timer cannot be hidden while running.");
                    return true;
                }
                if (timer.isTimerHidden()) {
                    sender.sendMessage(prfx + " Timer is already hidden.");
                } else {
                    timer.removeActionbar();
                    timer.setTimerHidden(true);
                    Bukkit.broadcastMessage(ChatColor.GRAY + prfx + " Timer hidden by §f§l" + sender.getName() + "§7.");
                }
                break;

            case "show":
                if (!timer.isTimerHidden()) {
                    sender.sendMessage(prfx + " Timer is already visible.");
                } else {
                    timer.setTimerHidden(false);
                    timer.sendActionBar();
                    Bukkit.broadcastMessage(ChatColor.GRAY + prfx + " Timer shown by §f§l" + sender.getName() + "§7.");
                }
                break;

            case "help":
            default:
                sendUsage(sender);
                break;
        }

        return true;
    }

    private void sendUsage(CommandSender sender) {
        String prfx = Prefix.PREFIX;
        sender.sendMessage(ChatColor.GRAY + prfx + " Usage" + ChatColor.DARK_GRAY + ": " + ChatColor.GOLD +
                "\n/timer §8resume §7- Resume/Start the timer" +
                "\n/timer §8pause §7- Pause the timer" +
                "\n/timer §8settime §f<seconds> §7- Set the timer to a specific time" +
                "\n/timer §8reset §7- Reset the timer" +
                "\n/timer §8show §7- Show the timer" +
                "\n/timer §8hide §7- Hide the timer" +
                "\n/timer §8help §7- Show this help menu");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(Arrays.asList("resume", "pause", "settime", "reset", "hide", "show", "help"));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("settime")) {
            completions.addAll(Arrays.asList("120", "60", "§cTime in seconds"));
        }
        String lastArg = args[args.length - 1];
        completions.removeIf(s -> !s.toLowerCase().startsWith(lastArg.toLowerCase()));
        return completions;
    }
}
