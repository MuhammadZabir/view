import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICriteria } from 'app/shared/model/criteria.model';

type EntityResponseType = HttpResponse<ICriteria>;
type EntityArrayResponseType = HttpResponse<ICriteria[]>;

@Injectable({ providedIn: 'root' })
export class CriteriaService {
    private resourceUrl = SERVER_API_URL + 'api/criteria';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/criteria';

    constructor(private http: HttpClient) {}

    create(criteria: ICriteria): Observable<EntityResponseType> {
        return this.http.post<ICriteria>(this.resourceUrl, criteria, { observe: 'response' });
    }

    update(criteria: ICriteria): Observable<EntityResponseType> {
        return this.http.put<ICriteria>(this.resourceUrl, criteria, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICriteria>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICriteria[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICriteria[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
