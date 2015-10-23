package io.github.cvronmin.uhc;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CornUHC extends JavaPlugin{
	YamlConfiguration config, pvpRecord, pvpArena;
	PvpRecord record;
	PvpArena arena;
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new EventHandler(), this);
		this.getCommand("requestpvp").setExecutor(new EventHandler.CommandHandler(this));
		this.getCommand("pvpyes").setExecutor(new EventHandler.CommandHandler(this));
		this.getCommand("pvpno").setExecutor(new EventHandler.CommandHandler(this));
		this.getCommand("parenaset").setExecutor(new EventHandler.CommandHandler(this));
		this.getCommand("parenado").setExecutor(new EventHandler.CommandHandler(this));
		getLogger().info("trying to load config");
		loadConfig();
		getLogger().info("trying to load pvp record");
		loadPvpRecord();
		getLogger().info("trying to load pvp arena");
		loadPvpArena();
		List<World> worlds = Bukkit.getWorlds();
		for (int i = 0; i < worlds.size(); i++) {
			World world = worlds.get(i);
			getLogger().info("World:" + world.getName());
		}
		getLogger().info("plugin for corn uhc load successfully");
	}
	
	public void onDisable() {
	}
	public void loadConfig(){
		File f = new File(this.getDataFolder(), "config.yml");
		if (f.exists()) {
			config = YamlConfiguration.loadConfiguration(f);
			getLogger().info("config load successfully");
		} else {
			getLogger().info("config not found, creating");
			this.saveDefaultConfig();
			config = YamlConfiguration.loadConfiguration(f);
			getLogger().info("config load successfully");
		}
	}
	public void loadPvpRecord(){
		File f = new File(this.getDataFolder(), "pvprecord.yml");
		if (f.exists()) {
			pvpRecord = YamlConfiguration.loadConfiguration(f);
			getLogger().info("pvp record load successfully");
		} else {
			getLogger().info("pvp record not found, creating");
			this.saveResource("pvprecord.yml", false);
			pvpRecord = YamlConfiguration.loadConfiguration(f);
			getLogger().info("pvp record load successfully");
		}
		getLogger().info("trying to create pvprecord instance");
		record = new PvpRecord(this);
		getLogger().info("pvprecord instance created successfully");
		return;
	}
	public void loadPvpArena(){
		File f = new File(this.getDataFolder(), "pvparena.yml");
		if (f.exists()) {
			pvpArena = YamlConfiguration.loadConfiguration(f);
			getLogger().info("pvp arena load successfully");
		} else {
			getLogger().info("pvp arena not found, creating");
			this.saveResource("pvparena.yml", false);
			pvpArena = YamlConfiguration.loadConfiguration(f);
			getLogger().info("pvp arena load successfully");
		}
		getLogger().info("trying to create pvparena instance");
		arena = new PvpArena(this);
		getLogger().info("pvparena instance created successfully");
	}
}
