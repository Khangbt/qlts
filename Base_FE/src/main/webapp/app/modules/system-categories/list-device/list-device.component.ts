import {Component, OnInit} from '@angular/core';
import {HeightService} from "app/shared/services/height.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SHOW_HIDE_COL_HEIGHT} from 'app/shared/constants/perfect-scroll-height.constants';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AddListDeviceComponent} from "app/modules/system-categories/list-device/add-list-device/add-list-device.component";
import {ListDeviceService} from "app/core/services/list-device/list-device.service";
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from "app/shared/constants/pagination.constants";
import {ActivatedRoute} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {WarehouseServicesService} from "app/core/services/warehouse/warehouse-services.service";
import {SupplierServieceService} from "app/core/services/Supplier-service/supplier-serviece.service";
import { ToastService } from 'app/shared/services/toast.service';

import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";

@Component({
  selector: 'jhi-list-device',
  templateUrl: './list-device.component.html',
  styleUrls: ['./list-device.component.scss']
})
export class ListDeviceComponent implements OnInit {
  height: any
  form: FormGroup;
  searchForm: any;
  page: number;
  itemsPerPage: any;
  totalItems: any;
  maxSizePage: any;
  previousPage: any;
  routeData: any;
  predicate: any;
  reverse: any;
  SHOW_HIDE_COL_HEIGHT = SHOW_HIDE_COL_HEIGHT;
  setI = 0;
  ListDevice = [];
  listWarehouse = [
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
  listSupplier = [];
  listPart = []
  columns = [
    {key: 0, value: "Mã số sản phẩm", isShow: true},
    {key: 1, value: "Tên sản phẩm", isShow: true},
    {key: 2, value: "Phòng ban đang sở hữu", isShow: true},
    {key: 3, value: "Số lượng thiết bị", isShow: true},
    {key: 4, value: "Số lượng thiết bị còn trong kho", isShow: true},
    {key: 5, value: "Vị trí nhà kho chứa sản phẩm", isShow: true},
    {key: 6, value: "Đơn vị", isShow: true},
    {key: 7, value: "Nhà cung cấp", isShow: true},
    {key: 8, value: "Ghi chú", isShow: false},

  ];

  constructor(
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private deviceService: ListDeviceService,
    private activatedRoute: ActivatedRoute,
    private spinner: NgxSpinnerService,
    private humanResourcesApiService: HumanResourcesApiService,
    private servicesService:WarehouseServicesService,
    private supplierServieceService:SupplierServieceService,
    private toastService: ToastService,

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

  ngOnInit(): void {
    this.searchForm = {};
    this.buidForm();
    this.loadAll();

    this.onResize();
    this.getPartList();
    this.getWarehouseList();
    this.getSupperlerList();
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  loadAll() {
    this.spinner.show();
    if (this.setI !== 0) {
      this.searchForm.partId = this.form.get('partId').value ? this.form.get('partId').value : null;
      this.searchForm.warehouseIdnpm  = this.form.get('warehouseID').value ? this.form.get('warehouseID').value : null;
      this.searchForm.nameOrCode = this.form.get('nameOrCode').value ? this.form.get('nameOrCode').value : null;
      this.searchForm.specifications = this.form.get('specifications').value ? this.form.get('specifications').value : null;
      this.searchForm.supplierId = this.form.get('supplierId').value ? this.form.get('supplierId').value : null;
    }
    this.searchForm.page = this.page;
    this.searchForm.pageSize = this.itemsPerPage;
    this.deviceService.getAll(this.searchForm).subscribe(
      value => {
        this.spinner.hide();

        this.paginateGroupPermissionList(value);
      },
      error => {
        this.spinner.hide();
      }
    )
  }

  private paginateGroupPermissionList(data) {
    this.ListDevice = data.data;
    this.totalItems = data.dataCount;

  }

  private buidForm() {
    this.form = this.formBuilder.group({
      partId: [null],
      warehouseID: [null],
      supplierId: [null],
      nameOrCode: [],
      specifications: [],
    })
  }

  transition() {
    this.loadAll();
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

  openAddListDevice(type, data) {
    const modalRef = this.modalService.open(AddListDeviceComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.data=data?data:null;
    modalRef.componentInstance.id = data?data.id:null;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });

  }

  toggleColumns(col) {
    col.isShow = !col.isShow;
  }

  onSearchData() {
    this.setI=1;
    this.loadAll();
  }

  delete(iteam) {
     if(iteam!==null){
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'delete';
        modalRef.componentInstance.param = 'thiết bị';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            console.warn(iteam)
            this.onSubmitDelete(iteam);
          }
        });
      } else {
        this.toastService.openErrorToast("Xóa không thành công");
      }
  
    
  }
 getWarehouseList(){
   const data=this.form.get("partId").value? this.form.get('partId').value : 0
    this.servicesService.getByPart(data).subscribe(
      value => {
        this.listWarehouse=value;
      },
      error => {
        this.listWarehouse=[]
      }
    )
  }
  getSupperlerList(){
    this.supplierServieceService.getListSuppler().subscribe(
      value => {
        this.listSupplier=value;
      },
      error => {
        this.listSupplier=[]
      }
    )
  }
  getPartList() {
    this.humanResourcesApiService.getPartList().subscribe(
      res => {
        if (res) {
          this.listPart = res.data;
        } else {
          this.listPart = [];
        }
      },
      error => {
        this.listPart = [];
      }
    );
  }
  xetString(value){
   if(value===null||value<1||value>3){
     return ;
   }
    return this.unitList[value-1].name;
  }
  onSubmitDelete(id) {
    this.spinner.show();
    this.deviceService.delete(id).subscribe(
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
