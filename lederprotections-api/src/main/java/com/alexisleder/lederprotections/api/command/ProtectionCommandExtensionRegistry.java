package com.alexisleder.lederprotections.api.command;

import org.bukkit.plugin.Plugin;

/**
 * Registro público para agregar subcomandos a {@code /p} y sus aliases.
 *
 * <p>Los nombres internos de LederProtections están reservados. Cada nombre solo
 * puede pertenecer a un addon y se libera automáticamente cuando ese addon se
 * desactiva.</p>
 */
public interface ProtectionCommandExtensionRegistry {

    /**
     * Registra un subcomando.
     *
     * @param owner plugin propietario
     * @param subCommand nombre sin barra ni espacios
     * @param extension implementación
     * @throws IllegalArgumentException si el nombre no es válido o está reservado
     * @throws IllegalStateException si el nombre ya está registrado
     */
    void register(Plugin owner, String subCommand, ProtectionCommandExtension extension);

    /**
     * Elimina un subcomando si pertenece al plugin indicado.
     *
     * @param owner plugin propietario
     * @param subCommand nombre registrado
     * @return true si se eliminó
     */
    boolean unregister(Plugin owner, String subCommand);

    /**
     * Elimina todas las extensiones de un plugin.
     *
     * @param owner plugin propietario
     */
    void unregisterAll(Plugin owner);
}
