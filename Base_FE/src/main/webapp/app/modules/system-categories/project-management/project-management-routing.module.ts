import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {JhiResolvePagingParams} from "ng-jhipster";
// import {ProjectManagementComponent} from "./project-management.component";
import {ListHandoverProductComponent} from "./list-handover-product/list-handover-product.component";
import {AddHumanComponent} from "./add-human/add-human.component";
import {RiskListComponent} from "app/modules/system-categories/project-management/risk-list/risk-list.component";
import {IssuesMemberComponent} from "app/modules/system-categories/project-management/issues-member/issues-member.component";
import {AddRiskComponent} from "app/modules/system-categories/project-management/risk-list/add-risk/add-risk.component";
import {AddProjectComponent} from "app/modules/system-categories/project-management/add-project/add-project.component";
import {EditProjectComponent} from "app/modules/system-categories/project-management/edit-project/edit-project.component";
import {ViewProjectComponent} from "app/modules/system-categories/project-management/view-project/view-project.component";

const routes: Routes = [
  // {
  //   path: '',
  //   component: ProjectManagementComponent,
  //   canActivate: [],
  //   resolve: {
  //     pagingParams: JhiResolvePagingParams
  //   },
  //   data: {
  //     defaultSort: 'id,asc',
  //     pageTitle: 'organizationCategories.title',
  //     url: 'project-management'
  //   }
  // },
  {
    path: 'add-project',
    component: AddProjectComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'project-management/add-project'
    }
  },
  {
    path: 'edit-project',
    component: EditProjectComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'project-management/edit-project'
    }
  },
  {
    path: 'view-project',
    component: ViewProjectComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'project-management/view-project'
    }
  },
  {
    path: 'list-handover-product',
    component: ListHandoverProductComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'project-management/list-handover-product/:id'
    }
  },
  {
    path: 'add-human',
    component: AddHumanComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'project-management/add-human'
    }
  },
  {
    path: 'risk-list',
    component: RiskListComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'project-management/risk-list'
    }
  },
  {
    path: 'add-risk/:type',
    component: AddRiskComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/add-risk/:type'
    }
  },
  {
    path: 'issues-member',
    component: IssuesMemberComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'issues-member'
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectManagementRoutingModule {
}
