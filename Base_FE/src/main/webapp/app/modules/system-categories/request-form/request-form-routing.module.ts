import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {RequestFormComponent} from './request-form.component'

import {JhiResolvePagingParams} from "ng-jhipster";

const routes: Routes = [{
  path: '',
  component: RequestFormComponent,
  canActivate: [],
  resolve: {
    pagingParams: JhiResolvePagingParams
  },
  data: {
    defaultSort: 'id,asc',
    pageTitle: 'organizationCategories.title',
    url: 'request-form'
  }
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RequestFormRoutingModule {
}
