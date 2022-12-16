export interface PermissionsI {
    usage: boolean;
    notificationPolicy: boolean;
}

export interface PermissionsMethods {
    getAllPermissions(): Promise<PermissionsI>;
}