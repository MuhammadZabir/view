import { IPointConfig } from './point-config.model';

export interface IStatusCategory {
    id?: number;
    name?: string;
    point?: number;
    main?: boolean;
    pointConfig?: IPointConfig;
}

export class StatusCategory implements IStatusCategory {
    constructor(public id?: number, public name?: string, public point?: number, public main?: boolean, public pointConfig?: IPointConfig) {
        this.main = false;
    }
}
