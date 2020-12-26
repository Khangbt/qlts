import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {of, Subscription} from "rxjs";
import {GroupPermissionService} from "app/core/services/group-permission/group-permission.service";
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
import {PartServiceService} from "app/core/services/part-sercvice/part-service.service";
import {AddDepartmenResourceComponent} from "app/modules/system-categories/department-resources/add-departmen-resource/add-departmen-resource.component";

@Component({
  selector: 'jhi-department-resources',
  templateUrl: './department-resources.component.html',
  styleUrls: ['./department-resources.component.scss']
})
export class DepartmentResourcesComponent implements OnInit {

  token = '';
  form: FormGroup;
  groupPermissions: any = [];
  listStatus: any = [
    {name: '--Tất cả--', value: ''},
    {name: 'Hoạt động', value: 'ACTIVE'}, {
      name: 'Không hoạt động', value: 'INACTIVE'
    }]

  listProvince: any = [];

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

  constructor(
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
    private commonService: CommonService
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
    this.getProvince();
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
    console.warn("vào day 123")
    this.getAllGroupPermission();
    this.spinner.hide();

  }

  getAllGroupPermission() {
    this.partServiceService
      .searchPart({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        code: this.form.get('code').value && this.form.get('code').value !== '--Tất cả--' ? this.form.get('code').value : '',
        status: this.form.get('status').value ? this.form.get('status').value : '',
        partName: this.form.get('partName').value ? this.form.get('partName').value : '',
        name: this.form.get('name').value ? this.form.get('name').value : ''

      })
      .subscribe(
        res => {
          this.spinner.hide();
          this.paginateGroupPermissionList(res);
        },
        err => {
          this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
        }
      );
  }



  onClearStatus() {
    this.setValueToField('status', '');
    this.loadAll();
  }



  openModalAddUser(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddUserForPermissionComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.id = selectedData.id;

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

  openModalSave(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddDepartmenResourceComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false,
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.id = selectedData ? selectedData.id : null;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });
  }

  onDelete(ids) {

    if(ids!==null){
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'delete';
        modalRef.componentInstance.param = 'nhóm quyền';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.onSubmitDelete(ids);
          }
        });
      } else {
        this.toastService.openErrorToast("Xóa không thành công");
      }

  }

  onSubmitDelete(id) {
    this.spinner.show();
    this.partServiceService.delete(id).subscribe(
      res => {
        this.spinner.hide();
        this.toastService.openSuccessToast('Xóa bộ phận thành công');
        this.loadAll();
      },
      err => {

        this.spinner.hide();
        this.toastService.openErrorToast('Xóa bộ phận không thành công');
      }
    );
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      code: ['--Tất cả--'],
      name: [],
      status: [],
      partName: [],
    });
  }

  private paginateGroupPermissionList(data) {
    this.groupPermissions = data.data;
    this.totalItems = data.dataCount;

  }



  // lay du lieu tinh thanh
  getProvince() {
  }
}
