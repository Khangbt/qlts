import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
// import { JhiResolvePagingParams } from 'ng-jhipster';
// import { ResourcesManagementComponent } from './resources-management.component';


const routes: Routes = [
  // {
  //   path: '',
  //   // component: ChartComponent,
  //   component: ResourcesManagementComponent,
  //   resolve: {
  //     pagingParams: JhiResolvePagingParams
  //   },
  //   data: {
  //     defaultSort: 'id,asc',
  //     pageTitle: 'organizationCategories.title',
  //     url: 'system-categories/resources-management'
  //   }
  // }
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ResourcesManagementRoutingModule { }
