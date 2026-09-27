package com.alexisleder.lederprotections.api.model;

import org.bukkit.Location;

import java.util.Objects;

/**
 * Solicitud acotada para buscar una posición segura dentro de una protección.
 *
 * @param protectionId identificador persistente
 * @param preferredLocation punto desde el cual iniciar la búsqueda
 * @param horizontalInset bloques que deben quedar entre el destino y cada borde horizontal
 * @param horizontalRadius radio máximo de búsqueda, entre 0 y 16
 * @param verticalRange rango vertical máximo, entre 0 y 32
 * @param fallbackToProtectionHome true para intentar el home y la piedra como fallback
 */
public record SafeLocationRequest(
        String protectionId,
        Location preferredLocation,
        int horizontalInset,
        int horizontalRadius,
        int verticalRange,
        boolean fallbackToProtectionHome
) {

    /** Crea una solicitud validada y copia su ubicación mutable. */
    public SafeLocationRequest {
        Objects.requireNonNull(protectionId, "protectionId");
        Objects.requireNonNull(preferredLocation, "preferredLocation");

        if (protectionId.isBlank()) {
            throw new IllegalArgumentException("protectionId no puede estar vacío");
        }

        if (horizontalInset < 0 || horizontalInset > 16) {
            throw new IllegalArgumentException("horizontalInset debe estar entre 0 y 16");
        }

        if (horizontalRadius < 0 || horizontalRadius > 16) {
            throw new IllegalArgumentException("horizontalRadius debe estar entre 0 y 16");
        }

        if (verticalRange < 0 || verticalRange > 32) {
            throw new IllegalArgumentException("verticalRange debe estar entre 0 y 32");
        }

        preferredLocation = preferredLocation.clone();
    }

    /**
     * Devuelve una copia de la ubicación preferida.
     *
     * @return ubicación copiada
     */
    @Override
    public Location preferredLocation() {
        return preferredLocation.clone();
    }
}
