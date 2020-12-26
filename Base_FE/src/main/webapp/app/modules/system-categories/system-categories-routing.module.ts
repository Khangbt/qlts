import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { HumanResourcesComponent } from "app/modules/system-categories/human-resources/human-resources.component";
import { GroupPermissionsComponent } from "app/modules/system-categories/group-permissions/group-permissions.component";
import { SaveGroupPermissionComponent } from "app/modules/system-categories/group-permissions/save-group-permission/save-group-permission.component";
import { AddHumanComponent } from "app/modules/system-categories/project-management/add-human/add-human.component";
import {SupplierResourcesComponent} from "app/modules/system-categories/supplier-resources/supplier-resources.component";
import {AssetResourcesComponent} from "app/modules/system-categories/asset-resources/asset-resources.component";
import {DepartmentResourcesComponent} from "app/modules/system-categories/department-resources/department-resources.component";
import {WarehouseResourcesComponent} from "app/modules/system-categories/warehouse-resources/warehouse-resources.component";

const routes: Routes = [
  {
    path: 'supplier-resources',
    component: SupplierResourcesComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/supplier-resources'
    }
  },
  {
    path: 'asset-resources',
    component: AssetResourcesComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/asset-resources'
    }
  },

  {
    path: 'department-resources',
    component: DepartmentResourcesComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/department-resources'
    }
  },
  {
    path: 'warehouse-resources',
    component: WarehouseResourcesComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/warehouse-resources'
    }
  },
  {
    path: 'human-resources',
    component: HumanResourcesComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/human-resources'
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
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/add-human'
    }
  },
  {
    path: 'group-permissions',
    component: GroupPermissionsComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/group-permissions'
    }
  },
  {
    path: 'group-permissions/:type',
    component: SaveGroupPermissionComponent,
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/group-permissions/:type'
    }
  },
  {
    path: 'partner-management',
    loadChildren: () => import('./partner-management/partner-management.module').then(m => m.PartnerManagementModule),
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/partmer-management'
    }
  },
  {
    path: 'project-management',
    loadChildren: () => import('./project-management/project-management.module').then(m => m.ProjectManagementModule),
    canActivate: [],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/project-management'
    }
  },

  {
    path: 'resources-management',
    loadChildren: () => import('./resources-management/resources-management.module').then(m => m.ResourcesManagementModule),
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/resources-management'
    }
  },
  {
    path: 'resources-human',
    loadChildren: () => import('./resources-by-time-component/resources-by-time-component.module').then(m => m.ResourcesByTimeComponentModule),
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/resources-human'
    }
  },
  {
    path: 'human-management',
    loadChildren: () => import('./human-resources/human-resources.module').then(m => m.HumanResourcesModule),
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/human-resources'
    }
  },

  {
    path: 'request-form',
    loadChildren: () => import('./request-form/request-form.module').then(m => m.RequestFormModule),
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',

      url: 'system-categories/request-form'
    }
  },
  {
    path: 'list-device',
    loadChildren: () => import('./list-device/list-device.module').then(m => m.ListDeviceModule),
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',

      url: 'system-categories/list-device'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SystemCategoriesRoutingModule {
}
