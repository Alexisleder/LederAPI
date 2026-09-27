package com.alexisleder.lederprotections.api.model;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

/**
 * Vista pública e inmutable del home de teletransporte de una protección.
 *
 * <p>El snapshot conserva el último home conocido incluso en un
 * {@code ProtectionRemovedEvent}. Resolver el mundo y comprobar que el destino
 * continúa siendo seguro corresponde a la API en el momento de la consulta.</p>
 *
 * @param worldName nombre del mundo
 * @param x coordenada X exacta
 * @param y coordenada Y exacta
 * @param z coordenada Z exacta
 * @param yaw orientación horizontal
 * @param pitch orientación vertical
 */
public record ProtectionTeleportHomeSnapshot(
        String worldName,
        double x,
        double y,
        double z,
        float yaw,
        float pitch
) {

    /** Crea un snapshot validado. */
    public ProtectionTeleportHomeSnapshot {
        Objects.requireNonNull(worldName, "worldName");

        if (worldName.isBlank()) {
            throw new IllegalArgumentException("worldName no puede estar vacío");
        }

        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) {
            throw new IllegalArgumentException("Las coordenadas del home deben ser finitas");
        }

        if (!Float.isFinite(yaw) || !Float.isFinite(pitch)) {
            throw new IllegalArgumentException("La orientación del home debe ser finita");
        }
    }

    /**
     * Convierte el snapshot a una ubicación usando un mundo ya resuelto.
     *
     * @param world mundo cuyo nombre debe coincidir
     * @return copia Bukkit del home
     */
    public Location toLocation(final World world) {
        Objects.requireNonNull(world, "world");

        if (!worldName.equals(world.getName())) {
            throw new IllegalArgumentException("El mundo no coincide con el home de la protección");
        }

        return new Location(world, x, y, z, yaw, pitch);
    }
}
