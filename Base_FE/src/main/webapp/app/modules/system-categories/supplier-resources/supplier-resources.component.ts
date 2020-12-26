import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Observable, of, Subject, Subscription} from "rxjs";
import {HumanResouces} from "app/core/models/human-resources/human-resouces.model";
import {OrganizationCategoriesService} from "app/core/services/system-management/organization-categories.service";
import {HeightService} from "app/shared/services/height.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NgxSpinnerService} from "ngx-spinner";
import {JhiEventManager} from "ng-jhipster";
import {ToastService} from "app/shared/services/toast.service";
import {CommonService} from "app/shared/services/common.service";
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from "app/shared/constants/pagination.constants";
import {debounceTime} from "rxjs/operators";
import {TIME_OUT} from "app/shared/constants/set-timeout.constants";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {ImportExcelHumanResourceComponent} from "app/modules/system-categories/human-resources/import-excel-human-resource/import-excel-human-resource.component";
import {AddHumanResourcesComponent} from "app/modules/system-categories/human-resources/add-human-resources/add-human-resources.component";
import {REGEX_REPLACE_PATTERN} from "app/shared/constants/pattern.constants";
import {SHOW_HIDE_COL_HEIGHT} from 'app/shared/constants/perfect-scroll-height.constants';
import {AddSupplierResourceComponent} from "app/modules/system-categories/supplier-resources/add-supplier-resource/add-supplier-resource.component";
import {SupplierServieceService} from "app/core/services/Supplier-service/supplier-serviece.service";

@Component({
  selector: 'jhi-supplier-resources',
  templateUrl: './supplier-resources.component.html',
  styleUrls: ['./supplier-resources.component.scss']
})
export class SupplierResourcesComponent implements OnInit {

  form: FormGroup;
  height: number;
  itemsPerPage: any;
  maxSizePage: any;
  routeData: any;
  page: number;
  second: any;
  totalItems: any;
  previousPage: any;
  predicate: any;
  reverse: any;
  userList: any;
  formValue;
  eventSubscriber: Subscription;
  listUnit$ = new Observable<any[]>();
  unitSearch;
  listNCC = [];

  debouncer: Subject<string> = new Subject<string>();
  positionList: any[] = [];
  cities = [
    {id: 1, name: 'Đang làm việc'},
    {id: 2, name: 'Đã thôi việc'},

  ];
  searchForm: any;
  SHOW_HIDE_COL_HEIGHT = SHOW_HIDE_COL_HEIGHT;
  listHumanResourcse: HumanResouces[] = [];
  columns = [
    {key: 0, value: "Mã nhà cung cấp", isShow: true},
    {key: 1, value: "Tên nhà cung cấp", isShow: true},
    {key: 2, value: "Người liên hệ", isShow: true},
    {key: 3, value: "Fax", isShow: false},
    {key: 4, value: "Email", isShow: true},
    {key: 5, value: "Số điện thoại", isShow: true},
    {key: 6, value: "Địa chỉ", isShow: true},
    {key: 7, value: "Website", isShow: false},
    {key: 8, value: "Ghi chú", isShow: false},

  ];

  centerList: any[] = [];
  listHuman = new Observable<any[]>();
  searchHuman;
  debouncer5: Subject<string> = new Subject<string>();
  partList: any[] = [];
  departmentList: any[] = [];
  majorList: any[] = [];
  active = 1;
  user = JSON.parse(localStorage.getItem('user'));
  majorId = [1, 2];
  lstHumanResourcesId: any;
  historyList: any;

  constructor(
    private supplierServieceService: SupplierServieceService,
    private organizationCategoriesService: OrganizationCategoriesService,
    private heightService: HeightService,
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private humanResourcesApiService: HumanResourcesApiService,
    private modalService: NgbModal,
    protected router: Router,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private toastService: ToastService,
    private commonService: CommonService,
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
    this.searchForm = {};
    this.getPartList();
    this.onResize();
    this.loadAll();
    this.registerChange();
    this.getPositionList();
    this.debounceOnSearch5();
    this.getDeparmentList();
    this.getMajorList();
    this.getSeverityList()
  }

  permissionCheck(permission?: string) {
    return this.commonService.havePermission(permission);
  }


  toggleColumns(col) {
    col.isShow = !col.isShow;
  }

