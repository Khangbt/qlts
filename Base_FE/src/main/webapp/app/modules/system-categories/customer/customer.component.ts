// import {Component, OnInit} from '@angular/core';
// import {FormBuilder, FormGroup} from "@angular/forms";
// import {Observable, Subject, Subscription} from "rxjs";
// import {HeightService} from "app/shared/services/height.service";
// import {ActivatedRoute, Router} from "@angular/router";
// import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
// import {NgxSpinnerService} from "ngx-spinner";
// import {JhiEventManager} from "ng-jhipster";
// import {ToastService} from "app/shared/services/toast.service";
// import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from "app/shared/constants/pagination.constants";
// import {REGEX_PATTERN} from "app/shared/constants/pattern.constants";
// import {AddHumanResourcesComponent} from "app/modules/system-categories/human-resources/add-human-resources/add-human-resources.component";
// import {CustomerApiService} from "app/core/services/customer-api/customer-api.service.service";
//
// @Component({
//   selector: 'jhi-customer',
//   templateUrl: './customer.component.html',
//   styleUrls: ['./customer.component.scss']
// })
// export class CustomerComponent implements OnInit {
//   form: FormGroup;
//   height: number;
//   itemsPerPage: any;
//   maxSizePage: any;
//   routeData: any;
//   page: number;
//   second: any;
//   totalItems: any;
//   previousPage: any;
//   predicate: any;
//   reverse: any;
//   userList: any;
//   formValue;
//   eventSubscriber: Subscription;
//   listUnit$ = new Observable<any[]>();
//   unitSearch;
//   debouncer: Subject<string> = new Subject<string>();
//   positionList: any[] = [];
//   cooperated = [
//     {id: 1, name: 'Hoạt động'},
//     {id: 0, name: 'Không hoạt động'},
//
//   ];
//   centerList: any[] = [];
//   active = 1;
//   user = JSON.parse(localStorage.getItem('user'));
//   constructor(
//     private heightService: HeightService,
//     private activatedRoute: ActivatedRoute,
//     private formBuilder: FormBuilder,
//     private customerApiService: CustomerApiService,
//     // private translateService: TranslateService,
//     private modalService: NgbModal,
//     protected router: Router,
//     private spinner: NgxSpinnerService,
//     private eventManager: JhiEventManager,
//     private toastService: ToastService,
//     // private sysUserService: SysUserService
//   ) {
//     this.itemsPerPage = ITEMS_PER_PAGE;
//     this.maxSizePage = MAX_SIZE_PAGE;
//     this.routeData = this.activatedRoute.data.subscribe(data => {
//       if (data && data.pagingParams) {
//         this.page = data.pagingParams.page;
//         this.previousPage = data.pagingParams.page;
//         this.reverse = data.pagingParams.ascending;
//         this.predicate = data.pagingParams.predicate;
//       }
//     });
//   }
//
//
//   ngOnInit() {
//     this.buidForm();
//     this.onResize();
//     this.loadAll();
//   }
//
//   // }
//   loadAll() {
//     this.spinner.show();
//     this.customerApiService
//       .searchCustomer({
//         page: this.page,
//         pageSize: this.itemsPerPage,
//         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
//         code: this.form.get('code').value ? this.form.get('code').value : '',
//         isCooperated: this.form.get('isCooperated').value ? this.form.get('isCooperated').value : '',
//         // active: this.active,
//       })
//       .subscribe(
//         res => {
//           this.spinner.hide();
//           this.paginateUserList(res);
//         },
//         err => {
//           this.spinner.hide();
//           // this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
//           this.toastService.openErrorToast("loi");
//         }
//       );
//   }
//
//
//
//   get formControl() {
//     return this.form.controls;
//   }
//
//
//
//   setValueOfForm(formValue) {
//     this.formValue = formValue;
//   }
//
//   loadPage(page: number) {
//     if (page !== this.previousPage) {
//       this.previousPage = page;
//       this.transition();
//     }
//   }
//
//   changePageSize(size) {
//     this.itemsPerPage = size;
//     this.transition();
//   }
//
//   onSearchData() {
//     this.transition();
//   }
//
//   transition() {
//     this.router.navigate(['/system-categories/customer'], {
//       queryParams: {
//         page: this.page,
//         pageSize: this.itemsPerPage,
//         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
//         code: this.form.get('code').value ? this.form.get('code').value : '',
//         isCooperated: this.form.get('isCooperated').value ? this.form.get('isCooperated').value : '',
//       }
//     });
//     this.loadAll();
//   }
//
//   onResize() {
//     this.height = this.heightService.onResizeWithoutFooter();
//   }
//
//   sort() {
//     const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
//     if (this.predicate !== 'id') {
//       result.push('id');
//     }
//     return result;
//   }
//
//   customSearchFn(term: string, item: any) {
//     const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
//     const newRegEx = new RegExp(replacedKey, 'gi');
//     const purgedPosition = item.code.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
//     return newRegEx.test(purgedPosition);
//   }
//
//   onChangePosition(event) {
//     if (event) {
//       this.setValueToField('positionName', event.code);
//       this.setValueToField('positionId', event.id);
//     }
//   }
//
//   setValueToField(item, data) {
//     this.form.get(item).setValue(data);
//   }
//
//   onClearPosition() {
//     this.setValueToField('positionName', null);
//     this.setValueToField('positionId', null);
//   }
//   onChangePosition1(event) {
//     this.active = event.id;
//   }
//
//
//
//
//
//
//
//
//   private buidForm() {
//     this.form = this.formBuilder.group({
//       centerId: [],
//       active:[1],
//       code: [''],
//       positionName: [null],
//
//     });
//   }
//
//   private paginateUserList(res) {
//     console.warn(1234, res);
//     this.totalItems = res.dataCount;
//     this.userList = res.data;
//   }
//
//
//
//   getValueOfField(item) {
//     return this.form.get(item).value;
//   }
//
//   trimSpace(element) {
//     const value = this.getValueOfField(element);
//     if (value) {
//       this.setValueToField(element, value.trim());
//     }
//   }
//
//   onClick() {
//     const modalRef = this.modalService.open(AddHumanResourcesComponent, {
//       size: 'lg',
//       backdrop: 'static',
//       keyboard: false,
//       windowClass: 'myCustomModalClass'
//     });
//     modalRef.componentInstance.type = 'add';
//     modalRef.componentInstance.data = {};
//     modalRef.componentInstance.onCloseModal.subscribe(value => {
//     });
//   }
//
//   /*duc*/
//   openModalAddUser(type?: string, selectedData?: any) {
//     const modalRef = this.modalService.open(AddHumanResourcesComponent, {
//       size: 'lg',
//       backdrop: 'static',
//       keyboard: false
//     });
//     modalRef.componentInstance.type = type;
//     modalRef.componentInstance.id = selectedData ? selectedData.humanResourceId : null;
//     modalRef.result.then(result => {
//       if(result) {
//         this.loadAll();
//       }
//     }).catch(() => {
//       this.loadAll();
//     });
//
//   }
//
//
//   /* end duc*/
//
//
// }
//
