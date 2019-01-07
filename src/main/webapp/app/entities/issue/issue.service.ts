import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIssue } from 'app/shared/model/issue.model';

type EntityResponseType = HttpResponse<IIssue>;
type EntityArrayResponseType = HttpResponse<IIssue[]>;

@Injectable({ providedIn: 'root' })
export class IssueService {
    private resourceUrl = SERVER_API_URL + 'api/issues';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/issues';

    constructor(private http: HttpClient) {}

    create(issue: IIssue): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(issue);
        return this.http
            .post<IIssue>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    update(issue: IIssue): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(issue);
        return this.http
            .put<IIssue>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IIssue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IIssue[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IIssue[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res));
    }

    private convertDateFromClient(issue: IIssue): IIssue {
        const copy: IIssue = Object.assign({}, issue, {
            durationStart: issue.durationStart != null && issue.durationStart.isValid() ? issue.durationStart.toJSON() : null,
            expectedDurationEnd:
                issue.expectedDurationEnd != null && issue.expectedDurationEnd.isValid() ? issue.expectedDurationEnd.toJSON() : null,
            durationEnd: issue.durationEnd != null && issue.durationEnd.isValid() ? issue.durationEnd.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.durationStart = res.body.durationStart != null ? moment(res.body.durationStart) : null;
        res.body.expectedDurationEnd = res.body.expectedDurationEnd != null ? moment(res.body.expectedDurationEnd) : null;
        res.body.durationEnd = res.body.durationEnd != null ? moment(res.body.durationEnd) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((issue: IIssue) => {
            issue.durationStart = issue.durationStart != null ? moment(issue.durationStart) : null;
            issue.expectedDurationEnd = issue.expectedDurationEnd != null ? moment(issue.expectedDurationEnd) : null;
            issue.durationEnd = issue.durationEnd != null ? moment(issue.durationEnd) : null;
        });
        return res;
    }
}
