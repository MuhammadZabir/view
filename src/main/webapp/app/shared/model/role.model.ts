import { IPermission } from './permission.model';

export interface IRole {
    id?: number;
    name?: string;
    description?: string;
    permissions?: IPermission[];
}

export class Role implements IRole {
    constructor(public id?: number, public name?: string, public description?: string, public permissions?: IPermission[]) {}
}
