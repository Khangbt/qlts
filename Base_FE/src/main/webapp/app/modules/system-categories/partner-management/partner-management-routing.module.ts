import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {JhiResolvePagingParams} from "ng-jhipster";
import {AddPartnerComponent} from "app/modules/system-categories/partner-management/add-partner/add-partner.component";
// import {PartnerManagementComponent} from "app/modules/system-categories/partner-management/partner-management.component";


const routes: Routes = [
  // {
  //   path: '',
  //   component: PartnerManagementComponent,
  //   canActivate: [],
  //   resolve: {
  //     pagingParams: JhiResolvePagingParams
  //   },
  //   data: {
  //     defaultSort: 'id,asc',
  //     pageTitle: 'organizationCategories.title',
  //     url: 'partner-management'
  //   }
  // },

  {
    path: 'add-partner',
    component: AddPartnerComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'partner-management/add-partner'
    }
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PartnerManagementRoutingModule {
}
