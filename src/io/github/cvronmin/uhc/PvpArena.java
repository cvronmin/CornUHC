package io.github.cvronmin.uhc;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class PvpArena {
	private final CornUHC pluginMain;
	public PvpArena(CornUHC main){
		pluginMain = main;
	}
	@SuppressWarnings("deprecation")
	public void generateArena(YamlConfiguration yml){
		World world = Bukkit.getWorld(yml.getString("world.name") + EnumWorldType.getType(yml.getString("world.type").toLowerCase()).getSuffix());
		Vector vec = yml.getVector("arena.base");
		int height = yml.getInt("arena.height");
		world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).setType(Material.STONE);
		world.getBlockAt(vec.getBlockX()-1, vec.getBlockY(), vec.getBlockZ()).setTypeIdAndData(Material.TORCH.getId(), (byte) 2, false);
		world.getBlockAt(vec.getBlockX()+1, vec.getBlockY(), vec.getBlockZ()).setTypeIdAndData(Material.TORCH.getId(), (byte) 1, false);
		world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()-1).setTypeIdAndData(Material.TORCH.getId(), (byte) 4, false);
		world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()+1).setTypeIdAndData(Material.TORCH.getId(), (byte) 3, false);
		for (int y = 1; y <= height; y++) {
			for (int x = -y; x <= y; x++) {
				for (int z = -y; z <= y; z++) {
/*					if (!(Math.abs(x) == y && Math.abs(z) == y && y > 1)) {
						if(
								((Math.abs(Math.abs(x) - Math.abs(z)) != y-1))
								&&
							(!((Math.abs(x) == height/2+1 && Math.abs(z) == height/2+1)&&(Math.abs(Math.abs(x) - Math.abs(z)) == 0))))*/
						world.getBlockAt(vec.getBlockX()+x, vec.getBlockY()+y, vec.getBlockZ()-(x - z)).setType(Material.SAND);						
//					}
				}
			}
			for (int x = -y; x <= y; x++) {
				for (int z = -y; z <= y; z++) {
					if(x != 0 & z != 0){
//						if (!(Math.abs(x) == y-1 && Math.abs(z) == y-1) && !(Math.abs(x) == y && Math.abs(z) == y && y > 1)) {
							if (!world.getBlockAt(vec.getBlockX()+x, vec.getBlockY()+y, vec.getBlockZ()+z).getType().equals(Material.SAND)) {
								world.getBlockAt(vec.getBlockX()+x, vec.getBlockY()+y, vec.getBlockZ()+z).setTypeIdAndData(Material.TORCH.getId(), getTorchSide(x, z), false);				
							}
//						}
					}
				}
			}
			world.getBlockAt(vec.getBlockX()-y-1, vec.getBlockY()+y, vec.getBlockZ()).setTypeIdAndData(Material.TORCH.getId(), (byte) 2, false);
			world.getBlockAt(vec.getBlockX()+y+1, vec.getBlockY()+y, vec.getBlockZ()).setTypeIdAndData(Material.TORCH.getId(), (byte) 1, false);
			world.getBlockAt(vec.getBlockX(), vec.getBlockY()+y, vec.getBlockZ()-y-1).setTypeIdAndData(Material.TORCH.getId(), (byte) 4, false);
			world.getBlockAt(vec.getBlockX(), vec.getBlockY()+y, vec.getBlockZ()+y+1).setTypeIdAndData(Material.TORCH.getId(), (byte) 3, false);

		}

	}
	public void setBase(Vector vec){
		pluginMain.pvpArena.set("arena.base", vec);
		save();
	}
	public void save(){
		try {
			File f = new File(pluginMain.getDataFolder(), "pvparena.yml");
			pluginMain.pvpArena.save(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private byte getTorchSide(int x, int z){
		boolean flag1 = x < 0;
		boolean flag2 = z < 0;
		if(!flag1 && !flag2){return 1;}
		if( flag1 &&  flag2){return 2;}
		if( flag1 && !flag2){return 3;}
		if(!flag1 &&  flag2){return 4;}
		return 0;
	}
	private static enum EnumWorldType{
		OVERWORLD(""),NETHER("_nether"),THE_END("_the_end");
		private final String suffix;
		private EnumWorldType(String suffix){
			this.suffix = suffix;
		}
		public String getSuffix(){
			return suffix;
		}
		public static EnumWorldType getType(String s){
			switch (s) {
			case "overworld":
				return EnumWorldType.OVERWORLD;
			case "nether":
				return EnumWorldType.NETHER;
			case "the end":
				return EnumWorldType.THE_END;
			case "normal":
				return EnumWorldType.OVERWORLD;
			case "end":
				return EnumWorldType.THE_END;
			default:
				return EnumWorldType.OVERWORLD;
			}
		}
	}
}
