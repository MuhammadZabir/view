import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ViewCompanyModule } from './company/company.module';
import { ViewDepartmentModule } from './department/department.module';
import { ViewCriteriaModule } from './criteria/criteria.module';
import { ViewAchievementModule } from './achievement/achievement.module';
import { ViewIssueModule } from './issue/issue.module';
import { ViewCommentIssueModule } from './comment-issue/comment-issue.module';
import { ViewFeedbackModule } from './feedback/feedback.module';
import { ViewIssueDifficultyModule } from './issue-difficulty/issue-difficulty.module';
import { ViewPermissionModule } from './permission/permission.module';
import { ViewPointConfigModule } from './point-config/point-config.module';
import { ViewRoleModule } from './role/role.module';
import { ViewStatusCategoryModule } from './status-category/status-category.module';
import { ViewRequestModule } from './request/request.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ViewCompanyModule,
        ViewDepartmentModule,
        ViewCriteriaModule,
        ViewAchievementModule,
        ViewIssueModule,
        ViewCommentIssueModule,
        ViewFeedbackModule,
        ViewIssueDifficultyModule,
        ViewPermissionModule,
        ViewPointConfigModule,
        ViewRoleModule,
        ViewStatusCategoryModule,
        ViewRequestModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ViewEntityModule {}
