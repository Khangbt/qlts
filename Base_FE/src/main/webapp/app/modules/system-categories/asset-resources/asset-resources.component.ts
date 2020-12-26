import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Observable, of, Subject, Subscription} from "rxjs";
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
import {REGEX_REPLACE_PATTERN} from "app/shared/constants/pattern.constants";
import {SHOW_HIDE_COL_HEIGHT} from "app/shared/constants/perfect-scroll-height.constants";
import {AddAssetResourcesComponent} from "app/modules/system-categories/asset-resources/add-asset-resources/add-asset-resources.component";
import {AsetServiceService} from "app/core/services/device-api/aset-service.service";
import {AssetModel} from "app/core/models/asset-model/asset-model";
import {QRcodeComponent} from "app/modules/system-categories/asset-resources/qrcode/qrcode.component";
import {DeviceServiceService} from "app/core/services/device-api/device-service.service";
import {WarehouseServicesService} from "app/core/services/warehouse/warehouse-services.service";
import {SupplierServieceService} from "app/core/services/Supplier-service/supplier-serviece.service";
import {ListDeviceService} from "app/core/services/list-device/list-device.service";
import {DownloadService} from "app/shared/services/download.service";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";

@Component({
  selector: 'jhi-asset-resources',
  templateUrl: './asset-resources.component.html',
  styleUrls: ['./asset-resources.component.scss']
})
export class AssetResourcesComponent implements OnInit {


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
  debouncer: Subject<string> = new Subject<string>();
  positionList: any[] = [];
  cities = [
    {id: 1, name: 'Đang làm việc'},
    {id: 2, name: 'Đã thôi việc'},

  ];
  listGroup: [];
  searchForm: any;
  SHOW_HIDE_COL_HEIGHT = SHOW_HIDE_COL_HEIGHT;
  listAsset = [];
  columns = [
    {key: 0, value: "Mã số thiết bị", isShow: true},
    {key: 1, value: "Tên thiết bị", isShow: true},
    {key: 2, value: "Phòng ban đang sở hữu", isShow: true},
    {key: 3, value: "đơn vị", isShow: false},
    {key: 4, value: "Số lượng", isShow: true},
    {key: 5, value: "Người đang sử dụng", isShow: true},
    {key: 6, value: "Tình trạng thiết bị", isShow: true},
    {key: 7, value: "Nhà cung cấp", isShow: false},
    {key: 8, value: "Kho chứa", isShow: false},
    {key: 9, value: "Phiếu đy kèm mượn thiết bị", isShow: false},
    {key: 10, value: "Trang thái", isShow: false},

    {key: 11, value: "Ghi chú", isShow: false},

  ];
  unitList = [{
    id: 1,
    name: "Lô"
  }, {
    id: 2,
    name: "Cái"
  },
    {
      id: 3,
      name: "Chiếc"
    }];
  centerList: any[] = [];
  listA = new Observable<any[]>();
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
  listWarehouse = [];
  listSupplier = [];
  setI = 0;
  listStatus = [{id: 1, name: "Đang Trong kho"}, {id: 2, name: "Đang sử dụng"}]

  constructor(
    private  asetServiceService: AsetServiceService,
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
    private serviceService: DeviceServiceService,
    private commonService: CommonService,
    private servicesService: WarehouseServicesService,
    private supplierServieceService: SupplierServieceService,
    private listDeviceService: ListDeviceService,
    private downloadService: DownloadService,
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
    this.getDevide()
    this.loadAll();
    this.onResize();
    this.getWarehouseList();
    this.getSupperlerList();

  }

  permissionCheck(permission?: string) {
    return this.commonService.havePermission(permission);
  }


  toggleColumns(col) {
    col.isShow = !col.isShow;
  }

  // search theo ma nhan su


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

