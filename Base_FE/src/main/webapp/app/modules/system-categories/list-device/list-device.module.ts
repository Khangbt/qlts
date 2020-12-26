import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {InvoiceWebappSharedModule} from "app/shared/shared.module";
import {ListDeviceComponent} from "app/modules/system-categories/list-device/list-device.component";
import {ListDeviceRoutingModule} from "./list-device-routing.module";
import {AddListDeviceComponent} from "app/modules/system-categories/list-device/add-list-device/add-list-device.component";

@NgModule({
  declarations: [
    ListDeviceComponent,
    AddListDeviceComponent
  ],
  imports: [
    CommonModule,
    InvoiceWebappSharedModule,
    ListDeviceRoutingModule,
  ],
  entryComponents:[
    AddListDeviceComponent
  ]
})
export class ListDeviceModule {
}
