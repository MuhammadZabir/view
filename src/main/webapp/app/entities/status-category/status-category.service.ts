import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStatusCategory } from 'app/shared/model/status-category.model';

type EntityResponseType = HttpResponse<IStatusCategory>;
type EntityArrayResponseType = HttpResponse<IStatusCategory[]>;

@Injectable({ providedIn: 'root' })
export class StatusCategoryService {
    private resourceUrl = SERVER_API_URL + 'api/status-categories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/status-categories';

    constructor(private http: HttpClient) {}

    create(statusCategory: IStatusCategory): Observable<EntityResponseType> {
        return this.http.post<IStatusCategory>(this.resourceUrl, statusCategory, { observe: 'response' });
    }

    update(statusCategory: IStatusCategory): Observable<EntityResponseType> {
        return this.http.put<IStatusCategory>(this.resourceUrl, statusCategory, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IStatusCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStatusCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStatusCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
