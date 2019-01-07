import { ICompany } from 'app/shared/model//company.model';
import { IAchievement } from 'app/shared/model//achievement.model';

export interface IDepartment {
    id?: number;
    name?: string;
    description?: string;
    company?: ICompany;
    achievements?: IAchievement[];
}

export class Department implements IDepartment {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public company?: ICompany,
        public achievements?: IAchievement[]
    ) {}
}
