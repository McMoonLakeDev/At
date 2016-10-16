package com.minecraft.moonlake.at;

import com.minecraft.moonlake.MoonLakePlugin;
import com.minecraft.moonlake.at.listeners.PlayerListener;
import com.minecraft.moonlake.at.manager.AtManager;
import com.minecraft.moonlake.economy.EconomyPlugin;
import com.minecraft.moonlake.economy.EconomyType;
import com.minecraft.moonlake.economy.api.EconomyManager;
import com.minecraft.moonlake.economy.api.MoonLakeEconomy;
import com.minecraft.moonlake.logger.MLogger;
import com.minecraft.moonlake.logger.MLoggerWrapped;
import com.minecraft.moonlake.util.StringUtil;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by MoonLake on 2016/10/10.
 */
public class AtPlugin extends JavaPlugin {

    private String prefix;
    private boolean economyEnable;
    private EconomyManager economy;
    private EconomyType economyType;
    private final MLogger mLogger;

    public AtPlugin() {
        this.mLogger = new MLoggerWrapped("MoonLakeAt");
    }

    @Override
    public void onEnable() {
        if(!setupMoonLake()) {
            this.getMLogger().error("月色之湖前置核心插件加载失败.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(!setupMoonLakeEconomy()) {
            this.getMLogger().error("月色之湖前置经济插件加载失败.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        AtManager.setMain(this);

        this.initFolder();
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getMLogger().log("月色之湖 At 插件 v" + getDescription().getVersion() + " 成功加载.");
    }

    @Override
    public void onDisable() {
    }

    private void initFolder() {
        if(!getDataFolder().exists())
            getDataFolder().mkdirs();
        File config = new File(getDataFolder(), "config.yml");
        if(!config.exists())
            saveDefaultConfig();
        this.prefix = StringUtil.toColor(getConfig().getString("Prefix", "&3MoonLake &7>> &f"));
        this.economyType = EconomyType.fromType(getConfig().getString("Economy.Type", "money"));
        this.economyEnable = getConfig().getBoolean("Economy.Enable", true);
    }

    private boolean setupMoonLake() {
        Plugin plugin = getServer().getPluginManager().getPlugin("MoonLake");
        return plugin != null && plugin instanceof MoonLakePlugin;
    }

    private boolean setupMoonLakeEconomy() {
        Plugin plugin = getServer().getPluginManager().getPlugin("MoonLakeEconomy");
        return plugin != null && plugin instanceof MoonLakeEconomy && (this.economy = ((EconomyPlugin) plugin).getManager()) != null;
    }

    public MLogger getMLogger() {
        return mLogger;
    }

    public EconomyType getEconomyType() {
        return economyType;
    }

    public boolean isEconomy() {
        return economyEnable;
    }

    public EconomyManager getEconomy() {
        return economy;
    }

    public String getMessage(String key, Object... args) {
        return StringUtil.toColor(StringUtil.format(prefix + getConfig().getString("Messages." + key, ""), args));
    }
}
