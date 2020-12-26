import {Component, OnInit} from '@angular/core';
import {HeightService} from "app/shared/services/height.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SHOW_HIDE_COL_HEIGHT} from "app/shared/constants/perfect-scroll-height.constants";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ActivatedRoute} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from "app/shared/constants/pagination.constants";
import {AddRequestFormBorrowComponent} from "app/modules/system-categories/request-form/add-request-form-borrow/add-request-form-borrow.component";
import {AddRequestFormBuyComponent} from "app/modules/system-categories/request-form/add-request-form-buy/add-request-form-buy.component";
import {RequestFomApiService} from "app/core/services/request-form-api/request-fom-api.service";
import {ToastService} from "app/shared/services/toast.service";
import {AddRequestFormReturnComponent} from "app/modules/system-categories/request-form/add-request-form-return/add-request-form-return.component";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";

@Component({
  selector: 'jhi-request-form',
  templateUrl: './request-form.component.html',
  styleUrls: ['./request-form.component.scss']
})
export class RequestFormComponent implements OnInit {
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
  ListDevice = [];
  partList = [];
  listRequest = [];
  columns = [
    {key: 0, value: "Mã số phiếu", isShow: true},
    {key: 1, value: "Loại phiéu", isShow: true},
    {key: 2, value: "Người tạo", isShow: true},
    {key: 3, value: "Ngày tạo", isShow: true},
    {key: 4, value: "Ngày xử lý", isShow: false},
    {key: 5, value: "Người xử lý", isShow: false},
    {key: 6, value: "Trạng thái phiếu", isShow: true},
    {key: 7, value: "Lý do từ chối", isShow: true},
    {key: 8, value: "Ghi chú", isShow: false},
    {key: 9, value: "Phòng ban yêu cầu", isShow: true},

  ];
  listStatus = [{id: 1, name: "Mới tạo"}, {id: 2, name: "Đã duyêt"}, {id: 3, name: "Đã hủy"}]
  listStyle = [{id: "YCM", name: "Phiếu yêu cầu nhập"},
    {id: "PT", name: "Phiếu Trả"},
    {id: "PYCM", name: "Phiếu yêu mươn thiết bị"}]

