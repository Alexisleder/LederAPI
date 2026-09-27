package com.alexisleder.lederprotections.api;

import com.alexisleder.lederprotections.api.command.ProtectionCommandExtensionRegistry;
import com.alexisleder.lederprotections.api.model.ProtectionAccessRole;
import com.alexisleder.lederprotections.api.model.ProtectionSnapshot;
import com.alexisleder.lederprotections.api.model.SafeLocationRequest;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

/**
 * Contrato público y versionado de LederProtections para plugins externos.
 *
 * <p>Los consumidores deben obtener esta interfaz mediante el
 * {@code ServicesManager} de Bukkit y declarar LederProtections como
 * {@code softdepend} o {@code depend}. Ningún método expone modelos internos
 * mutables ni registros de persistencia.</p>
 *
 * <p>Todos los métodos deben invocarse desde el hilo principal del servidor.
 * Las ubicaciones devueltas son copias y pueden modificarse de forma segura.</p>
 */
public interface LederProtectionsApi {

    /** Versión del contrato público, independiente de la versión del plugin. */
    String API_VERSION = "1.0";

    /**
     * Obtiene la versión del contrato implementado.
     *
     * @return versión de API
     */
    String getApiVersion();

    /**
     * Busca la protección que contiene una ubicación.
     *
     * @param location ubicación
     * @return snapshot inmutable, o vacío si no está protegida
     */
    Optional<ProtectionSnapshot> findProtectionAt(Location location);

    /**
     * Busca una protección por su identificador persistente.
     *
     * @param protectionId identificador persistente
     * @return snapshot inmutable
     */
    Optional<ProtectionSnapshot> findProtectionById(String protectionId);

    /**
     * Resuelve únicamente el rol almacenado de un UUID.
     *
     * <p>Este método no evalúa permisos de Bukkit ni bypasses administrativos.
     * Es adecuado para consultas offline y para diferenciar membresía persistida
     * de privilegios temporales.</p>
     *
     * @param playerId UUID del jugador
     * @param protectionId identificador de protección
     * @return rol, o vacío si la protección no existe
     */
    Optional<ProtectionAccessRole> getStoredRole(UUID playerId, String protectionId);

    /**
     * Resuelve el rol efectivo de membresía de un jugador conectado.
     *
     * <p>Solo {@code lederprotections.admin} eleva el resultado a
     * {@link ProtectionAccessRole#ADMIN}. Los permisos técnicos del motor de
     * reglas, incluido {@code lederprotections.bypass.rules}, no conceden un rol
     * público de membresía ni deben ampliar capacidades de addons. Un rol
     * almacenado {@link ProtectionAccessRole#BLOCKED} conserva precedencia sobre
     * el permiso administrativo.</p>
     *
     * @param player jugador conectado
     * @param protectionId identificador de protección
     * @return rol efectivo de membresía, o vacío si la protección no existe
     */
    Optional<ProtectionAccessRole> getEffectiveRole(Player player, String protectionId);

    /**
     * Comprueba si el jugador puede usar funciones reservadas a miembros.
     *
     * <p>{@link ProtectionAccessRole#BLOCKED} siempre deniega, incluso si el
     * jugador tiene {@code lederprotections.admin}. Esta precedencia permite que
     * addons sensibles, como sistemas de vuelo, fallen de forma segura.</p>
     *
     * @param player jugador
     * @param protectionId identificador de protección
     * @return true para admin, dueño, codueño o miembro
     */
    boolean canUseMemberFeatures(Player player, String protectionId);

    /**
     * Comprueba si el jugador puede administrar ajustes similares a flags.
     *
     * <p>Los administradores y el dueño pueden siempre. Los codueños solo pueden
     * cuando el dueño les permitió editar flags; miembros y visitantes no.</p>
     *
     * @param player jugador
     * @param protectionId identificador de protección
     * @return true si puede administrar ajustes de addons
     */
    boolean canManageSettings(Player player, String protectionId);

