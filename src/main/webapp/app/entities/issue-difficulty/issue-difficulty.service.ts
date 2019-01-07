import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIssueDifficulty } from 'app/shared/model/issue-difficulty.model';

type EntityResponseType = HttpResponse<IIssueDifficulty>;
type EntityArrayResponseType = HttpResponse<IIssueDifficulty[]>;

@Injectable({ providedIn: 'root' })
export class IssueDifficultyService {
    private resourceUrl = SERVER_API_URL + 'api/issue-difficulties';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/issue-difficulties';

    constructor(private http: HttpClient) {}

    create(issueDifficulty: IIssueDifficulty): Observable<EntityResponseType> {
        return this.http.post<IIssueDifficulty>(this.resourceUrl, issueDifficulty, { observe: 'response' });
    }

    update(issueDifficulty: IIssueDifficulty): Observable<EntityResponseType> {
        return this.http.put<IIssueDifficulty>(this.resourceUrl, issueDifficulty, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IIssueDifficulty>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IIssueDifficulty[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IIssueDifficulty[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
