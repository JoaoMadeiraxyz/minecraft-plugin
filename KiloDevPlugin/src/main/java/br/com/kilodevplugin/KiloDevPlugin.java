package br.com.kilodevplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class KiloDevPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.SNOWBALL) {
            event.getPlayer().launchProjectile(Snowball.class);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.SNOWBALL) {
            if (event.getEntity().getShooter() instanceof Player) {
                Snowball snowball = (Snowball) event.getEntity();
                Pig pig = (Pig) event.getEntity().getWorld().spawnEntity(snowball.getLocation(), EntityType.PIG);
                pig.setCustomName("ExplodingPig");
                pig.setCustomNameVisible(false);
                pig.setInvulnerable(true);

                // Aguarda 20 ticks (1 segundo) antes de explodir
                Bukkit.getScheduler().runTaskLater(this, () -> explodePig(pig), 20);
                event.getEntity().remove();
            }
        }
    }

    private void explodePig(Pig pig) {
        pig.getWorld().createExplosion(pig.getLocation(), 4.0f);
        pig.remove();
    }
}
