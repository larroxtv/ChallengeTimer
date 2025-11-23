package dev.larrox.challengeTimer;

import dev.larrox.challengeTimer.commands.TimerCommand;
import dev.larrox.challengeTimer.utils.Config;
import dev.larrox.challengeTimer.utils.Timer;
import dev.larrox.challengeTimer.utils.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ChallengeTimer extends JavaPlugin {

    private Timer timer;
    private Config configuration;
    private final String prfx = Prefix.PREFIX;
    private static ChallengeTimer instance;

    public Timer getTimer() {
        return timer;
    }

    public Config getConfiguration() {
        return configuration;
    }

    public String getPrfx() {
        return prfx;
    }

    public static ChallengeTimer getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        configuration = new Config();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prfx + " §7Activated!");

        if (getCommand("timer") != null) getCommand("timer").setExecutor(new TimerCommand());

        timer = new Timer();
    }

    @Override
    public void onDisable() {
        if (timer != null) timer.save();
        if (configuration != null) configuration.save();

        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prfx + " §7Deactivated!");
    }
}
