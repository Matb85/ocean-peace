import { registerPlugin } from "@capacitor/core"
import type { PermissionsI, PermissionsMethods as PermissionsMethods } from "../../web/api/permissions";

interface PermissionsPlugin {
    getAllPermissions(): Promise<{
        usage: boolean, 
        notificationPolicy: boolean
    }>;
}

const Permissions = registerPlugin<PermissionsPlugin>("Permissions");

const plugin: PermissionsMethods = {
    async getAllPermissions(): Promise<PermissionsI> {
        const { usage, notificationPolicy} = await Permissions.getAllPermissions();

        return { usage, notificationPolicy };
    }
}

export default plugin;
