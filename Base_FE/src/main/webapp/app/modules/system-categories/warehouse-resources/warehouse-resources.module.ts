import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {InvoiceWebappSharedModule} from "app/shared/shared.module";
import {WarehouseResourcesComponent} from "app/modules/system-categories/warehouse-resources/warehouse-resources.component";
import {WarehouseResourcesRotingModule} from "app/modules/system-categories/warehouse-resources/warehouse-resources-roting.module";



@NgModule({
  declarations: [
    WarehouseResourcesComponent
  ],
  imports: [
    CommonModule,
    InvoiceWebappSharedModule,
    WarehouseResourcesRotingModule
  ]
})
export class WarehouseResourcesModule { }
