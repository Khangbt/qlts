import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PerfectScrollbarModule} from "ngx-perfect-scrollbar";
import {InvoiceWebappSharedModule} from "app/shared/shared.module";
import {ChartsModule} from "ng2-charts";
import {TreeViewModule} from "@syncfusion/ej2-angular-navigations";
import {AddPartnerComponent} from "app/modules/system-categories/partner-management/add-partner/add-partner.component";
import {PartnerManagementComponent} from "app/modules/system-categories/partner-management/partner-management.component";
import {PartnerManagementRoutingModule} from "app/modules/system-categories/partner-management/partner-management-routing.module";

@NgModule({
  declarations: [
    PartnerManagementComponent,
    AddPartnerComponent,
  ],
  imports: [
    CommonModule,
    PerfectScrollbarModule,
    InvoiceWebappSharedModule,
    ChartsModule,
    TreeViewModule,
    PartnerManagementRoutingModule,
  ]
})
export class PartnerManagementModule {
}
