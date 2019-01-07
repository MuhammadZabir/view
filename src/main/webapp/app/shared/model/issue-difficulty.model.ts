import { IPointConfig } from './point-config.model';

export interface IIssueDifficulty {
    id?: number;
    name?: string;
    value?: number;
    pointConfig?: IPointConfig;
}

export class IssueDifficulty implements IIssueDifficulty {
    constructor(
        public id?: number,
        public name?: string,
        public value?: number,
        public pointConfig?: IPointConfig
    ) {}
}
