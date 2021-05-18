package net.thiccaxe.resonance.logging;

import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class JavaLogger implements ResonanceLogger {
    private final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacySection();

    private final Logger logger;

    public JavaLogger(Logger logger) {
        this.logger = logger;
    }


    @Override
    public void info(@NotNull String message) {
        logger.info(message);
    }

    @Override
    public void info(@NotNull ComponentLike message) {
        info(legacyComponentSerializer.serialize(message.asComponent()));
    }

    @Override
    public void warn(@NotNull String message) {
        logger.warning(message);
    }

    @Override
    public void warn(@NotNull ComponentLike message) {
        warn(legacyComponentSerializer.serialize(message.asComponent()));
    }

    @Override
    public void error(@NotNull String message) {
        logger.severe(message);
    }

    @Override
    public void error(@NotNull ComponentLike message) {
        error(legacyComponentSerializer.serialize(message.asComponent()));
    }

    @Override
    public @NotNull String prefix() {
        return logger.getName();
    }

    @Override
    public void sendMessage(@NonNull ComponentLike message) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Identified source, @NonNull ComponentLike message) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Identity source, @NonNull ComponentLike message) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Component message) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Identified source, @NonNull Component message) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Identity source, @NonNull Component message) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull ComponentLike message, @NonNull MessageType type) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Identified source, @NonNull ComponentLike message, @NonNull MessageType type) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Identity source, @NonNull ComponentLike message, @NonNull MessageType type) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Component message, @NonNull MessageType type) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Identified source, @NonNull Component message, @NonNull MessageType type) {
        info(message);
    }

    @Override
    public void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type) {
        info(message);
    }

    //Unused methods

    @Override
    public void sendActionBar(@NonNull ComponentLike message) {

    }

    @Override
    public void sendActionBar(@NonNull Component message) {

    }

    @Override
    public void sendPlayerListHeader(@NonNull ComponentLike header) {

    }

    @Override
    public void sendPlayerListHeader(@NonNull Component header) {

    }

    @Override
    public void sendPlayerListFooter(@NonNull ComponentLike footer) {

    }

    @Override
    public void sendPlayerListFooter(@NonNull Component footer) {

    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NonNull ComponentLike header, @NonNull ComponentLike footer) {

    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NonNull Component header, @NonNull Component footer) {

    }

    @Override
    public void showTitle(@NonNull Title title) {

    }

    @Override
    public void clearTitle() {

    }

    @Override
    public void resetTitle() {

    }

    @Override
    public void showBossBar(@NonNull BossBar bar) {

    }

    @Override
    public void hideBossBar(@NonNull BossBar bar) {

    }

    @Override
    public void playSound(@NonNull Sound sound) {

    }

    @Override
    public void playSound(@NonNull Sound sound, double x, double y, double z) {

    }

    @Override
    public void stopSound(@NonNull SoundStop stop) {

    }

    @Override
    public void openBook(Book.@NonNull Builder book) {

    }

    @Override
    public void openBook(@NonNull Book book) {

    }
}