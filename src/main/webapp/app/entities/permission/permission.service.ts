import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPermission } from 'app/shared/model/permission.model';

type EntityResponseType = HttpResponse<IPermission>;
type EntityArrayResponseType = HttpResponse<IPermission[]>;

@Injectable({ providedIn: 'root' })
export class PermissionService {
    private resourceUrl = SERVER_API_URL + 'api/permissions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/permissions';

    constructor(private http: HttpClient) {}

    create(permission: IPermission): Observable<EntityResponseType> {
        return this.http.post<IPermission>(this.resourceUrl, permission, { observe: 'response' });
    }

    update(permission: IPermission): Observable<EntityResponseType> {
        return this.http.put<IPermission>(this.resourceUrl, permission, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPermission>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPermission[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPermission[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
