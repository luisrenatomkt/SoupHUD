package com.soupmod.souphud;

import com.soupmod.souphud.config.ConfigManager;
import com.soupmod.souphud.counter.InventoryScanner;
import com.soupmod.souphud.event.EventRegistry;
import com.soupmod.souphud.hud.HudManager;
import com.soupmod.souphud.input.KeyManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe principal do Soup HUD. Mod somente de cliente: em servidores dedicados nada e inicializado,
 * e "acceptableRemoteVersions = *" permite entrar em servidores que nao tem o mod.
 */
@Mod(modid = SoupHudMod.MODID, name = SoupHudMod.NAME, version = SoupHudMod.VERSION,
        acceptedMinecraftVersions = "[1.8.9]", acceptableRemoteVersions = "*")
public class SoupHudMod {

    public static final String MODID = "souphud";
    public static final String NAME = "Soup HUD";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private static ConfigManager configManager;
    private static InventoryScanner scanner;
    private static HudManager hudManager;
    private static KeyManager keyManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (event.getSide() != Side.CLIENT) {
            LOGGER.info("Soup HUD e um mod de cliente; nada sera carregado neste lado.");
            return;
        }
        configManager = new ConfigManager(event.getModConfigurationDirectory());
        configManager.load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide() != Side.CLIENT || configManager == null) {
            return;
        }
        scanner = new InventoryScanner();
        hudManager = new HudManager(configManager.getConfig(), scanner);
        keyManager = new KeyManager();
        EventRegistry.registerAll(keyManager, hudManager, scanner);
        LOGGER.info("Soup HUD {} carregado.", VERSION);
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static InventoryScanner getScanner() {
        return scanner;
    }

    public static HudManager getHudManager() {
        return hudManager;
    }

    public static KeyManager getKeyManager() {
        return keyManager;
    }
}
