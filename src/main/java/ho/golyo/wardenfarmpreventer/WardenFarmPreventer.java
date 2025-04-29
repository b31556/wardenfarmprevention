package ho.golyo.wardenfarmpreventer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.logging.Logger;

public class WardenFarmPreventer extends JavaPlugin implements Listener {
    private static final Logger LOGGER = Logger.getLogger("WardenFarmPreventer");
    private Random random;
    private int searchRadius;
    private int minY;
    private int maxY;
    private int maxAttempts;

    @Override
    public void onEnable() {
        // Konfiguráció betöltése
        saveDefaultConfig();
        loadConfig();

        // Inicializálás
        random = new Random();
        getServer().getPluginManager().registerEvents(this, this);
        LOGGER.info("WardenFarmPreventer plugin enabled.");
    }

    @Override
    public void onDisable() {
        LOGGER.info("WardenFarmPreventer plugin disabled.");
    }

    private void loadConfig() {
        searchRadius = getConfig().getInt("search-radius", 50);
        minY = getConfig().getInt("min-y", -64);
        maxY = getConfig().getInt("max-y", 0);
        maxAttempts = getConfig().getInt("max-attempts", 20);
    }

    @EventHandler
    public void onWardenSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() != org.bukkit.entity.EntityType.WARDEN) return;

        Location wardenLoc = event.getEntity().getLocation();
        Block shrieker = findSculkShriekerNear(wardenLoc);

        if (shrieker != null && shrieker.getType() == Material.SCULK_SHRIEKER) {
            // Aszinkron pozíció keresés
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location newLoc = findRandomDeepDarkLocation(shrieker.getWorld(), shrieker.getLocation());

                    // Szinkron blokk módosítás
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // Töröljük a Shrieker-t
                            shrieker.getWorld().getBlockAt(shrieker.getLocation()).setType(Material.AIR);
                            LOGGER.info("Sculk Shrieker removed at " + shrieker.getLocation());

                            // Helyezzük el az új Shrieker-t, ha találtunk pozíciót
                            if (newLoc != null) {
                                newLoc.getBlock().setType(Material.SCULK_SHRIEKER);
                                LOGGER.info("Sculk Shrieker placed at " + newLoc);
                            } else {
                                LOGGER.warning("Could not find a suitable location for Sculk Shrieker.");
                            }
                        }
                    }.runTask(WardenFarmPreventer.this);
                }
            }.runTaskAsynchronously(this);
        }
    }

    private Block findSculkShriekerNear(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        // 5x5x5 területen keresünk Shrieker-t
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    Block block = world.getBlockAt(x + dx, y + dy, z + dz);
                    if (block.getType() == Material.SCULK_SHRIEKER) {
                        return block;
                    }
                }
            }
        }
        return null;
    }

    private Location findRandomDeepDarkLocation(World world, Location origin) {
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = origin.getBlockX() + random.nextInt(searchRadius * 2) - searchRadius;
            int z = origin.getBlockZ() + random.nextInt(searchRadius * 2) - searchRadius;
            int y = random.nextInt(maxY - minY + 1) + minY;

            Location candidate = new Location(world, x, y, z);

            // Ellenőrizzük, hogy Deep Dark biome-ban van-e
            if (world.getBiome(x, y, z) != Biome.DEEP_DARK) {
                continue;
            }

            // Ellenőrizzük, hogy a pozíció alkalmas-e
            Block block = world.getBlockAt(x, y, z);
            Block above = world.getBlockAt(x, y + 1, z);
            if (block.getType().isSolid() && above.getType() == Material.AIR) {
                return new Location(world, x, y + 1, z);
            }
        }
        return null;
    }
}