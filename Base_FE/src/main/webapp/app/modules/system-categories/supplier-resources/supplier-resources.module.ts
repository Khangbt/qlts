import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SupplierResourcesComponent} from "app/modules/system-categories/supplier-resources/supplier-resources.component";
import {InvoiceWebappSharedModule} from "app/shared/shared.module";



@NgModule({
  declarations: [
    SupplierResourcesComponent,
  ],
  imports: [
    CommonModule,
    InvoiceWebappSharedModule
  ]
})
export class SupplierResourcesModule { }
