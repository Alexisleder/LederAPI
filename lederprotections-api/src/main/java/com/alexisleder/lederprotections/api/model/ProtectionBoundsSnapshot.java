package com.alexisleder.lederprotections.api.model;

import org.bukkit.Location;

import java.util.Objects;
import java.util.OptionalDouble;

/**
 * Vista inmutable de los límites inclusivos de una protección.
 *
 * @param worldName nombre del mundo
 * @param minX mínimo X inclusivo
 * @param minY mínimo Y inclusivo
 * @param minZ mínimo Z inclusivo
 * @param maxX máximo X inclusivo
 * @param maxY máximo Y inclusivo
 * @param maxZ máximo Z inclusivo
 */
public record ProtectionBoundsSnapshot(
        String worldName,
        int minX,
        int minY,
        int minZ,
        int maxX,
        int maxY,
        int maxZ
) {

    /** Crea límites públicos validados. */
    public ProtectionBoundsSnapshot {
        Objects.requireNonNull(worldName, "worldName");

        if (worldName.isBlank()) {
            throw new IllegalArgumentException("worldName no puede estar vacío");
        }

        if (minX > maxX || minY > maxY || minZ > maxZ) {
            throw new IllegalArgumentException("Los límites mínimos no pueden superar a los máximos");
        }
    }

    /**
     * Comprueba una ubicación usando coordenadas de bloque.
     *
     * @param location ubicación
     * @return true si está dentro
     */
    public boolean contains(final Location location) {
        if (location == null || location.getWorld() == null) {
            return false;
        }

        return worldName.equals(location.getWorld().getName())
                && location.getBlockX() >= minX
                && location.getBlockX() <= maxX
                && location.getBlockY() >= minY
                && location.getBlockY() <= maxY
                && location.getBlockZ() >= minZ
                && location.getBlockZ() <= maxZ;
    }

    /**
     * Calcula la distancia horizontal hasta abandonar estos límites.
     *
     * @param location ubicación contenida
     * @return distancia geométrica, o vacío si la ubicación no está dentro
     */
    public OptionalDouble horizontalDistanceToBoundary(final Location location) {
        if (!contains(location)) {
            return OptionalDouble.empty();
        }

        final double x = location.getX();
        final double z = location.getZ();
        final double distance = Math.min(
                Math.min(x - minX, (maxX + 1.0D) - x),
                Math.min(z - minZ, (maxZ + 1.0D) - z)
        );

        return OptionalDouble.of(Math.max(0.0D, distance));
    }
}
