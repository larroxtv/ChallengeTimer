package dev.larrox.challengeTimer.utils;

import dev.larrox.challengeTimer.ChallengeTimer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private final File file;
    public final YamlConfiguration config;

    public Config() {
        ChallengeTimer plugin = ChallengeTimer.getInstance();

        File dir;
        if (plugin != null) {
            dir = plugin.getDataFolder();
        } else {
            dir = new File("./plugins/ChallengeTimer/");
        }

        if (!dir.exists()) {
            boolean ok = dir.mkdirs();
            if (!ok && plugin != null) {
                plugin.getLogger().warning("Could not create data folder: " + dir.getAbsolutePath());
            }
        }

        this.file = new File(dir, "config.yml");

        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (!created && plugin != null) {
                    plugin.getLogger().warning("Could not create config file: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                if (plugin != null) {
                    plugin.getLogger().severe("Failed to create config file: " + e.getMessage());
                } else {
                    e.printStackTrace();
                }
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            ChallengeTimer plugin = ChallengeTimer.getInstance();
            if (plugin != null) {
                plugin.getLogger().severe("Failed to save config file: " + e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
    }

    public File getFile() {
        return file;
    }
}
