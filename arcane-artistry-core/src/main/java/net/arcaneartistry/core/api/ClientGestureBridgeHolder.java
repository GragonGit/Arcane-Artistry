package net.arcaneartistry.core.api;

/** Static holder for the installed {@link ClientGestureBridge}. See that type's docs. */
public final class ClientGestureBridgeHolder {
    private static ClientGestureBridge instance = ClientGestureBridge.NOOP;

    private ClientGestureBridgeHolder() {
    }

    public static void set(ClientGestureBridge bridge) {
        instance = bridge;
    }

    public static ClientGestureBridge get() {
        return instance;
    }
}
