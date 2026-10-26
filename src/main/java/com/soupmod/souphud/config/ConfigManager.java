package com.soupmod.souphud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soupmod.souphud.SoupHudMod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/** Persistencia: le e grava config/souphud.json (UTF-8, gravacao via arquivo temporario). */
public final class ConfigManager {

    private final File file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private HudConfig config = new HudConfig();

    public ConfigManager(File configDirectory) {
        this.file = new File(configDirectory, "souphud.json");
    }

    public HudConfig getConfig() {
        return config;
    }

    public void load() {
        HudConfig loaded = null;
        if (file.isFile()) {
            try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                loaded = gson.fromJson(reader, HudConfig.class);
            } catch (Exception ex) {
                SoupHudMod.LOGGER.error("Nao foi possivel ler " + file.getName() + "; usando padroes.", ex);
                backupBrokenFile();
            }
        }
        config = loaded != null ? loaded : new HudConfig();
        config.sanitize();
        config.markChanged();
        if (!file.isFile()) {
            save();
        }
    }

    public synchronized void save() {
        File dir = file.getParentFile();
        if (dir != null && !dir.isDirectory() && !dir.mkdirs()) {
            SoupHudMod.LOGGER.error("Nao foi possivel criar a pasta de configuracao.");
            return;
        }
        File tmp = new File(dir, file.getName() + ".tmp");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(tmp), StandardCharsets.UTF_8)) {
            gson.toJson(config, writer);
        } catch (Exception ex) {
            SoupHudMod.LOGGER.error("Falha ao gravar a configuracao.", ex);
            return;
        }
        try {
            Files.move(tmp.toPath(), file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception atomicFailure) {
            try {
                Files.move(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ex) {
                SoupHudMod.LOGGER.error("Falha ao substituir o arquivo de configuracao.", ex);
            }
        }
    }

    private void backupBrokenFile() {
        try {
            File backup = new File(file.getParentFile(), file.getName() + ".broken");
            Files.move(file.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ignored) {
            // melhor esforco
        }
    }
}
