package com.alexisleder.lederprotections.api.event;

import com.alexisleder.lederprotections.api.model.ProtectionSnapshot;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Objects;

/**
 * Evento no cancelable emitido después de eliminar y desindexar una protección.
 */
public final class ProtectionRemovedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ProtectionSnapshot protection;

    /**
     * Crea el evento.
     *
     * @param protection último snapshot persistido
     */
    public ProtectionRemovedEvent(final ProtectionSnapshot protection) {
        this.protection = Objects.requireNonNull(protection, "protection");
    }

    /**
     * Obtiene la protección eliminada.
     *
     * @return snapshot inmutable anterior a la eliminación
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
