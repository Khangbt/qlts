import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {DepartmentResourcesComponent} from "app/modules/system-categories/department-resources/department-resources.component";
import {InvoiceWebappSharedModule} from "app/shared/shared.module";



@NgModule({
  declarations: [
    DepartmentResourcesComponent
  ],
  imports: [
    CommonModule,
    InvoiceWebappSharedModule
  ]
})
export class DepartmenResourcesModule { }
