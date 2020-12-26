import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from 'app/shared/constants/pagination.constants';
import {ActivatedRoute, Router} from '@angular/router';
import {HeightService} from 'app/shared/services/height.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConfirmModalComponent} from 'app/shared/components/confirm-modal/confirm-modal.component';
import {NgxSpinnerService} from 'ngx-spinner';
import {Observable, of, Subject, Subscription} from 'rxjs';
import {JhiEventManager} from 'ng-jhipster';
import {ToastService} from 'app/shared/services/toast.service';
import {REGEX_PATTERN} from 'app/shared/constants/pattern.constants';
import {AddPartnerComponent} from "app/modules/system-categories/partner-management/add-partner/add-partner.component";
import {CustomerApiService} from "app/core/services/customer-api/customer-api.service.service";
import {debounceTime} from "rxjs/operators";
import {TIME_OUT} from "app/shared/constants/set-timeout.constants";
import {TranslateService} from "@ngx-translate/core";
import {CommonService} from "app/shared/services/common.service";
import {NgSelectComponent} from "@ng-select/ng-select";


@Component({
  selector: 'jhi-partner-management',
  templateUrl: './partner-management.component.html',
  styleUrls: ['./partner-management.component.scss']
})
export class PartnerManagementComponent implements OnInit, AfterViewInit {
  @ViewChild('ngSelectName', {static: false}) ngSelectName: NgSelectComponent;
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
    {id: 1, name: 'Hoạt động'},
    {id: 3, name: 'Không hoạt động'},

  ];
  listStatus = [
    {statusId: null, statusValue: "Tất cả"},
    {statusId: 1, statusValue: "Đang hợp tác"},
    {statusId: 0, statusValue: "Không hợp tác"}
  ];
  status = null;
  listPartner: any;
  searchCustomer;
  dataCustomer: Subject<string> = new Subject<string>();
  listCustomer = new Observable<any[]>();
  columns = [
    {key: 0, value: "Mã khách hàng", isShow: true},
    {key: 1, value: "Tên khách hàng", isShow: true},
    {key: 2, value: "Trạng thái hợp tác", isShow: true},
    {key: 3, value: "Đã hợp tác với IIST", isShow: true},
    {key: 4, value: "Số điện thoại", isShow: true},
    {key: 5, value: "Địa chỉ", isShow: true},
    {key: 6, value: "Ghi chú", isShow: false},
  ];
  showAction: any;
  centerList: any[] = [];
  active = 1;
  user = JSON.parse(localStorage.getItem('user'));
  code = '';
  listHistory: any;

  constructor(
    private heightService: HeightService,
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private customerApiService: CustomerApiService,
    private modalService: NgbModal,
    protected router: Router,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private translateService: TranslateService,
    private toastService: ToastService,
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
    if (!commonService.havePermission('resource.qldt_sdt') && !commonService.havePermission('resource.qldt_xdt')) {
      this.showAction = false;
    }else{
      this.showAction = true;
    }
  }

  get formControl() {
    return this.form.controls;
  }

  ngOnInit() {
    this.buildForm();
    this.onResize();
    this.loadAll();
    this.registerChange();
    this.debounceOnSearch5();
  }

  debounceOnSearch5() {
    this.dataCustomer.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit5(value));
  }

  loadDataOnSearchUnit5(term) {
    const data = {
      keySearch: term.trim().toUpperCase(),
      type: 'PARTNER'
    }
    this.customerApiService.getListCustomer(data).subscribe(res => {
      if (this.searchCustomer) {
        const dataRes: any = res;
        this.listCustomer = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));
      } else {
        this.listCustomer = of([]);
      }
    });
  }

  // }
  loadAll() {
    this.spinner.show();
    this.getListHistory();
    this.customerApiService
      .searchCustomer({
        page: this.page,
        pageSize: this.itemsPerPage,
        sort: (this.reverse ? 'asc' : 'desc'),
        code: this.code,
        status: this.status,
      })
      .subscribe(
        res => {
          this.spinner.hide();
          this.paginateUserList(res);
        },
        err => {
          this.spinner.hide();
          this.toastService.openErrorToast("Đã có lỗi xảy ra");
        }
      );
  }

  getListHistory() {
    this.customerApiService.getListHistory().subscribe(success => {
      this.listHistory = success;
    });

  }

  toggleColumns(col) {
    col.isShow = !col.isShow;
  }

  onChangeCustomer() {
  }

  changeLeagueOwner(e) {
    this.code = ((e === undefined) ? '' : e.code);
  }


  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('HumanResourcesChange', response => this.loadAll());
  }

  setValueOfForm(formValue) {
    this.formValue = formValue;
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
    this.transition();
  }

  showStatus(dt) {
    if (dt === 1)
      return "Đang hợp tác"
    return "Không hợp tác"
  }

  showCoop(dt) {
    if (dt === 1)
      return "Có"
    return "Không"
  }

  transition() {
    this.router.navigate(['/system-categories/partner-management'], {
      queryParams: {
        // page: this.page,
        // pageSize: this.itemsPerPage,
        // sort: (this.reverse ? 'asc' : 'desc'),
        // code: this.form.get('code').value ? this.form.get('code').value : '',
        // status: this.status,
      }
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

  customSearchFn(term: string, item: any) {
    const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    const newRegEx = new RegExp(replacedKey, 'gi');
    const purgedPosition = item.code.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    return newRegEx.test(purgedPosition);
  }

  onChangePosition(event) {
    if (event) {
      // this.setValueToField('cooperated', event.statusId);
      this.status = event.statusId;
    }
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  onClearPosition() {
    this.setValueToField('positionName', null);
    this.setValueToField('positionId', null);
  }

  onChangePosition1(event) {
    this.active = event.id;
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

  getValueOfField(item) {
    return this.form.get(item).value;
  }

  trimSpace(element) {
    const value = this.getValueOfField(element);
    if (value) {
      this.setValueToField(element, value.trim());
    }
  }

  openPartnerModal2(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddPartnerComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.id = selectedData ? selectedData.customerId : null;
    modalRef.result.then(result => {
      if (result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });

  }

  convertJson(value) {
    if (value) {
      return JSON.parse(value);
    }
  }


  /* end duc*/

  openPartnerModal(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddPartnerComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false,
      windowClass: 'myCustomModalClass'
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.id = selectedData ? selectedData.customerId : null;
    modalRef.result.then(result => {
      if (result) {
        this.form.get('status').reset();
        this.loadAll();
      }

    })
      .catch(() => {
        this.form.get('status').reset();
        this.form.reset();
        this.loadAll();
      });
  }

  openModalDelete(data) {
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'delete';
    modalRef.componentInstance.param = 'khách hàng';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.deleteCustomer(data.customerId);
      }
    });
  }

  deleteCustomer(ids) {
    this.customerApiService.deleteCustomer(ids).subscribe(res => {
      if (res.data) {
        this.spinner.hide();
        if (res.data.code === "BK021") {
          this.toastService.openSuccessToast("Xóa khách hàng thành công");
        } else if (res.data.code === "BK022") {

          this.toastService.openErrorToast("Xóa nhân sự thất bại");
        }
        this.loadAll();
      }
    }, error => {
      this.spinner.hide();
      this.toastService.openErrorToast("Xóa khách hàng thất bại");
    });
  }

  onSearchCustomer(event) {
    this.searchCustomer = event.term;
    const term = event.term;
    if (term !== '') {
      this.dataCustomer.next(term);
    } else {
      this.listCustomer = of([]);
    }
  }

  onClearCustomer() {
    this.listCustomer = of([]);
    this.searchCustomer = '';
  }

  onCloseCustomer() {
    if (!this.form.value.parentName) {
      this.listCustomer = of([]);
      this.searchCustomer = '';
    }
  }

  openAddProject(type?: string, selectedData?: any) {
    // this.router.navigate(['system-categories/add-partner']);
    // if (type === 'updateCustomer')  this.commonService.setDataTranfer('partnerData', selectedData);
    if(type==='addCustomer') this.router.navigate(['system-categories/add-partner']);
    else this.router.navigate(['system-categories/add-partner'], { queryParams: { id: selectedData.customerId} });

  }

  customSearchCustomer(term: string, item: any): any {
    term = term.toLocaleLowerCase().trim();
    return (item.name ? item.name.toLocaleLowerCase().indexOf(term) > -1 : ''.indexOf(term))
      || (item.code ? item.code.toLocaleLowerCase().indexOf(term) > -1 : ''.indexOf(term))
  }

  private buildForm() {
    this.code = '',
      this.form = this.formBuilder.group({
        status: "",
        code: "",
        note: "",
      });
  }

  private paginateUserList(res) {
    console.warn(1234, res);
    this.totalItems = res.dataCount;
    this.listPartner = res.data;
  }

  ngAfterViewInit(): void {
    this.ngSelectName.focus();
  }
}



