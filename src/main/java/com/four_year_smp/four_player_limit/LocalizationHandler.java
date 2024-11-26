package com.four_year_smp.four_player_limit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import org.yaml.snakeyaml.Yaml;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class LocalizationHandler {
    private final HashMap<String, String> _translations = new HashMap<>();
    private final Semaphore _translationLock = new Semaphore(1);
    private final FourPlayerLimitPlugin _plugin;

    public LocalizationHandler(FourPlayerLimitPlugin plugin) {
        _plugin = plugin;
        reload();
    }

    public Component getReloaded() {
        return LegacyComponentSerializer.legacy('&').deserialize(getTranslation("reloaded"));
    }

    public Component getPlayerLimit(int limit) {
        return LegacyComponentSerializer.legacy('&').deserialize(getTranslation("player-limit-get", limit));
    }

    public Component getSetPlayerLimit(int limit) {
        return LegacyComponentSerializer.legacy('&').deserialize(getTranslation("player-limit-set", limit));
    }

    public Component getSetPlayerLimitInvalid(String limit) {
        return LegacyComponentSerializer.legacy('&').deserialize(getTranslation("player-limit-set-invalid", limit));
    }

    public void reload() {
        _translationLock.acquireUninterruptibly();

        try {
            // Ensure the translation files exist
            ensureTranslationFilesExist();

            // Clear the existing translations
            _translations.clear();

            // Get the current language file
            String langFile = _plugin.getConfig().getString("lang", "en_US");

            // Iterate over all the keys in the translation file and add them to the translations map
            Path langFilePath = _plugin.getDataFolder().toPath().resolve("lang").resolve(langFile + ".yml");

            InputStream translationStream;
            try {
                translationStream = Files.newInputStream(langFilePath, StandardOpenOption.READ);
            } catch (IOException error) {
                _plugin.getLogger().severe(MessageFormat.format("Failed to open translation file, falling back to built-in translations: {0}", error));
                translationStream = _plugin.getResource("lang/en_US.yml");
            }

            // Load the translations from the file
            Yaml yaml = new Yaml();
            Map<?, ?> kvp = yaml.load(translationStream);
            for (Map.Entry<?, ?> entry : kvp.entrySet()) {
                _translations.put(entry.getKey().toString(), entry.getValue().toString());
                _plugin.logDebug(MessageFormat.format("Loaded translation: {0} -> {1}", entry.getKey(), entry.getValue()));
            }

            try {
                translationStream.close();
            } catch (IOException error) {
                _plugin.getLogger().severe(MessageFormat.format("Failed to close translation stream: {0}", error.getMessage()));
            }
        } finally {
            _translationLock.release();
        }
    }

    private String getTranslation(String key, Object... args) {
        _translationLock.acquireUninterruptibly();
        try {
            String translation = MessageFormat.format(_translations.getOrDefault(key, key), args);
            return MessageFormat.format("{0} {1}", _translations.getOrDefault("prefix", "[4PlayerLimit]"), translation);
        } finally {
            _translationLock.release();
        }
    }

    private boolean ensureTranslationFilesExist() {
        // Make sure the data folder exists
        if (!_plugin.getDataFolder().exists() && !_plugin.getDataFolder().mkdirs()) {
            _plugin.getLogger().severe(MessageFormat.format("Failed to create data folder: {0}", _plugin.getDataFolder().getAbsolutePath()));
            return false;
        }

        // Get the path to the translations directory
        Path translationPath = _plugin.getDataFolder().toPath().resolve("translations");

        // Attempt to load the current translation file
        String langFile = _plugin.getConfig().getString("lang", "en_US");
        Path translationFile = translationPath.resolve(langFile + ".yml");
        if (!translationFile.toFile().exists()) {
            // If the file was deleted...
            _plugin.saveResource("lang/" + langFile + ".yml", true);
        }

        return true;
    }
}
