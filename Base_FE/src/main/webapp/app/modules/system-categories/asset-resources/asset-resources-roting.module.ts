import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {JhiResolvePagingParams} from "ng-jhipster";
import {AssetResourcesComponent} from "app/modules/system-categories/asset-resources/asset-resources.component";


const routes: Routes = [
  {
    path: '',
    component: AssetResourcesComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'asset-resources'
    }
  },



]
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports:[RouterModule]
})
export class AssetResourcesRotingModule { }
