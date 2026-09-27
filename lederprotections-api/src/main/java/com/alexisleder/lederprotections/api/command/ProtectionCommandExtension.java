package com.alexisleder.lederprotections.api.command;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Subcomando aportado por un addon al comando principal de LederProtections.
 */
public interface ProtectionCommandExtension {

    /**
     * Comprueba si el emisor puede ver y ejecutar la extensión.
     *
     * <p>El addon conserva la responsabilidad completa sobre sus permisos.</p>
     *
     * @param sender emisor
     * @return true si puede usarla
     */
    boolean canExecute(CommandSender sender);

    /**
     * Ejecuta la extensión.
     *
     * @param sender emisor
     * @param label alias usado para el comando principal
     * @param arguments argumentos posteriores al nombre de la extensión
     */
    void execute(CommandSender sender, String label, List<String> arguments);

    /**
     * Sugiere argumentos posteriores al nombre de la extensión.
     *
     * @param sender emisor
     * @param label alias usado para el comando principal
     * @param arguments argumentos posteriores al nombre de la extensión
     * @return sugerencias; nunca debe devolver null
     */
    default List<String> tabComplete(
            final CommandSender sender,
            final String label,
            final List<String> arguments
    ) {
        return List.of();
    }
}