  constructor(private heightService: HeightService,
              private formBuilder: FormBuilder,
              private modalService: NgbModal,
              private activatedRoute: ActivatedRoute,
              private spinner: NgxSpinnerService,
              private requestFomApiService: RequestFomApiService,
              private toastService: ToastService,
              private humanResourcesApiService: HumanResourcesApiService,
              private formStoringService: FormStoringService,
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
    this.buidForm()
    this.searchForm = {};

    this.buidForm()
    this.loadAll();
    this.getPartList();
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      status: [null],
      creatHummerId: [null],
      tyle: [null],
      code: [],
      partId: [],
    });
    this.xetDataUer();
  }

  loadAll() {
    console.warn("Load lai")
    this.spinner.show();
    this.searchForm.tyle = this.form.get("tyle").value
    this.searchForm.code = this.form.get("code").value
    this.searchForm.partId = this.form.get("partId").value

    this.searchForm.creatHummerId = this.form.get("creatHummerId").value
    this.searchForm.status = this.form.get("status").value
    this.searchForm.page = this.page;
    this.searchForm.pageSize = this.itemsPerPage;
    this.requestFomApiService.getSearhAll(this.searchForm).subscribe(
      value => {
        this.paginateGroupPermissionList(value);
        this.spinner.hide();
      }, error => {
        this.spinner.hide();

      }
    )
    this.onResize();

  }

  private paginateGroupPermissionList(data) {
    this.listRequest = data.data;
    this.totalItems = data.dataCount;

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

  openBorrowDevice(type, data) {
    const modalRef = this.modalService.open(AddRequestFormBorrowComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.id = data;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });

  }

  openBuyDevice(type, data) {
    const modalRef = this.modalService.open(AddRequestFormBuyComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.id = data;
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
    this.loadAll();
  }

  openAddListRequest(style, data) {
    if (data.tyle === "PYCM") {
      const modalRef = this.modalService.open(AddRequestFormBorrowComponent, {
        size: 'lg',
        backdrop: 'static',
        keyboard: false
      });
      if (style === "detail") {
        modalRef.componentInstance.type = style;
        modalRef.componentInstance.data1 = data ? data : null;
        modalRef.componentInstance.id = data ? data.requestId : null;
        modalRef.componentInstance.partId = data ? data.partId : null;
        modalRef.componentInstance.status = data ? data.status : null;
        modalRef.result.then(result => {
          if (result) {
            this.loadAll();
          }
        }).catch(() => {
          this.loadAll();
        });

      }
      if (style === "update") {
        modalRef.componentInstance.type = style;
        modalRef.componentInstance.data1 = data ? data : null;
        modalRef.componentInstance.id = data ? data.requestId : null;
        modalRef.componentInstance.partId = data ? data.partId : null;
        modalRef.componentInstance.status = data ? data.status : null;
        modalRef.result.then(result => {
          if (result) {
            this.loadAll();
          }
        }).catch(() => {
          this.loadAll();
        });

      }
      if (style === "browser") {
        modalRef.componentInstance.type = style;
        modalRef.componentInstance.data1 = data ? data : null;
        modalRef.componentInstance.id = data ? data.requestId : null;
        modalRef.componentInstance.partId = data ? data.partId : null;
        modalRef.componentInstance.status = data ? data.status : null;
        modalRef.result.then(result => {
          if (result) {
            this.loadAll();
          }
        }).catch(() => {
          this.loadAll();
        });

      }
    }
    if (data.tyle === "YCM") {
      const modalRef = this.modalService.open(AddRequestFormBuyComponent, {
        size: 'lg',
        backdrop: 'static',
        keyboard: false
      });
      if (style === "detail") {
        modalRef.componentInstance.type = style;
        modalRef.componentInstance.data1 = data ? data : null;
        modalRef.componentInstance.id = data ? data.requestId : null;
        modalRef.componentInstance.partId = data ? data.partId : null;
        modalRef.componentInstance.status = data ? data.status : null;
        modalRef.result.then(result => {
          if (result) {
            this.loadAll();
          }
        }).catch(() => {
          this.loadAll();
        });

      }
      if (style === "update") {
        modalRef.componentInstance.type = style;
        modalRef.componentInstance.data1 = data ? data : null;
        modalRef.componentInstance.id = data ? data.requestId : null;
        modalRef.componentInstance.partId = data ? data.partId : null;
        modalRef.componentInstance.status = data ? data.status : null;
        modalRef.result.then(result => {
          if (result) {
            this.loadAll();
          }
        }).catch(() => {
          this.loadAll();
        });

      }
      if (style === "browser") {
        modalRef.componentInstance.type = style;
        modalRef.componentInstance.data1 = data ? data : null;
        modalRef.componentInstance.id = data ? data.requestId : null;
        modalRef.componentInstance.partId = data ? data.partId : null;
        modalRef.componentInstance.status = data ? data.status : null;
        modalRef.result.then(result => {
          if (result) {
            this.loadAll();
          }
        }).catch(() => {
          this.loadAll();
        });

      }
    }
    if (data.tyle === "PT") {
      const modalRef = this.modalService.open(AddRequestFormReturnComponent, {
        size: 'lg',
        backdrop: 'static',
        keyboard: false
      });
      if (style === "detail") {
        modalRef.componentInstance.type = style;
        modalRef.componentInstance.data1 = data ? data : null;
        modalRef.componentInstance.id = data ? data.requestId : null;
        modalRef.componentInstance.partId = data ? data.partId : null;
        modalRef.componentInstance.creatHummerId = data ? data.creatHummerId : null;
        modalRef.componentInstance.status = data ? data.status : null;
        modalRef.result.then(result => {
          if (result) {
            this.loadAll();
          }
        }).catch(() => {
          this.loadAll();
        });

      }
      if (style === "update") {
        modalRef.componentInstance.type = style;
        modalRef.componentInstance.data1 = data ? data : null;
        modalRef.componentInstance.id = data ? data.requestId : null;
        modalRef.componentInstance.partId = data ? data.partId : null;
        modalRef.componentInstance.creatHummerId = data ? data.creatHummerId : null;
        modalRef.componentInstance.status = data ? data.status : null;
        modalRef.result.then(result => {
          if (result) {
            this.loadAll();
          }
        }).catch(() => {
          this.loadAll();
        });

      }
      if (style === "browser") {
        modalRef.componentInstance.type = style;
        modalRef.componentInstance.data1 = data ? data : null;
        modalRef.componentInstance.id = data ? data.requestId : null;
        modalRef.componentInstance.partId = data ? data.partId : null;
        modalRef.componentInstance.status = data ? data.status : null;
        modalRef.componentInstance.creatHummerId = data ? data.creatHummerId : null;

        modalRef.result.then(result => {
          if (result) {
            this.loadAll();
          }
        }).catch(() => {
          this.loadAll();
        });

      }
    }
  }

  delete(data) {
    this.spinner.show();
    console.warn("1233323")
    this.requestFomApiService.deleteFindyId(data.id).subscribe(
      value => {

        this.spinner.hide();
        this.toastService.openSuccessToast('Xóa bộ phận thành công');
        this.loadAll();
      }, error => {
        this.spinner.hide();
        this.toastService.openSuccessToast('Looix');
        this.loadAll();
      }
    )

  }

  openReturnDevice(tyle, data) {
    const modalRef = this.modalService.open(AddRequestFormReturnComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = tyle;
    modalRef.componentInstance.id = data;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });
  }

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

  convertDate(str) {
    const date = new Date(str),
      mnth = ('0' + (date.getMonth() + 1)).slice(-2),
      day = ('0' + date.getDate()).slice(-2);
    return [day, mnth, date.getFullYear()].join('/');
  }

  convertStyle(str) {
    if (str === 'PT') {
      return 'Phiếu trả thiết bị'
    }
    if (str === 'YCM') {
      return 'Phiếu yêu cầu nhập kho'
    }
    if (str === 'PYCM') {
      return 'Phiếu yêu cầu mượn thiết bị'
    }
  }

  convertStatus(str) {
    if (str === 1) {
      return 'Đang chờ xử lý'
    }
    if (str === 2) {
      return 'Đã xử lý xong'
    }
    if (str === 3) {
      return 'Đã hủy'
    }
  }

  xetDataUer() {
    const userToken: any = this.formStoringService.get(STORAGE_KEYS.USER);
    if (userToken.role === "ROLE_USER") {
      this.form.get("creatHummerId").setValue(userToken.humanResourceId)
    }
    if (userToken.role === "ROLE_ADMINPART") {
      this.form.get("partId").setValue(userToken.partId)
    }

  }
}
