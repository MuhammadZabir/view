import { IUser } from '../../core/user/user.model';

export interface IFeedback {
    id?: number;
    subject?: string;
    description?: string;
    rating?: number;
    user?: IUser;
}

export class Feedback implements IFeedback {
    constructor(public id?: number, public subject?: string, public description?: string, public rating?: number, public user?: IUser) {}
}
