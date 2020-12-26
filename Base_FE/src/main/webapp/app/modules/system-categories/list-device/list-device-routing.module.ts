import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {ListDeviceComponent} from "./list-device.component";
import {JhiResolvePagingParams} from "ng-jhipster";

const routes: Routes = [{
  path: '',
  component: ListDeviceComponent,
  canActivate: [],
  resolve: {
    pagingParams: JhiResolvePagingParams
  },
  data: {
    defaultSort: 'id,asc',
    pageTitle: 'organizationCategories.title',
    url: 'list-device'
  }
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ListDeviceRoutingModule {
}
