import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SystemCategoriesRoutingModule} from 'app/modules/system-categories/system-categories-routing.module';
import {InvoiceWebappSharedModule} from 'app/shared/shared.module';
import {PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {ChartsModule} from 'ng2-charts';
import {ConvertStatusPipe} from "app/shared/pipes/convert-status.pipe";
import {MaxLengthTextPipe} from "app/shared/pipes/max-length-text.pipe";
import {GroupPermissionsComponent} from './group-permissions/group-permissions.component';
import {SaveGroupPermissionComponent} from './group-permissions/save-group-permission/save-group-permission.component';
import {AddUserForPermissionComponent} from './group-permissions/add-user-for-permission/add-user-for-permission.component';
import {TreeViewModule} from '@syncfusion/ej2-angular-navigations';
import {AddHumanComponent} from './project-management/add-human/add-human.component';
import {AddPartnerComponent} from "./partner-management/add-partner/add-partner.component";
import {ProjectManagementModule} from "app/modules/system-categories/project-management/project-management.module";
import {IssuesMemberDetailComponent} from "app/modules/system-categories/project-management/issues-member/issues-member-detail/issues-member-detail.component";
import {ModalConfirmRiskComponent} from "app/modules/system-categories/project-management/risk-list/modal-confirm-risk/modal-confirm-risk.component";
import {ResourcesManagementModule} from './resources-management/resources-management.module';
import {ResourcesByTimeComponentModule} from './resources-by-time-component/resources-by-time-component.module';
// import {CustomerComponent} from "app/modules/system-categories/customer/customer.component";
import {ImportExcelHumanResourceComponent} from "app/modules/system-categories/human-resources/import-excel-human-resource/import-excel-human-resource.component";
import {HumanResourcesModule} from "app/modules/system-categories/human-resources/human-resources.module";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {PartnerManagementModule} from "app/modules/system-categories/partner-management/partner-management.module";
import {RedmineLinkComponent} from "app/modules/system-categories/project-management/view-project/redmine-link/redmine-link.component";
import {SupplierResourcesModule} from "app/modules/system-categories/supplier-resources/supplier-resources.module";
import {AssetResourcesModule} from "app/modules/system-categories/asset-resources/asset-resources.module";
import { AddSupplierResourceComponent } from './supplier-resources/add-supplier-resource/add-supplier-resource.component';
import { AddAssetResourcesComponent } from './asset-resources/add-asset-resources/add-asset-resources.component';
import {AssetResourcesComponent} from "app/modules/system-categories/asset-resources/asset-resources.component";
import {DepartmenResourcesModule} from "app/modules/system-categories/department-resources/departmen-resources.module";
import { AddGroupUserDepartmentResourcesComponent } from './add-group-user-department-resources/add-group-user-department-resources.component';
import { AddDepartmentResourcesComponent } from './add-department-resources/add-department-resources.component';
import { AddDepartmenResourceComponent } from './department-resources/add-departmen-resource/add-departmen-resource.component';
import {WarehouseResourcesModule} from "app/modules/system-categories/warehouse-resources/warehouse-resources.module";
import {RequestFormModule} from 'app/modules/system-categories/request-form/request-form.module';
import {ListDeviceModule} from "app/modules/system-categories/list-device/list-device.module";
import { AddWareHouseComponent } from './warehouse-resources/add-ware-house/add-ware-house.component';
import { QRcodeComponent } from './asset-resources/qrcode/qrcode.component';
import { ExportComponent } from './asset-resources/export/export.component';
import {NgxQRCodeModule} from "ngx-qrcode2";
@NgModule({
  declarations: [
    GroupPermissionsComponent,
    SaveGroupPermissionComponent,
    AddUserForPermissionComponent,
    // HumanResourcesComponent,
    IssuesMemberDetailComponent,
    ModalConfirmRiskComponent,
    ImportExcelHumanResourceComponent,
    // CustomerComponent,
    AddSupplierResourceComponent,
    AddAssetResourcesComponent,
    AddGroupUserDepartmentResourcesComponent,
    AddDepartmentResourcesComponent,
    AddDepartmenResourceComponent,
    AddWareHouseComponent,
    QRcodeComponent,
    ExportComponent,
  ],
  imports: [
    CommonModule,
    SystemCategoriesRoutingModule,
    PerfectScrollbarModule,
    InvoiceWebappSharedModule,
    ChartsModule,
    TreeViewModule,
    PartnerManagementModule,
    ProjectManagementModule,
    ResourcesManagementModule,
    ResourcesByTimeComponentModule,
    HumanResourcesModule,
    SupplierResourcesModule,
    AssetResourcesModule,
    DepartmenResourcesModule,
    WarehouseResourcesModule,
    NgxQRCodeModule,
    RequestFormModule,
    ListDeviceModule
  ],
  entryComponents: [
    SaveGroupPermissionComponent,
    AddUserForPermissionComponent,
    AddHumanComponent,
    AddPartnerComponent,
    IssuesMemberDetailComponent,
    ImportExcelHumanResourceComponent,
    ModalConfirmRiskComponent,
    RedmineLinkComponent,
    AddSupplierResourceComponent,
    AssetResourcesComponent,
    AddDepartmenResourceComponent,
    AddAssetResourcesComponent,
    AddWareHouseComponent,
    QRcodeComponent,
    ExportComponent,
  ],
  exports: [
  ],
  providers: [ConvertStatusPipe, MaxLengthTextPipe, NgbActiveModal]
})
export class SystemCategoriesModule {
}
