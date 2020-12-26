import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ProjectManagementComponent} from "./project-management.component";
import {ListHandoverProductComponent} from "./list-handover-product/list-handover-product.component";
import {ProjectManagementRoutingModule} from "./project-management-routing.module";
import {PerfectScrollbarModule} from "ngx-perfect-scrollbar";
import {InvoiceWebappSharedModule} from "app/shared/shared.module";
import {ChartsModule} from "ng2-charts";
import {TreeViewModule} from "@syncfusion/ej2-angular-navigations";
import {AddHumanComponent} from "app/modules/system-categories/project-management/add-human/add-human.component";
import {RiskListComponent} from './risk-list/risk-list.component';
import {IssuesMemberComponent} from './issues-member/issues-member.component';
import {AddRiskComponent} from './risk-list/add-risk/add-risk.component';
import {MyEnterDirective} from './risk-list/my-enter.directive';
import {AddProjectComponent} from './add-project/add-project.component';
import { EditProjectComponent } from './edit-project/edit-project.component';
import { ConfirmPlanComponent } from './edit-project/confirm-plan/confirm-plan.component';
import { ChartKPIComponent } from './edit-project/chart-kpi/chart-kpi.component';
import { ViewProjectComponent } from './view-project/view-project.component';
import { RedmineLinkComponent } from './view-project/redmine-link/redmine-link.component';

@NgModule({
  declarations: [
    ProjectManagementComponent,
    ListHandoverProductComponent,
    AddHumanComponent,
    RiskListComponent,
    IssuesMemberComponent,
    AddRiskComponent,
    MyEnterDirective,
    AddProjectComponent,
    EditProjectComponent,
    ConfirmPlanComponent,
    ChartKPIComponent,
    ViewProjectComponent,
    RedmineLinkComponent
  ],
  exports: [
    ChartKPIComponent
  ],
  imports: [
    CommonModule,
    PerfectScrollbarModule,
    InvoiceWebappSharedModule,
    ChartsModule,
    TreeViewModule,
    ProjectManagementRoutingModule,
  ],
  entryComponents:[ConfirmPlanComponent]
})
export class ProjectManagementModule {
}
