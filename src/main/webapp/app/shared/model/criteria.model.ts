import { ICriteria } from './criteria.model';

export interface ICriteria {
    id?: number;
    name?: string;
    description?: string;
    level?: number;
    type?: string;
    criteria?: ICriteria[];
}

export class Criteria implements ICriteria {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public level?: number,
        public type?: string,
        public criteria?: ICriteria[]
    ) {}
}
