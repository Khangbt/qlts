import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RequestFormRoutingModule} from './request-form-routing.module'
import {RequestFormComponent} from './request-form.component'
import {InvoiceWebappSharedModule} from "app/shared/shared.module";
import {AddRequestFormBorrowComponent} from "app/modules/system-categories/request-form/add-request-form-borrow/add-request-form-borrow.component";
import {AddRequestFormBuyComponent} from "app/modules/system-categories/request-form/add-request-form-buy/add-request-form-buy.component";
import {AddRequestFormReturnComponent} from "app/modules/system-categories/request-form/add-request-form-return/add-request-form-return.component";

@NgModule({
  declarations: [
    RequestFormComponent,
    AddRequestFormBorrowComponent,
    AddRequestFormBuyComponent,
    AddRequestFormReturnComponent,
  ],
    imports: [
        CommonModule,
        RequestFormRoutingModule,
        InvoiceWebappSharedModule,
    ],
  entryComponents:[
    AddRequestFormBorrowComponent,
    AddRequestFormBuyComponent,
    AddRequestFormReturnComponent,
  ]
})
export class RequestFormModule { }
