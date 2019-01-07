import { IUser } from '../../core/user/user.model';
import { IIssue } from './issue.model';

export interface ICommentIssue {
    id?: number;
    comment?: string;
    user?: IUser;
    issue?: IIssue;
}

export class CommentIssue implements ICommentIssue {
    constructor(public id?: number, public comment?: string, public user?: IUser, public issue?: IIssue) {}
}
