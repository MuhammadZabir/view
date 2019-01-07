import { IDepartment } from './department.model';
import { IPointConfig } from './point-config.model';

export interface ICompany {
    id?: number;
    name?: string;
    departments?: IDepartment[];
    pointConfig?: IPointConfig;
}

export class Company implements ICompany {
    constructor(public id?: number, public name?: string, public departments?: IDepartment[], public pointConfig?: IPointConfig) {}
}
