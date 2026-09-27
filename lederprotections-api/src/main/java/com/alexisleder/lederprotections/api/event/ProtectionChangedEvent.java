package com.alexisleder.lederprotections.api.event;

import com.alexisleder.lederprotections.api.model.ProtectionSnapshot;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Evento no cancelable emitido después de persistir cambios en una protección.
 */
public final class ProtectionChangedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ProtectionSnapshot previousProtection;
    private final ProtectionSnapshot protection;
    private final Set<ProtectionChangeType> changes;

    /**
     * Crea el evento.
     *
     * @param previousProtection snapshot anterior
     * @param protection snapshot actual
     * @param changes categorías modificadas
     */
    public ProtectionChangedEvent(
            final ProtectionSnapshot previousProtection,
            final ProtectionSnapshot protection,
            final Set<ProtectionChangeType> changes
    ) {
        this.previousProtection = Objects.requireNonNull(previousProtection, "previousProtection");
        this.protection = Objects.requireNonNull(protection, "protection");
        Objects.requireNonNull(changes, "changes");

        if (changes.isEmpty()) {
            throw new IllegalArgumentException("changes no puede estar vacío");
        }

        this.changes = Collections.unmodifiableSet(EnumSet.copyOf(changes));
    }

    /**
     * Obtiene el estado anterior.
     *
     * @return snapshot anterior
     */
    public ProtectionSnapshot getPreviousProtection() {
        return previousProtection;
    }

    /**
     * Obtiene el estado actual.
     *
     * @return snapshot actual
     */
    public ProtectionSnapshot getProtection() {
        return protection;
    }

    /**
     * Obtiene las categorías modificadas.
     *
     * @return conjunto inmutable y no vacío
     */
    public Set<ProtectionChangeType> getChanges() {
        return changes;
    }

    /**
     * Comprueba una categoría concreta.
     *
     * @param change categoría
     * @return true si cambió
     */
    public boolean hasChange(final ProtectionChangeType change) {
        return changes.contains(Objects.requireNonNull(change, "change"));
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
