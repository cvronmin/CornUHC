package io.github.cvronmin.uhc;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PvpRecord {
	private final CornUHC pluginMain;
	private String pvptime;
	private int time;
	public PvpRecord(CornUHC main){
		pluginMain = main;
		getPvpedTime(pluginMain.pvpRecord);
	}
	public void getPvpedTime(YamlConfiguration yml){
		for (int i = 0; i < 0xFFFF; ++i) {
			if ((yml.isSet("pvp" + (i - 1)) || (i - 1) == 0) && !yml.isSet("pvp" + i)) {
				pvptime = "pvp" + (i - 1);
				time = i - 1;
			}
			else {
				continue;
			}
		}
		if(pvptime == null | pvptime.equals("") | time <= 0){
			pluginMain.getLogger().info("fail to load pvp times, reset to 0");
			pvptime = "pvp" + "0";
			time = 0;
		}
	}
	public void newPvp(){
		++time;
		pvptime = pvptime.substring(0, 2) + time;
	}
	public void writePVPer(Player p1, Player p2){
		if(pvptime != null & pvptime != ""){
		pluginMain.pvpRecord.set(pvptime + ".pvp.p1", p1.getName());
		pluginMain.pvpRecord.set(pvptime + ".pvp.p1.agree", true);
		pluginMain.pvpRecord.set(pvptime + ".pvp.p2", p2.getName());
		pluginMain.pvpRecord.set(pvptime + ".pvp.p2.agree", false);
		save();
		}
	}
	public void writePVPerGo(Player p1, Player p2){
		if(pvptime != null & pvptime != ""){
		pluginMain.pvpRecord.set(pvptime + ".pvp.p2.agree", true);
		pluginMain.pvpRecord.set(pvptime + ".pvp.p1.cor", p1.getLocation());
		pluginMain.pvpRecord.set(pvptime + ".pvp.p2.cor", p2.getLocation());
		pluginMain.pvpRecord.set(pvptime + ".pvp.p1.death", false);
		pluginMain.pvpRecord.set(pvptime + ".pvp.p2.death", false);
		save();
		}
	}
	public void writePVPWin(String deathp, Player winp){
		if(pvptime != null & pvptime != ""){
		pluginMain.pvpRecord.set(pvptime + ".pvp." + deathp + ".death", true);
		pluginMain.pvpRecord.set(pvptime + ".pvp.winner", winp);
		save();
		}
	}
	public void writePVPEnd(){
		if(pvptime != null & pvptime != ""){
		pluginMain.pvpRecord.set(pvptime + ".pvp.end", true);
		save();
		}
	}
	public boolean isPVPEnd(){
		if(pvptime != null & pvptime != ""){
			if (!pluginMain.pvpRecord.isSet(pvptime + ".pvp.end")) {
				return true;
			}
			return pluginMain.pvpRecord.getBoolean(pvptime + ".pvp.end");
		}
		return true;
	}
	public String readPVPerInYml(Player p){
		if(pvptime != null & pvptime != ""){
		if(pluginMain.pvpRecord.get(pvptime + ".pvp.p1").equals(p.getName())){
			return "p1";
		}
		if(pluginMain.pvpRecord.get(pvptime + ".pvp.p2").equals(p.getName())){
			return "p2";
		}
		}
		return "";
	}
	public String readPVPerName(String ymlpath){
		if(pvptime != null & pvptime != ""){
		return pluginMain.pvpRecord.getString(pvptime + ".pvp." + ymlpath);
		}
		return "";
	}
	public Vector readPVPerLoc(String ymlpath){
		if(pvptime != null & pvptime != ""){
		return pluginMain.pvpRecord.getVector(pvptime + ".pvp." + ymlpath + ".cor");
		}
		return null;
	}
	public void save(){
		try {
			File f = new File(pluginMain.getDataFolder(), "pvprecord.yml");
			pluginMain.pvpRecord.save(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
