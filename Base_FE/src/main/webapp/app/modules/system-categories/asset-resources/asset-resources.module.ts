import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AssetResourcesComponent} from "app/modules/system-categories/asset-resources/asset-resources.component";
import {InvoiceWebappSharedModule} from "app/shared/shared.module";



@NgModule({
  declarations: [
    AssetResourcesComponent,
  ],
    imports: [
        CommonModule,
        InvoiceWebappSharedModule
    ]
})
export class AssetResourcesModule { }
