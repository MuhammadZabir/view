import { IIssue } from '../../shared/model/issue.model';
import { IFeedback } from '../../shared/model/feedback.model';
import { ICommentIssue } from '../../shared/model/comment-issue.model';
import { IDepartment } from '../../shared/model/department.model';

export interface IUser {
    id?: any;
    login?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    activated?: boolean;
    langKey?: string;
    authorities?: any[];
    staffId?: string;
    issues: IIssue[];
    feedbacks: IFeedback[];
    commentIssues: ICommentIssue[];
    department: IDepartment,
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    password?: string;

}

export class User implements IUser {
    constructor(
        public id?: any,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public activated?: boolean,
        public langKey?: string,
        public authorities?: any[],
        public staffId?: string,
        public issues?: IIssue[],
        public feedbacks?: IFeedback[],
        public commentIssues?: ICommentIssue[],
        public department?: IDepartment,
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public password?: string
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authorities = authorities ? authorities : null;
        this.staffId = staffId ? staffId : null;
        this.issues = issues ? issues : null;
        this.feedbacks = feedbacks ? feedbacks : null;
        this.commentIssues = commentIssues ? commentIssues : null;
        this.department = department ? department : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
    }
}
