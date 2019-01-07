import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICommentIssue } from 'app/shared/model/comment-issue.model';

type EntityResponseType = HttpResponse<ICommentIssue>;
type EntityArrayResponseType = HttpResponse<ICommentIssue[]>;

@Injectable({ providedIn: 'root' })
export class CommentIssueService {
    private resourceUrl = SERVER_API_URL + 'api/comment-issues';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/comment-issues';

    constructor(private http: HttpClient) {}

    create(commentIssue: ICommentIssue): Observable<EntityResponseType> {
        return this.http.post<ICommentIssue>(this.resourceUrl, commentIssue, { observe: 'response' });
    }

    update(commentIssue: ICommentIssue): Observable<EntityResponseType> {
        return this.http.put<ICommentIssue>(this.resourceUrl, commentIssue, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICommentIssue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICommentIssue[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICommentIssue[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
