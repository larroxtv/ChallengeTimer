package dev.larrox.challengeTimer.utils;

import dev.larrox.challengeTimer.ChallengeTimer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {

    private boolean isRunning = false;
    private int time = 0;
    private boolean isTimerHidden = false;

    public Timer() {
        ChallengeTimer plugin = ChallengeTimer.getInstance();
        if (plugin != null && plugin.getConfiguration() != null) {
            this.time = plugin.getConfiguration().config.getInt("timer.time");
        }
        runTimerTask();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isTimerHidden() {
        return isTimerHidden;
    }

    public void setTimerHidden(boolean timerHidden) {
        isTimerHidden = timerHidden;
    }

    public void sendActionBar() {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (!isRunning) {
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        new TextComponent(ChatColor.GRAY + "Der " + ChatColor.GREEN + "Timer §7ist" + ChatColor.RED + " pausiert§7.")
                );
                continue;
            }

            int hours = time / 3600;
            int minutes = (time % 3600) / 60;
            int seconds = time % 60;

            String timeString = String.format("%02dh %02dm %02ds", hours, minutes, seconds);

            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + timeString)
            );
        }
    }

    public void removeActionbar() {
        for (var player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
        }
        isTimerHidden = true;
    }

    private void runTimerTask() {
        ChallengeTimer plugin = ChallengeTimer.getInstance();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isTimerHidden) {
                    sendActionBar();
                }

                if (isRunning) {
                    time += 1;
                }
            }
        }.runTaskTimer((org.bukkit.plugin.Plugin) plugin, 20L, 20L);
    }

    public void save() {
        ChallengeTimer plugin = ChallengeTimer.getInstance();
        if (plugin != null && plugin.getConfiguration() != null) {
            plugin.getConfiguration().config.set("timer.time", time);
            plugin.getConfiguration().save();
        }
    }
}
