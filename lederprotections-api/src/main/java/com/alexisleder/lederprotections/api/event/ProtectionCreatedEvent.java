package com.alexisleder.lederprotections.api.event;

import com.alexisleder.lederprotections.api.model.ProtectionSnapshot;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

/**
 * Evento no cancelable emitido después de persistir e indexar una protección.
 */
public final class ProtectionCreatedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ProtectionSnapshot protection;

    /**
     * Crea el evento.
     *
     * @param protection snapshot creado
     */
    public ProtectionCreatedEvent(final ProtectionSnapshot protection) {
        this.protection = Objects.requireNonNull(protection, "protection");
    }

    /**
     * Obtiene la protección creada.
     *
     * @return snapshot inmutable
     */
    public ProtectionSnapshot getProtection() {
        return protection;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Obtiene la lista estática de handlers requerida por Bukkit.
     *
     * @return handlers
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
