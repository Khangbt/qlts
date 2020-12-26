import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ResourcesManagementRoutingModule } from './resources-management-routing.module';
import { ResourcesManagementComponent } from './resources-management.component';
import {InvoiceWebappSharedModule} from "app/shared/shared.module";
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { ViewResourceProjectDetailComponent } from 'app/modules/system-categories/resources-management/view-resource-project-detail/view-resource-project-detail.component';


@NgModule({
  declarations: [
    ResourcesManagementComponent,
    ViewResourceProjectDetailComponent
  ],
  imports: [
    CommonModule,
    ResourcesManagementRoutingModule,
    InvoiceWebappSharedModule,
    PerfectScrollbarModule,
  ],
  entryComponents: [ViewResourceProjectDetailComponent],
})
export class ResourcesManagementModule { }
