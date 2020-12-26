import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {of, Subscription} from "rxjs";
import {GroupPermissionService} from "app/core/services/group-permission/group-permission.service";
import {PartServiceService} from "app/core/services/part-sercvice/part-service.service";
import {HeightService} from "app/shared/services/height.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {JhiEventManager} from "ng-jhipster";
import {ToastService} from "app/shared/services/toast.service";
import {TranslateService} from "@ngx-translate/core";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CommonService} from "app/shared/services/common.service";
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from "app/shared/constants/pagination.constants";
import {AddUserForPermissionComponent} from "app/modules/system-categories/group-permissions/add-user-for-permission/add-user-for-permission.component";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {AddWareHouseComponent} from "app/modules/system-categories/warehouse-resources/add-ware-house/add-ware-house.component";
import {WarehouseServicesService} from "app/core/services/warehouse/warehouse-services.service";
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";

@Component({
  selector: 'jhi-warehouse-resources',
  templateUrl: './warehouse-resources.component.html',
  styleUrls: ['./warehouse-resources.component.scss']
})
export class WarehouseResourcesComponent implements OnInit {

  token = '';
  form: FormGroup;
  groupPermissions: any = [];
  listStatus: any = [
    {name: '--Tất cả--', value: ''},
    {name: 'Hoạt động', value: 'ACTIVE'}, {
      name: 'Không hoạt động', value: 'INACTIVE'
    }]

  listProvince: any = [];
  partList: any[] = [];

  listRole = [];
  code: any = {code: '', name: '--Tất cả--'};
  status: any = {name: 'Tất cả', value: ''};
  routeData: any;
  height: any;
  height1: any;
  itemsPerPage: any;
  totalItems: any;
  maxSizePage: any;
  page: any;
  pagingParams
  previousPage: any;
  predicate: any;
  reverse: any;
  eventSubscriber: Subscription;
  listHistory: any;
  provinceID: any;
  checkBoll = false;

  constructor(
    private humanResourcesApiService: HumanResourcesApiService,
    private gpService: GroupPermissionService,
    private  partServiceService: PartServiceService,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private toastService: ToastService,
    private translateService: TranslateService,
    private modalService: NgbModal,
    private commonService: CommonService,
    private warehouseServicesService: WarehouseServicesService,
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.maxSizePage = MAX_SIZE_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      if (data && data.pagingParams) {
        this.page = data.pagingParams.page;
        this.previousPage = data.pagingParams.page;
        this.reverse = data.pagingParams.ascending;
        this.predicate = data.pagingParams.predicate;
      }
    });
  }

  ngOnInit() {
    this.buidForm();
    this.loadAll();
    this.onResize();
    this.registerChange();
    this.getListRole();
    this.getProvince();
    this.getPartList();

  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  changePageSize(size) {
    this.itemsPerPage = size;
    this.transition();
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
    this.height1 = this.heightService.onResizeHeight170Px();
  }

  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('groupPermissionsChange', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  onSearchData() {
    this.transition();
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  transition() {
    this.loadAll();
  }

  loadAll() {
    this.spinner.show();
    this.getAllGroupPermission()
  }

  getAllGroupPermission() {
    this.warehouseServicesService
      .searchWarehouse({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        code: this.form.get('code').value && this.form.get('code').value !== '--Tất cả--' ? this.form.get('code').value : '',
        status: this.form.get('status').value ? this.form.get('status').value : '',
        provinceID: this.form.get('provinceID').value ? this.form.get('provinceID').value : '',
        partname: this.form.get('partname').value ? this.form.get('partname').value : '',
        name: this.form.get('name').value ? this.form.get('name').value : '',
        fullName: this.form.get('fullName').value ? this.form.get('fullName').value : '',
        partId: this.form.get('partId').value ? this.form.get('partId').value : '',
        address: this.form.get('address').value ? this.form.get('address').value : '',
      })
      .subscribe(
        res => {
          this.spinner.hide();
          this.paginateGroupPermissionList(res);
        },
        err => {
          this.spinner.hide();
          this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
        }
      );
  }

  getAllHistory() {

  }

  // ngOnDestroy() {
  //   this.eventManager.destroy(this.eventSubscriber);
  // }

  onClearStatus() {
    this.setValueToField('status', '');
    this.loadAll();
  }

  convertDate(str) {
    if (str === null || str === '') {
      return "";
    } else {
      const date = new Date(str);
      return (date.getDate() < 10 ? ('0' +
        date.getDate()) : (date.getDate())) +
        '/' +
        (date.getMonth() < 9 ? ('0' +
          (date.getMonth() + 1)) : (date.getMonth() + 1)) +
        '/' +
        date.getFullYear();
      // return [date.getFullYear(), mnth, day].join('-');
    }
  }

  openModalAddUser(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddUserForPermissionComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.data = selectedData;
    modalRef.componentInstance.groupPermissions = this.listRole;
    modalRef.result.then(result => {
      this.transition();
    }).catch(() => {
      this.transition();
    });
  }

  permissionCheck(permission?: string) {
    return this.commonService.havePermission(permission);
  }

  openModalSaveGroupPermissions(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddWareHouseComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false,
    });
    modalRef.componentInstance.type = type;
    console.warn("111" + selectedData)
    modalRef.componentInstance.id = selectedData ? selectedData.wearhouseID : null;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });
  }

// xoa nhan su
  onDelete(data) {
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'delete';
    modalRef.componentInstance.param = 'kho';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.deleteW(data.wearhouseID);
      }
    });
  }

  deleteW(ids) {
    this.warehouseServicesService.deleteWarehouse(ids).subscribe(res => {
      this.spinner.show();
      if (res.data) {
        this.spinner.hide();
        this.toastService.openSuccessToast("Xóa kho thành công!");
        this.loadAll();
      }
    }, error => {
      this.spinner.hide();
      this.toastService.openErrorToast("Không thể xóa kho");
    });
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      code: ['--Tất cả--'],
      name: [],
      status: [],
      provinceID: [],
      partname: [],
      partId: [],
      fullName: [],
      address: [],
    });
  }

  private paginateGroupPermissionList(data) {
    this.groupPermissions = data.data;
    this.totalItems = data.dataCount;

  }

  private getListRole() {

  }

  // lay du lieu tinh thanh
  getProvince() {
    this.partServiceService.getProvince().subscribe(
      res => {
        if (res) {
          this.listProvince = res.data;
        } else {
          this.listProvince = [];
        }
      },
      err => {
        this.listProvince = [];
      }
    );
  }

  // lay du lieu bo phan
  getPartList() {
    this.humanResourcesApiService.getPartList().subscribe(
      res => {
        if (res) {
          this.partList = res.data;
        } else {
          this.partList = [];
        }
      },
      error => {
        this.partList = [];
      }
    );
  }


}