    /**
     * Comprueba silenciosamente la regla de ruptura de bloques.
     *
     * @param player jugador conectado
     * @param location ubicación del bloque
     * @return true si LederProtections permite romperlo
     */
    boolean canBreakBlock(Player player, Location location);

    /**
     * Comprueba silenciosamente la regla de colocación de bloques.
     *
     * @param player jugador conectado
     * @param location ubicación donde quedaría el bloque
     * @return true si LederProtections permite colocarlo
     */
    boolean canPlaceBlock(Player player, Location location);

    /**
     * Comprueba silenciosamente la interacción de clic derecho con un bloque.
     *
     * <p>Se aplica la misma regla específica que usan los listeners internos
     * para puertas, trampillas, botones, palancas, contenedores y demás bloques
     * controlados. Los materiales que LederProtections no gobierna se permiten.</p>
     *
     * @param player jugador conectado
     * @param location ubicación del bloque
     * @return true si la interacción está permitida
     */
    boolean canInteractWithBlock(Player player, Location location);

    /**
     * Comprueba si un jugador puede abrir el contenedor físico de una ubicación.
     *
     * <p>La decisión reutiliza exactamente la regla correspondiente al material:
     * {@code chest-open}, {@code barrel-open}, {@code shulker-open} o
     * {@code container-open}. También respeta overrides por protección, el
     * estado global del motor de reglas y su permiso de bypass configurado.</p>
     *
     * <p>Este método no envía mensajes al jugador. Está pensado para que plugins
     * que operan sobre inventarios, como sistemas de sell wands, validen el
     * acceso antes de leer, retirar o vender cualquier ítem.</p>
     *
     * @param player jugador que intenta operar sobre el contenedor
     * @param location ubicación del bloque contenedor
     * @return true si se permite la apertura; false para entradas inválidas o
     *         materiales que no sean contenedores controlados
     */
    boolean canOpenContainer(Player player, Location location);

    /**
     * Comprueba si una ubicación pertenece a una protección concreta.
     *
     * @param protectionId identificador de protección
     * @param location ubicación
     * @return true si está dentro de sus límites actuales
     */
    boolean contains(String protectionId, Location location);

    /**
     * Calcula la distancia horizontal hasta el borde geométrico más cercano.
     *
     * @param protectionId identificador de protección
     * @param location ubicación que debe estar dentro de la protección
     * @return distancia en bloques, o vacío si no existe/no contiene la ubicación
     */
    OptionalDouble getHorizontalDistanceToBoundary(String protectionId, Location location);

    /**
     * Busca una posición segura dentro de una protección.
     *
     * <p>La búsqueda reutiliza las validaciones del teletransporte de
     * LederProtections y está acotada por los límites definidos en la solicitud.</p>
     *
     * @param request solicitud validada
     * @return ubicación segura centrada, o vacío si no puede demostrarse
     */
    Optional<Location> findSafeLocation(SafeLocationRequest request);

    /**
     * Busca una posición segura usando un snapshot aunque el registro ya no exista.
     *
     * <p>Este overload está diseñado para reaccionar a
     * {@code ProtectionRemovedEvent}. La identidad del snapshot y de la solicitud
     * debe coincidir. La búsqueda usa únicamente los límites, piedra y home
     * inmutables del snapshot y vuelve a comprobar el estado actual de los bloques.</p>
     *
     * @param protection último snapshot conocido
     * @param request solicitud cuyo protectionId debe coincidir
     * @return ubicación segura dentro de los límites anteriores, o vacío
     */
    Optional<Location> findSafeLocation(ProtectionSnapshot protection, SafeLocationRequest request);

    /**
     * Obtiene el registro de subcomandos para extender formalmente {@code /p}.
     *
     * @return registro de extensiones
     */
    ProtectionCommandExtensionRegistry getCommandExtensions();
}
