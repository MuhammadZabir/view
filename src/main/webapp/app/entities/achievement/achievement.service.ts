import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAchievement } from 'app/shared/model/achievement.model';

type EntityResponseType = HttpResponse<IAchievement>;
type EntityArrayResponseType = HttpResponse<IAchievement[]>;

@Injectable({ providedIn: 'root' })
export class AchievementService {
    private resourceUrl = SERVER_API_URL + 'api/achievements';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/achievements';

    constructor(private http: HttpClient) {}

    create(achievement: IAchievement): Observable<EntityResponseType> {
        return this.http.post<IAchievement>(this.resourceUrl, achievement, { observe: 'response' });
    }

    update(achievement: IAchievement): Observable<EntityResponseType> {
        return this.http.put<IAchievement>(this.resourceUrl, achievement, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAchievement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAchievement[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAchievement[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
