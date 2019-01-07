import { Component, OnInit } from "@angular/core";
import { CompanyService } from '../../entities/company';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { ICompany } from 'app/shared/model/company.model';

@Component({
    selector: 'jhi-view-request',
    templateUrl: './request.component.html'
})
export class RequestComponent implements OnInit {

    companies: ICompany[];
    companyId: number;
    attachmentEvent;

    constructor(private companyService: CompanyService, private http: HttpClient) {}

    ngOnInit() {
        this.companyService.query().subscribe(
            (res: HttpResponse<ICompany[]>) => {
                this.companies = res.body;
            });
    }

    reset(element: HTMLInputElement) {
        element.value = '';
    }

    selectAttachment(event) {
        this.attachmentEvent = event;
    }

    request() {
        const formData = new FormData();
        formData.append("companyId", this.companyId.toString());
        formData.append("excel", this.attachmentEvent.target.files[0], this.attachmentEvent.target.files[0].name);
        this.http.post<ICompany>('api/request', formData, { observe: 'response' })
            .subscribe((res: any) => window.history.back());
    }
    
}