  // search theo ma nhan su
  onSearHuman(event) {
    this.searchHuman = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer5.next(term);
    } else {
      this.listHuman = of([]);
    }
  }

  debounceOnSearch5() {
    this.debouncer5.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit5(value));
  }


  loadDataOnSearchUnit5(term) {
    const data = {
      keySearch: term.trim().toUpperCase(),
      type: 'PARTNER'
    }
    this.humanResourcesApiService.getHumanResourcesInfo(data).subscribe(res => {
      // this.organizationCategoriesService.getPartnerInfo(data).subscribe(res => {
      if (this.searchHuman) {
        const dataRes: any = res;

        this.listHuman = of(dataRes.sort((a, b) => a.fullName.localeCompare(b.fullName)));
        // this.listHuman = of(dataRes.sort((a, b) => a.code.localeCompare(b.code)));

      } else {
        this.listHuman = of([]);
      }
    });
  }

  onClearUnit5() {
    this.listHuman = of([]);
    this.searchHuman = '';
  }

  onClearHuman() {
    this.listHuman = of([]);
    this.searchHuman = '';
  }

  onSearchHuman() {
    if (!this.form.value.parentName) {
      this.listHuman = of([]);
      this.searchHuman = '';
    }
  }

  customSearchHunan(term: string, item: any): any {
    term = term.toLocaleLowerCase().trim();
    return (item.fullName ? item.fullName.toLocaleLowerCase().indexOf(term) > -1 : ''.indexOf(term))
      || (item.code ? item.code.toLocaleLowerCase().indexOf(term) > -1 : ''.indexOf(term))
      || (item.email ? item.email.toLocaleLowerCase().indexOf(term) > -1 : ''.indexOf(term));
    // || item.code.toLocaleLowerCase().indexOf(term) > -1
    // || item.email.toLocaleLowerCase().indexOf(term) > -1;
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

  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }

  // lây du lieu phong ban
  getDeparmentList() {
    this.humanResourcesApiService.getDepartmentList().subscribe(
      res => {
        if (res) {
          this.departmentList = res.data;
        } else {
          this.departmentList = [];
        }
      },
      err => {
        this.departmentList = [];
      }
    );
  }

  // lay du lieu chuc danh
  getPositionList() {
    this.humanResourcesApiService.getPositionList().subscribe(
      res => {
        if (res) {
          this.positionList = res.data;
        } else {
          this.positionList = [];
        }
      },
      err => {
        this.positionList = [];
      }
    );
  }

  // lay chuyen môn
  getMajorList() {
    this.humanResourcesApiService.getMajorList().subscribe(
      res => {
        if (res) {
          this.majorList = res.data;
        } else {
          this.majorList = [];
        }
      },
      error => {
        this.majorList = [];
      }
    );
  }


// loa du lieu bang
  loadAll() {
    this.spinner.show();

    this.searchForm.nameOrCode = this.form.value.nameOrCode;
    this.searchForm.fullName = this.form.value.fullName;
    this.searchForm.phone = this.form.value.phone;
    this.searchForm.address = this.form.value.address;
    this.searchForm.page = this.page;
    this.searchForm.pageSize = this.itemsPerPage;
    this.supplierServieceService

      .searchSupplier(
        this.searchForm
      )
      .subscribe(
        res => {
          this.spinner.hide();
          this.paginateUserList(res);
        },
        err => {
          this.spinner.hide();
          // this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
          this.toastService.openErrorToast("loi");
        }
      );
  }

  private paginateUserList(res) {
    this.totalItems = res.dataCount;
    this.listHumanResourcse = res.data;
  }

  get formControl() {
    return this.form.controls;
  }

  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('HumanResourcesChange', response => this.loadAll());
  }

  // setValueOfForm(formValue) {
  //   this.formValue = formValue;
  // }

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

  onSearchData() {
    this.transition();
  }

  transition() {
    this.router.navigate(['/system-categories/supplier-resources'], {
      queryParams: {}
    });
    this.loadAll();
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

//////////////////////////////////////////
  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  onChangePosition1(event) {
    this.active = event.id;
  }


// xoa nhan su
  deleteHumanResource(data) {
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'delete';
    modalRef.componentInstance.param = 'nhà cung cấp';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.deleteHumanResource1(data.supplierId);
      }
    });
  }

  deleteHumanResource1(ids) {
    this.supplierServieceService.deleteHumanResources(ids).subscribe(res => {
      this.spinner.show();
      if (res.data) {
        this.spinner.hide();
        this.toastService.openSuccessToast("Xóa nhà cung cấp thành công!");
        this.loadAll();
      }
    }, error => {
      this.spinner.hide();
      // this.toastService.openErrorToast(this.translateService.instant('user.invalidDelete'));
      // this.toastService.openErrorToast("Xóa nhân sự thất bại, có rằng buộc dữ liệu");
      this.toastService.openErrorToast("Nhà cung  đang chờ giao hàng");
    });
  }





