import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAchievement } from 'app/shared/model/achievement.model';

@Component({
    selector: 'jhi-achievement-detail',
    templateUrl: './achievement-detail.component.html'
})
export class AchievementDetailComponent implements OnInit {
    achievement: IAchievement;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ achievement }) => {
            this.achievement = achievement;
        });
    }

    previousState() {
        window.history.back();
    }
}
