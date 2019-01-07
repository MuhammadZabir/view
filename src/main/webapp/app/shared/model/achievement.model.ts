import { IUser } from '../../core/user/user.model';
import { IDepartment } from './department.model';
import { ICriteria } from './criteria.model';

export interface IAchievement {
    id?: number;
    status?: string;
    user?: IUser;
    department?: IDepartment;
    criteria?: ICriteria;
}

export class Achievement implements IAchievement {
    constructor(
        public id?: number,
        public status?: string,
        public user?: IUser,
        public department?: IDepartment,
        public criteria?: ICriteria
    ) {}
}