// resetpassword
  resetpassword(item) {
    this.spinner.show();
    this.humanResourcesApiService.resetpassword(item.humanResourceId).subscribe(res => {
      if (res.data) {
        this.toastService.openSuccessToast('Mật khẩu mới đã được gửi tới email ' + item.email + ", xin vui lòng check email!");
        this.spinner.hide();
      } else {
        this.spinner.hide();
        // this.toastService.openErrorToast(this.translateService.instant('user.invalidDelete'));
        this.toastService.openErrorToast('user.invalidDelete');
      }
    });
  }


  private buidForm() {
    this.form = this.formBuilder.group({
      centerId: [],
      active: [],
      code: [''],
      positionName: [null],
      partId: [],
      departmentId: [],
      experience: [],
      lstMajorId: [],
      lstHumanResourcesId: [],

      humanResourceId: [],
      fullName: [],
      groupSupplierid: [],
      phone: [],
      positionId: [],
      address: [],
      nameOrCode:[]
    });
  }

  importExcel() {
    const modalRef = this.modalService.open(ImportExcelHumanResourceComponent, {
      size: 'xl',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = "import";
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });
  }

  getValueOfField(item) {
    return this.form.get(item).value;
  }

  trimSpace(element) {
    const value = this.getValueOfField(element);
    if (value) {
      this.setValueToField(element, value.trim());
    }
  }

  onClick() {
    const modalRef = this.modalService.open(AddHumanResourcesComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false,
      windowClass: 'myCustomModalClass'
    });
    modalRef.componentInstance.type = 'add';
    modalRef.componentInstance.data = {};
    modalRef.componentInstance.onCloseModal.subscribe(value => {
    });
  }


  convertJson(value) {
    if (value) {
      return JSON.parse(value);
    }
  }

  getDataShow(item) {
    const newValue = JSON.parse(item.valueNew);
    const oldValue = JSON.parse(item.valueOld);
    let result = '';
    if (newValue) {
      if (newValue.code) {
        result += newValue.code + " - ";
      }
      if (newValue.fullName) {
        result += newValue.fullName;
      }
    } else if (oldValue) {
      if (oldValue.code) {
        result += oldValue.code + " - ";
      }
      if (oldValue.fullName) {
        result += oldValue.fullName;
      }
    }
    return result;
  }

  convertDate(str) {
    if (str === null || str === '') {
      return "";
    } else {
      const date = new Date(str);
      return (date.getDate() < 11 ? ('0' +
        date.getDate()) : (date.getDate())) +
        '/' +
        (date.getMonth() < 9 ? ('0' +
          (date.getMonth() + 1)) : (date.getMonth() + 1)) +
        '/' +
        date.getFullYear();
      // return [date.getFullYear(), mnth, day].join('-');
    }
  }

  onKeyInput(event) {
    const value = this.getValueOfField('phone');
    let valueChange = event.target.value;
    const parseStr = valueChange.split('');
    if (parseStr[0] === '0') {
      valueChange = valueChange.replace('0', '');
    } else {
      valueChange = valueChange.replace(REGEX_REPLACE_PATTERN.INTEGER, '');
    }
    if (value !== valueChange) {
      this.setValueToField('phone', valueChange);
      return false;
    }
  }


  /*duc*/
  openModalAddUser(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddHumanResourcesComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.id = selectedData ? selectedData.supplierId : null;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });

  }


  getSeverityList() {
    this.humanResourcesApiService.getByParType('NCC').subscribe(
      res => {
        const d: any = res;
        this.listNCC = d.data;
      },
      err => {
        this.listNCC = []
      }
    )
  }

  openAddHuman(type, selectedData) {
    const modalRef = this.modalService.open(AddSupplierResourceComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false,
    });
    modalRef.componentInstance.type = type;
    console.warn(selectedData)
    modalRef.componentInstance.id = null != selectedData ? selectedData.supplierId : null;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });

  }


}

