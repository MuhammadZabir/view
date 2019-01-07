import { Moment } from 'moment';
import { IIssueDifficulty } from './issue-difficulty.model';
import { IStatusCategory } from './status-category.model';

export interface IPointConfig {
    id?: number;
    startDate?: Moment;
    endDate?: Moment;
    issueDifficulties?: IIssueDifficulty[];
    statusCategories?: IStatusCategory[];
}

export class PointConfig implements IPointConfig {
    constructor(
        public id?: number,
        public startDate?: Moment,
        public endDate?: Moment,
        public issueDifficulties?: IIssueDifficulty[],
        public statusCategories?: IStatusCategory[]
    ) {}
}