        this.listA = of(dataRes.sort((a, b) => a.fullName.localeCompare(b.fullName)));
        // this.listA = of(dataRes.sort((a, b) => a.code.localeCompare(b.code)));

      } else {
        this.listA = of([]);
      }
    });
  }

  onClearUnit5() {
    this.listA = of([]);
    this.searchHuman = '';
  }

  onClearHuman() {
    this.listA = of([]);
    this.searchHuman = '';
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


// loa du lieu bang
  loadAll() {
    this.spinner.show();
    if (this.setI !== 0) {
      this.searchForm.partId = this.form.get('partId').value ? this.form.get('partId').value : null;
      this.searchForm.warehouseId = this.form.get('warehouseID').value ? this.form.get('warehouseID').value : null;
      this.searchForm.nameOrCode = this.form.get('nameOrCode').value ? this.form.get('nameOrCode').value : null;
      this.searchForm.specifications = this.form.get('specifications').value ? this.form.get('specifications').value : null;
      this.searchForm.supplierId = this.form.get('supplierId').value ? this.form.get('supplierId').value : null;
      this.searchForm.reQuest = this.form.get('reQuest').value ? this.form.get('reQuest').value : null;
      this.searchForm.status = this.form.get('status').value ? this.form.get('status').value : null;

    }
    this.searchForm.page = this.page;
    this.searchForm.pageSize = this.itemsPerPage;
    this.serviceService.getAll(
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
    this.listAsset = res.data;
  }

  get formControl() {
    return this.form.controls;
  }

  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('HumanResourcesChange', response => this.loadAll());
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

  onSearchData() {
    this.setI = 1;
    this.transition();
  }

  transition() {
    this.router.navigate(['/system-categories/asset-resources'], {});
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


  private buidForm() {
    this.form = this.formBuilder.group({
      partId: [null],
      warehouseID: [null],
      supplierId: [null],
      nameOrCode: [],
      specifications: [],
      status: [],
      reQuest: [],
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

  convertJson(value) {
    if (value) {
      return JSON.parse(value);
    }
  }


  onKeyInput(event) {
    const value = this.getValueOfField('experience');
    let valueChange = event.target.value;
    const parseStr = valueChange.split('');
    if (parseStr[0] === '0') {
      valueChange = valueChange.replace('0', '');
    } else {
      valueChange = valueChange.replace(REGEX_REPLACE_PATTERN.INTEGER, '');
    }
    if (value !== valueChange) {
      this.setValueToField('experience', valueChange);
      return false;
    }
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }


  openUpdateAsser(type, selectedData) {
    const modalRef = this.modalService.open(AddAssetResourcesComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false,
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.id = selectedData ? selectedData.id : null;
    modalRef.componentInstance.code = selectedData ? selectedData.code : null;
    modalRef.componentInstance.listGroup = this.listGroup ? this.listGroup : null;
    modalRef.componentInstance.listSupplier = this.listSupplier ? this.listSupplier : null;
    modalRef.componentInstance.data = selectedData ? selectedData : null;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });

  }

  deleteResource(data) {

  }

  openQRCode(selectedData) {
    const modalRef = this.modalService.open(QRcodeComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false,
    });
    modalRef.componentInstance.listAsset = selectedData;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });

  }

  getWarehouseList() {
    const data = this.form.get("partId").value ? this.form.get('partId').value : 0
    this.servicesService.getByPart(data).subscribe(
      value => {
        this.listWarehouse = value;
      },
      error => {
        this.listWarehouse = []
      }
    )
  }

  getSupperlerList() {
    this.supplierServieceService.getListSuppler().subscribe(
      value => {
        this.listSupplier = value;
      },
      error => {
        this.listSupplier = []
      }
    )
  }

  getNate(value) {
    console.warn(value)
    if (value === null || value > 2 || value < 0) {
      return
    }
    console.warn("vào" + value)

    return this.listStatus[value - 1].name
  }

  getNameUnit(value) {
    if (value === null || value > 3 || value < 0) {
      return
    }
    return this.unitList[value - 1].name
  }

  getDevide() {
    this.listDeviceService.getListDeviceGroupNotId().subscribe(
      value => {
        this.listGroup = value;
        console.warn(this.listGroup)

      },
      error => {
        this.listGroup = []
      }
    )
    console.warn(this.listGroup)

  }

  xetdataa(iteam) {
    if (iteam.status === 2) {
      return iteam.useHummerName;
    } else {
      return '';
    }
  }

  xetdataWar(iteam) {
    if (iteam.status === 1) {
      return iteam.wareHouseName;
    } else {
      return '';
    }
  }

  xetdataLoacal(iteam) {
    if (iteam.status === 1) {
      return iteam.wareHouseName;
    } else {
      return '';
    }
  }

  onDelete(ids) {

    if(ids!==null){
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'delete';
      modalRef.componentInstance.param = 'thiết bị';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          console.warn(ids)
          this.onSubmitDelete(ids);
        }
      });
    } else {
      this.toastService.openErrorToast("Xóa không thành công");
    }

  }

  exportData(excelType) {
    this.spinner.show();
    const data = {};
    if ('xlsx' === excelType) {
      this.serviceService.dowloadExel(data).subscribe(res => {
        this.spinner.hide();
        console.warn(111, res);
        if (res) {
          this.downloadService.downloadFile(res);
        }
      }, error => {
        this.spinner.hide();
      });
    } else if ('xls' === excelType) {
      this.serviceService.dowloadExel(data).subscribe(res => {
        this.spinner.hide();
        console.warn(111, res);
        if (res) {
          this.downloadService.downloadFile(res);
        }
      }, error => {
        this.spinner.hide();
      });
    }
  }
  onSubmitDelete(id) {
    this.spinner.show();
    this.serviceService.delete(id).subscribe(
      res => {
        console.warn(res)
        this.spinner.hide();
        this.toastService.openSuccessToast('Xóa  thành công');
        this.loadAll();
      },
      err => {
      console.warn(err)
        this.spinner.hide();
        this.toastService.openErrorToast('Xóa  không thành công');
      }
    );
  }
}

