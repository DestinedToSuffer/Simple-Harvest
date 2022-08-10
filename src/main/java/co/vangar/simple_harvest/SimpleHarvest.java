package co.vangar.simple_harvest;

import co.vangar.simple_harvest.harvest.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class SimpleHarvest extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getLogger().log(Level.INFO, "Simple Harvest started!");
    }
}
