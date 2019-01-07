import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPointConfig } from 'app/shared/model/point-config.model';

type EntityResponseType = HttpResponse<IPointConfig>;
type EntityArrayResponseType = HttpResponse<IPointConfig[]>;

@Injectable({ providedIn: 'root' })
export class PointConfigService {
    private resourceUrl = SERVER_API_URL + 'api/point-configs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/point-configs';

    constructor(private http: HttpClient) {}

    create(pointConfig: IPointConfig): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pointConfig);
        return this.http
            .post<IPointConfig>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    update(pointConfig: IPointConfig): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pointConfig);
        return this.http
            .put<IPointConfig>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPointConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPointConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPointConfig[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res));
    }

    private convertDateFromClient(pointConfig: IPointConfig): IPointConfig {
        const copy: IPointConfig = Object.assign({}, pointConfig, {
            startDate: pointConfig.startDate != null && pointConfig.startDate.isValid() ? pointConfig.startDate.toJSON() : null,
            endDate: pointConfig.endDate != null && pointConfig.endDate.isValid() ? pointConfig.endDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
        res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pointConfig: IPointConfig) => {
            pointConfig.startDate = pointConfig.startDate != null ? moment(pointConfig.startDate) : null;
            pointConfig.endDate = pointConfig.endDate != null ? moment(pointConfig.endDate) : null;
        });
        return res;
    }
}
