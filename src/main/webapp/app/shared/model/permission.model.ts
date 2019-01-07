import { IRole } from './role.model';

export interface IPermission {
    id?: number;
    name?: string;
    description?: string;
    action?: string;
    roles?: IRole[];
}

export class Permission implements IPermission {
    constructor(public id?: number, public name?: string, public description?: string, public action?: string, public roles?: IRole[]) {}
}
