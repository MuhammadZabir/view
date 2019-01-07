import { Moment } from 'moment';
import { ICommentIssue } from './comment-issue.model';
import { IUser } from '../../core/user/user.model';

export interface IIssue {
    id?: number;
    name?: string;
    type?: string;
    category?: string;
    durationStart?: Moment;
    expectedDurationEnd?: Moment;
    durationEnd?: Moment;
    description?: string;
    status?: string;
    commentIssues?: ICommentIssue[];
    difficulty?: string;
    user?: IUser;
}

export class Issue implements IIssue {
    constructor(
        public id?: number,
        public name?: string,
        public type?: string,
        public category?: string,
        public durationStart?: Moment,
        public expectedDurationEnd?: Moment,
        public durationEnd?: Moment,
        public description?: string,
        public status?: string,
        public commentIssues?: ICommentIssue[],
        public difficulty?: string,
        public user?: IUser
    ) {}
}
