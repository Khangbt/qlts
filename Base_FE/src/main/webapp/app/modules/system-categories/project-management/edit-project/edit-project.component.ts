import {AfterViewInit, Component, EventEmitter, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from 'app/shared/constants/pagination.constants';
import {ActivatedRoute, Router} from '@angular/router';
import {HeightService} from 'app/shared/services/height.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConfirmModalComponent} from 'app/shared/components/confirm-modal/confirm-modal.component';
import {TranslateService} from '@ngx-translate/core';
import {NgxSpinnerService} from 'ngx-spinner';
import {OrganizationCategoriesService} from 'app/core/services/system-management/organization-categories.service';
import {HttpClient} from '@angular/common/http';
import {Observable, of, Subject, Subscription} from 'rxjs';
import {JhiEventManager} from 'ng-jhipster';
import {debounceTime} from 'rxjs/operators';
import {TIME_OUT} from 'app/shared/constants/set-timeout.constants';
import {ToastService} from 'app/shared/services/toast.service';
import {REGEX_PATTERN} from 'app/shared/constants/pattern.constants';
import {edituserModel} from 'app/core/models/system-categories/edit-user.model';
import {OrganizationCategoriesModel} from 'app/core/models/system-categories/organization-categories.model';
import {CommonService} from 'app/shared/services/common.service';
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";
import {CommonUtils} from 'app/shared/util/common-utils.service';
import {UploadFileComponent} from 'app/shared/components/upload-file/upload-file.component';
import {createNumberMask} from 'text-mask-addons';
import {RedmineLinkComponent} from "app/modules/system-categories/project-management/view-project/redmine-link/redmine-link.component";
import {CODE_POPSITION, KEY_SEARCH} from "app/shared/constants/app-params.constants";
import {ConfirmPlanComponent} from "app/modules/system-categories/project-management/edit-project/confirm-plan/confirm-plan.component";

@Component({
  selector: 'jhi-edit-project',
  templateUrl: './edit-project.component.html',
  styleUrls: ['./edit-project.component.scss']
})
export class EditProjectComponent implements OnInit, AfterViewInit {
  @ViewChild('fileImport', {static: false}) fileImport: UploadFileComponent;
  @Input() public selectedData;
  @Input() type;
  form: FormGroup;
  height: number;
  itemsPerPage: any;
  maxSizePage: any;
  routeData: any;
  page: any;
  second: any;
  totalItems: any;
  previousPage: any;
  role: any;
  userRole: any;
  predicate: any;
  reverse: any;
  checkDelete = false;
  listId: any[];
  listA: any[];
  listUnit$ = new Observable<any[]>();
  parentOrganizationList = new Observable<any[]>();
  // groupOrganizationList = new Observable<any[]>();
  groupOrganizationList: any[] = [];
  groupOrganizationList2: any[] = [];
  unitSearch;
  checkRole = false;
  listUnit7$ = new Observable<any[]>();
  parentOrganizationList1 = new Observable<any[]>();
  // groupOrganizationList = new Observable<any[]>();
  groupOrganizationList1: any[] = [];
  unitSearch7;


  debouncer: Subject<string> = new Subject<string>();
  listUnit1$ = new Observable<any[]>();
  unitSearch1;
  debouncer1: Subject<string> = new Subject<string>();
  listUnit2$ = new Observable<any[]>();
  unitSearch2;
  debouncer2: Subject<string> = new Subject<string>();
  listUnit3$ = new Observable<any[]>();
  unitSearch3;
  debouncer3: Subject<string> = new Subject<string>();
  listUnit4$ = new Observable<any[]>();
  unitSearch4;
  debouncer4: Subject<string> = new Subject<string>();
  listUnit5$ = new Observable<any[]>();
  unitSearch5;
  debouncer5: Subject<string> = new Subject<string>();
  listUnit6$ = new Observable<any[]>();
  unitSearch6;
  debouncer6: Subject<string> = new Subject<string>();
  searchForm: any;
  eventSubscriber: Subscription;
  organizationCategoriesModel: [];
  organizationList: any[];
  rBaM: number;
  rpmT: number;
  edit: edituserModel[];
  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  message = '';
  projectId: number;
  disabled: Boolean = true;
  fileInfos: Observable<any>;
  project: OrganizationCategoriesModel;
  // attachDoc: OrganizationCategoriesModel[];
  attachDoc = [];
  dataClone: any;
  testDoc: OrganizationCategoriesModel;
  listFileProject: any;
  filePerPage = 10;
  dateUpload: any;
  filePage = 1;
  maxFilePage = 10;
  errImport = false;
  successImport = false;
  successMessage;
  errMessage;
  lstvalueOld: any[] = [];
  lstvalueNew: any[] = [];
  valueOldString: String;
  valueNewString: String;
  lstHistory: any[] = [];
  model: any;

  dataEstimatePrelimiinary: any;
  dataEstimateUnapproved: any;
  dataEstimateOffer: any;
  dataEstimateLatch: any;
  dataEstimateInternal: any;

  nameProject: string;
  isModalConfirmShow = false;
  currencyMasksScoreEvaluation = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: true,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: false,
    thousandsSeparatorSymbol: '',
    decimalSymbol: '.'
  };


  constructor(
    private activatedRoute: ActivatedRoute,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private translateService: TranslateService,
    private spinner: NgxSpinnerService,
    private toastService: ToastService,
    private organizationCategoriesService: OrganizationCategoriesService,
    private eventManager: JhiEventManager,
    private _http: HttpClient,
    private router: Router,
    private commonService: CommonService,
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
    this.activatedRoute.queryParams.subscribe(param => {
      this.projectId = param.id;
    })
  }

  get formControl() {
    return this.form.controls;
  }

  ngOnInit() {
    this.spinner.show();
    this.searchForm = {};
    this.getRole();
    this.buidForm();
    this.onResize();
    this.searchHandle();
    this.registerChange();
    this.debounceOnSearch1();
    this.debounceOnSearch2();
    this.debounceOnSearch3();
    this.debounceOnSearch4();
    this.debounceOnSearch5();
    this.debounceOnSearch6();
    this.getDataDropdown();
    this.getDataDropdown1();
    this.getDataDropdown2();
    this.checkRole1();
    this.setDecimal();
    this.rBaM = 0;
    this.rpmT = 0;
    const dataProject = {
      projectId: this.projectId,
    }

    this.organizationCategoriesService.getProjectById(dataProject).subscribe(res => {
      this.attachDoc = res.data[0].lstAttachDocument;
      this.lstHistory = res.data[0].historyDTOList;
      this.dataClone = res.data[0];
      this.model = res.data[0];
      this.nameProject = res.data[0].name;
      this.form.patchValue(res.data[0]);
      this.form.get('code').setValue(res.data[0].code);
      this.form.get('name').setValue(res.data[0].name);

      const dataTemp = [{
        humanResourceId: res.data[0].pmId,
        username: res.data[0].pmName
      }]
      this.listUnit1$ = of(dataTemp);


      const dataTemp2 = [{
        humanResourceId: res.data[0].baId,
        username: res.data[0].bmName
      }]
      this.listUnit2$ = of(dataTemp2);


      const dataTemp3 = [{
        humanResourceId: res.data[0].testLeadId,
        username: res.data[0].testLeaderName
      }]
      this.listUnit3$ = of(dataTemp3);


      const dataTemp4 = [{
        humanResourceId: res.data[0].qaId,
        username: res.data[0].qmName
      }]
      this.listUnit4$ = of(dataTemp4);


      const dataTemp5 = [{
        customerId: res.data[0].partnerID,
        name: res.data[0].partnerName
      }]
      this.listUnit5$ = of(dataTemp5);

      this.form.get("ba").setValue(res.data[0].ba === 1 ? true : false);
      this.form.get("dev").setValue(res.data[0].dev === 1 ? true : false);
      this.form.get("test").setValue(res.data[0].test === 1 ? true : false);
      this.form.get("BaM").setValue(res.data[0].baMan === 1 ? "1" : "0");

      const dataTemp6 = [{
        id: res.data[0].statusDetail,
        name: res.data[0].statusDetailName
      }]
      this.listUnit6$ = of(dataTemp6);
      this.form.get('customerPmName').setValue(res.data[0].customerPmName);
      this.form.get('amName').setValue(res.data[0].amName);
      this.form.get('amEmail').setValue(res.data[0].amEmail);
      this.form.get('month').setValue(res.data[0].month);
      this.form.get('dateExpected').setValue(res.data[0].dateExpected);
      this.form.get('description').setValue(res.data[0].description);
      this.form.get('estimatePrelimiinary').setValue(res.data[0].estimatePrelimiinary);
      this.form.get('estimateActual').setValue(res.data[0].estimateActual);
      this.form.get('estimateOffer').setValue(res.data[0].estimateOffer);
      this.form.get('estimateLatch').setValue(res.data[0].estimateLatch);
      this.form.get('estimateUnapproved').setValue(res.data[0].estimateUnapproved);
      this.form.get('estimateInternal').setValue(res.data[0].estimateInternal);

      this.dataEstimateInternal = res.data[0].estimateInternal;
      this.dataEstimatePrelimiinary = res.data[0].estimatePrelimiinary;
      this.dataEstimateLatch = res.data[0].estimateLatch;
      this.dataEstimateOffer = res.data[0].estimateOffer
      this.dataEstimateUnapproved = res.data[0].estimateUnapproved;
      console.warn(277, "Data EI", this.model)
      //console.warn("manhhihi2", this.project);


    });
  }

  ngAfterViewInit(): void {
    this.spinner.hide();
  }

  checkRole1() {
    const userToken: any = this.formStoringService.get(STORAGE_KEYS.USER);
    if (userToken.role !== "BOD" && userToken.role !== "ADMIN") {
      this.checkRole = true;
    }
    // eslint-disable-next-line no-console
    console.log(this.checkRole);
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }

  getValueOfField(item) {
    return this.form.get(item).value;
  }


  onBlurEmail(field) {
    this.setValueToField(field, this.getValueOfField(field).trim());
    if (!REGEX_PATTERN.EMAIL.test(this.getValueOfField(field))) {
      if (this.getValueOfField(field) !== '') {
        this.form.controls[field].setErrors({invalid: true});
      }
    }
  }


  onSearchUnit(event) {
    this.unitSearch = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer.next(term);
    } else {
      this.listUnit$ = of([]);
    }
  }


  onClearUnit() {
    this.listUnit$ = of([]);
    this.unitSearch = '';
  }

  onSearchUnitClose() {
    if (!this.form.value.groupName) {
      this.listUnit$ = of([]);
      this.unitSearch = '';
    }
  }


  onSearchUnit6(event) {
    this.unitSearch6 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer6.next(term);
    } else {
      this.listUnit6$ = of([]);
    }
  }

  debounceOnSearch6() {
    this.debouncer6.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit6(value));
  }

  loadDataOnSearchUnit6(term) {
    const data = {
      keySearch: term,
      type: 'STATUSD'
    }

    this.organizationCategoriesService.getPartnerInfo(data).subscribe(res => {
      if (this.unitSearch6) {
        const dataRes: any = res;
        this.listUnit6$ = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));

      } else {
        this.listUnit6$ = of([]);
      }
    });
  }

  onClearUnit6() {
    this.listUnit6$ = of([]);
    this.unitSearch6 = '';
  }

  onSearchUnitClose6() {
    if (!this.form.value.name) {
      this.listUnit6$ = of([]);
      this.unitSearch6 = '';
    }
  }

  onSearchUnit1(event) {
    this.unitSearch1 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer1.next(term);
    } else {
      this.listUnit1$ = of([]);
    }
  }

  debounceOnSearch1() {
    this.debouncer1.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit1(value));
  }

  loadDataOnSearchUnit1(term) {
    const data = {
      keySearch: term,
      type: KEY_SEARCH.PM
    }
    this.organizationCategoriesService.getParents(data).subscribe((res => {
      if (this.unitSearch1) {
        const dataRes: any = res;
        this.listUnit1$ = of(dataRes.sort((a, b) => a.username.localeCompare(b.username)));

      } else {
        this.listUnit1$ = of([]);
      }
    }));
  }

  onClearUnit1() {
    this.listUnit1$ = of([]);
    this.unitSearch1 = '';
  }

  onSearchUnitClose1() {
    if (!this.form.value.name) {
      this.listUnit1$ = of([]);
      this.unitSearch1 = '';
    }
  }

  customSearchFn1(term: string, item: any): any {
    term = term.toLocaleLowerCase();
    return item.username.toLocaleLowerCase().indexOf(term) > -1 ||
      item.email.toLocaleLowerCase().indexOf(term) > -1;
  }

  onSearchUnit2(event) {
    // eslint-disable-next-line no-debugger
    this.unitSearch2 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer2.next(term);
    } else {
      this.listUnit2$ = of([]);
    }
  }

  debounceOnSearch2() {
    this.debouncer2.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit2(value));
  }

  loadDataOnSearchUnit2(term) {
    // eslint-disable-next-line no-debugger
    debugger;
    const data = {
      keySearch: term,
      type: KEY_SEARCH.BA_MANAGER
    }

    this.organizationCategoriesService.getParents(data).subscribe(res => {
      if (this.unitSearch2) {
        const dataRes: any = res;
        this.listUnit2$ = of(dataRes.map(value => {
          const dataMap = {
            username: value.username ? value.username : '',
            humanResourceId: value.humanResourceId ? value.humanResourceId : '',
            email: value.email ? value.email : ''
          }
          return dataMap;
        }));

      } else {
        this.listUnit2$ = of([]);
      }
    });
  }

  onClearUnit2() {
    this.listUnit2$ = of([]);
    this.unitSearch2 = '';
  }

  onSearchUnitClose2() {
    if (!this.form.value.name) {
      this.listUnit2$ = of([]);
      this.unitSearch2 = '';
    }
  }


  onSearchUnit3(event) {
    this.unitSearch3 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer3.next(term);
    } else {
      this.listUnit3$ = of([]);
    }
  }

  debounceOnSearch3() {
    this.debouncer3.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit3(value));
  }

  loadDataOnSearchUnit3(term) {
    const data = {
      keySearch: term,
      type: KEY_SEARCH.TEST_LEAD
    }

    this.organizationCategoriesService.getParents(data).subscribe(res => {
      if (this.unitSearch3) {
        const dataRes: any = res;
        this.listUnit3$ = of(dataRes.map(value => {
          const dataMap = {
            username: value.username ? value.username : '',
            humanResourceId: value.humanResourceId ? value.humanResourceId : '',
            email: value.email ? value.email : ''
          }
          return dataMap;
        }));

      } else {
        this.listUnit3$ = of([]);
      }
    });
  }

  onClearUnit3() {
    this.listUnit3$ = of([]);
    this.unitSearch3 = '';
  }

  onSearchUnitClose3() {
    if (!this.form.value.name) {
      this.listUnit3$ = of([]);
      this.unitSearch3 = '';
    }
  }


  onSearchUnit4(event) {
    this.unitSearch4 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer4.next(term);
    } else {
      this.listUnit4$ = of([]);
    }
  }

  debounceOnSearch4() {
    this.debouncer4.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit4(value));
  }

  loadDataOnSearchUnit4(term) {
    const data = {
      keySearch: term,
      type: KEY_SEARCH.QA_MANAGER
    }

    this.organizationCategoriesService.getParents(data).subscribe(res => {
      if (this.unitSearch4) {
        const dataRes: any = res;
        this.listUnit4$ = of(dataRes.map(value => {
          const dataMap = {
            username: value.username ? value.username : '',
            humanResourceId: value.humanResourceId ? value.humanResourceId : '',
            email: value.email ? value.email : ''
          }
          return dataMap;
        }));
      } else {
        this.listUnit4$ = of([]);
      }
    });
  }

  onClearUnit4() {
    this.listUnit4$ = of([]);
    this.unitSearch4 = '';
  }

  onSearchUnitClose4() {
    if (!this.form.value.parentName) {
      this.listUnit4$ = of([]);
      this.unitSearch4 = '';
    }
  }


  onSearchUnit5(event) {
    this.unitSearch5 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer5.next(term);
    } else {
      this.listUnit5$ = of([]);
    }
  }

  debounceOnSearch5() {
    this.debouncer5.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit5(value));
  }

  loadDataOnSearchUnit5(term) {
    const data = term

    this.organizationCategoriesService.getPartnerInfo(data).subscribe(res => {
      if (this.unitSearch5) {
        const dataRes: any = res;
        const customer: any = dataRes.data
        console.warn(604, "Customer", customer)
        this.listUnit5$ = of(customer.map(v => {
          return {
            customerId: v.customerId,
            name: v.name
          }
        }));
      } else {
        this.listUnit5$ = of([]);
      }
    });
  }

  onClearUnit5() {
    this.listUnit5$ = of([]);
    this.unitSearch5 = '';
  }

  onSearchUnitClose5() {
    if (!this.form.value.parentName) {
      this.listUnit5$ = of([]);
      this.unitSearch5 = '';
    }
  }


  searchHandle() {
    this.page = this.page - 1;
    this.organizationCategoriesService.search(this.searchForm).subscribe(result => {
      this.organizationCategoriesModel = result.data;
      this.totalItems = result.dataCount;
    });
  }

  getDataDropdown() {
    this.listUnit$ = of([]);
    const data = {
      keySearch: '',
      type: 'StatusO'
    }
    this.organizationCategoriesService.getStatusOverviewList(data).subscribe(
      result => {
        if (result) {
          const dataRes: any = result;
          this.groupOrganizationList = dataRes;
        } else {
          this.groupOrganizationList = [];
        }
      },
      err => {
        this.groupOrganizationList = [];
      }
    );
  }

  getDataDropdown2() {
    this.listUnit$ = of([]);
    const data = {
      keySearch: '',
      type: 'StatusD'
    }
    this.organizationCategoriesService.getStatusOverviewList(data).subscribe(
      result => {
        if (result) {
          const dataRes: any = result;
          this.groupOrganizationList2 = dataRes;
        } else {
          this.groupOrganizationList2 = [];
        }
      },
      err => {
        this.groupOrganizationList2 = [];
      }
    );
  }


  getDataDropdown1() {
    this.listUnit7$ = of([]);
    const data = {
      keySearch: '',
      type: 'StatusP'
    }
    this.organizationCategoriesService.getStatusOverviewList(data).subscribe(
      result => {
        if (result) {
          const dataRes: any = result;
          this.groupOrganizationList1 = dataRes;
        } else {
          this.groupOrganizationList1 = [];
        }
      },
      err => {
        this.groupOrganizationList1 = [];
      }
    );
  }


  doSearch() {
    this.searchForm.code = this.form.value.code;
    this.searchForm.name = this.form.value.name;
    this.searchForm.parentId = this.form.value.parent;
    this.searchForm.organizationGroup = this.form.value.groupOrganization;
    this.searchForm.page = this.page;
    this.searchForm.pageSize = this.itemsPerPage;

    this.organizationCategoriesService.search(this.searchForm).subscribe(response => {
      this.organizationCategoriesModel = response.data;
      this.totalItems = response.dataCount;
    });
  }

  customSearchFn(term: string, item: any): any {
    term = term.toLocaleLowerCase();
    console.warn(714, "Item", item)
    return item.name.toLocaleLowerCase().indexOf(term) > -1;
  }

  onChangePosition(event) {
    if (event) {
      this.setValueToField('id', event.id);
      this.setValueToField('name', event.name);
    }
  }

  onClearPosition() {
    this.setValueToField('name', null);
    this.setValueToField('name', null);
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  onSubmitDelete(id: number) {
    this.organizationCategoriesService.delete(id).subscribe(result => {
      // this.fetchData();
      this.page = 0;
      this.doSearch();
      this.toastService.openSuccessToast('Xóa dữ liệu thành công!');
    });
  }

  fetchData() {
    this.organizationCategoriesService.search(this.searchForm).subscribe(result => {
      this.organizationCategoriesModel = result.data;
      this.totalItems = result.dataCount;
    });
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  changePageSize(size) {
    this.itemsPerPage = size;
    //this.transition();
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    const formValue = this.form.value;
    this.doSearch();
  }

  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('organizationChange', response => this.searchHandle());
  }

  onProfitSelectionChange(data): void {
  };

  editUser(): void {
    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      return;
    }
    const neweditUserModel: edituserModel = new edituserModel();
    this.checkChange();
    const data = {
      projectId: this.projectId,
      //code : this.form.value.code,
      name: this.form.value.name.trim(),
      partnerID: this.form.value.partnerID,
      description: this.form.value.description,
      customerPmName: this.form.value.customerPmName === null ? '' : this.form.value.customerPmName.trim(),
      customerPmPhone: this.form.value.customerPmPhone === null ? '' : this.form.value.customerPmPhone.trim(),
      customerEmail: this.form.value.customerEmail === null ? '' : this.form.value.customerEmail.trim(),
      amPhone: this.form.value.amPhone === null ? '' : this.form.value.amPhone.trim(),
      amName: this.form.value.amName === null ? '' : this.form.value.amName.trim(),
      amEmail: this.form.value.amEmail === null ? '' : this.form.value.amEmail.trim(),
      estimatePrelimiinary: this.form.value.estimatePrelimiinary,
      estimateUnapproved: this.form.value.estimateUnapproved,
      estimateOffer: this.form.value.estimateOffer,
      estimateLatch: this.form.value.estimateLatch,
      estimateInternal: this.form.value.estimateInternal,
      pmId: this.form.value.pmId,
      baId: this.form.value.baId,
      qaId: this.form.value.qaId,
      ba: this.form.value.ba === true ? 1 : 0,
      dev: this.form.value.dev === true ? 1 : 0,
      test: this.form.value.test === true ? 1 : 0,
      baMan: this.form.value.BaM,
      //pmMan:this.rpmT,
      testLeadId: this.form.value.testLeadId,
      startDate: this.form.value.startDate === null ? '' : this.convertDate(this.form.value.startDate),
      endDate: this.form.value.startDate === null ? '' : this.convertDate(this.form.value.endDate),
      statusOverview: this.form.value.statusOverview,
      statusDetail: this.form.value.statusDetail,
      statusPayment: this.form.value.statusPayment,
      month: this.form.value.month,
      // dateExpected: this.form.value.dateExpected,
      dateExpected: this.form.value.dateExpected === null ? '' : this.convertDate(this.form.value.dateExpected),
      humanResourcesId: JSON.parse(localStorage.getItem('user')).humanResourceId,
      valueNew: this.valueNewString,
      valueOld: this.valueOldString,
    }

    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'update';
    modalRef.componentInstance.param = 'dự án';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.spinner.show();
        this.organizationCategoriesService.edit(data, this.listFileProject).subscribe(
          res => {
            this.toastService.openSuccessToast('Sửa dữ liệu thành công!');
            this.router.navigate(['system-categories/project-management']);
            this.spinner.hide()
          },
          err => {
            this.spinner.hide();
          });
      }
    });
  }

  checkChange() {
    const itemNew: any = {};
    const itemOld: any = {};
    if (this.form.value.customerPmName !== this.dataClone.customerPmName) {
      itemNew.customerPmName = this.form.value.customerPmName;
      itemOld.customerPmName = this.dataClone.customerPmName;
    }
    if (this.form.value.customerPmPhone !== this.dataClone.customerPmPhone) {
      itemNew.customerPmPhone = this.form.value.customerPmPhone;
      itemOld.customerPmPhone = this.dataClone.customerPmPhone;
    }
    if (this.form.value.customerEmail !== this.dataClone.customerEmail) {
      itemNew.customerEmail = this.form.value.customerEmail;
      itemOld.customerEmail = this.dataClone.customerEmail;
    }
    if (this.form.value.amPhone !== this.dataClone.amPhone) {
      itemNew.amPhone = this.form.value.amPhone;
      itemOld.amPhone = this.dataClone.amPhone;
    }
    if (this.form.value.amName !== this.dataClone.amName) {
      itemNew.amName = this.form.value.amName;
      itemOld.amName = this.dataClone.amName;
    }
    if ((this.form.value.ba === true ? 1 : 0) !== this.dataClone.ba) {
      itemNew.ba = (this.form.value.ba === true ? 1 : 0);
      itemOld.ba = this.dataClone.ba;
    }
    if ((this.form.value.dev === true ? 1 : 0) !== this.dataClone.dev) {
      itemNew.dev = (this.form.value.dev === true ? 1 : 0);
      itemOld.dev = this.dataClone.dev;
    }
    if ((this.form.value.test === true ? 1 : 0) !== this.dataClone.test) {
      itemNew.test = (this.form.value.test === true ? 1 : 0);
      itemOld.test = this.dataClone.test;
    }
    if (this.form.value.month !== this.dataClone.month) {
      itemNew.month = this.form.value.month;
      itemOld.month = this.dataClone.month;
    }
    if (this.form.value.dateExpected !== this.dataClone.dateExpected) {
      itemNew.dateExpected = this.form.value.dateExpected;
      itemOld.dateExpected = this.dataClone.dateExpected;
    }
    if (this.form.value.BaM !== this.dataClone.baMan.toString()) {
      itemNew.baMan = this.form.value.BaM;
      itemOld.baMan = this.dataClone.baMan;
    }
    if (this.form.value.estimatePrelimiinary !== this.dataClone.estimatePrelimiinary) {
      itemNew.estimatePrelimiinary = this.form.value.estimatePrelimiinary;
      itemOld.estimatePrelimiinary = this.dataClone.estimatePrelimiinary;
    }
    if (this.form.value.estimateOffer !== this.dataClone.estimateOffer) {
      itemNew.estimateOffer = this.form.value.estimateOffer;
      itemOld.estimateOffer = this.dataClone.estimateOffer;
    }
    if (this.form.value.estimateLatch !== this.dataClone.estimateLatch) {
      itemNew.estimateLatch = this.form.value.estimateLatch;
      itemOld.estimateLatch = this.dataClone.estimateLatch;
    }
    if (this.form.value.estimateInternal !== this.dataClone.estimateInternal) {
      itemNew.estimateInternal = this.form.value.estimateInternal;
      itemOld.estimateInternal = this.dataClone.estimateInternal;
    }
    if (this.form.value.statusOverview !== this.dataClone.statusOverview) {
      itemNew.statusOverview = this.form.value.statusOverview;
      itemOld.statusOverview = this.dataClone.statusOverview;
    }
    if (this.form.value.statusDetail !== this.dataClone.statusDetail) {
      itemNew.statusDetail = this.form.value.statusDetail;
      itemOld.statusDetail = this.dataClone.statusDetail;
    }
    if (this.form.value.statusPayment !== this.dataClone.statusPayment) {
      itemNew.statusPayment = this.form.value.statusPayment;
      itemOld.statusPayment = this.dataClone.statusPayment;
    }
    if (this.form.value.startDate !== this.dataClone.startDate) {
      itemNew.startDate = this.form.value.startDate;
      itemOld.startDate = this.dataClone.startDate;
    }
    if (this.form.value.endDate !== this.dataClone.endDate) {
      itemNew.endDate = this.form.value.endDate;
      itemOld.endDate = this.dataClone.endDate;
    }
    this.valueNewString = JSON.stringify(itemNew);
    this.valueOldString = JSON.stringify(itemOld);
  }

  // nuctv 29/07
  openModal() {
    const modalRef = this.modalService.open(RedmineLinkComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.projectID = this.projectId;
    modalRef.componentInstance.action = 'edit';
  }

  closeEdit() {
    this.isModalConfirmShow = true;
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'confirm';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.router.navigate(['system-categories/project-management']);
      }
      this.isModalConfirmShow = false;
    });
  }

  formatDate(date) {
    // console.log(date);
    if (date !== undefined) {
      return date.substring(0, 10);
    }

  }

  removeAttachDocument(index) {
    this.attachDoc.splice(index, 1);
  }

  onChangeFile(event: EventEmitter<File[]>) {
    this.dateUpload = this.convertDate(new Date());
    this.listFileProject = event;
    this.checkSizeFile();
  }

  onDeleteDocument(item) {
    let status;
    if (item.id !== undefined) {
      // this.fileAttachmentService.deleteFile(item.id).subscribe(
      //   res => {
      //     if (res.status === STATUS_CODE.SUCCESS) {
      //       status = res.status;
      //       this.toastService.openSuccessToast(this.translateService.instant('outsourcingPlan.toastr.delete.successful'));
      //     }
      //   },
      //   error => {
      //     this.toastService.openErrorToast(this.translateService.instant('outsourcingPlan.toastr.delete.fail'));
      //   },
      //   () => {
      //     let data;
      //     if (status === STATUS_CODE.SUCCESS) {
      //       data = this.formControl.listFileProject.value.filter(value => {
      //         if (value.fileName !== undefined && item.fileName !== undefined) {
      //           return value.fileName !== item.fileName;
      //         }
      //         if (value.name !== undefined) {
      //           return value;
      //         }
      //       });
      //     } else {
      //       data = this.formControl.listFileProject.value.filter(value => {
      //         if (value.fileName !== undefined && item.fileName !== undefined) {
      //           return value.fileName;
      //         }
      //         if (value.name !== undefined) {
      //           return value;
      //         }
      //       });
      //     }
      //     this.setValueToField('listFileProject', data);
      //   }
      // );
    } else {
      const data = this.formControl.listFileProject.value.filter(value => {
        if (value.name !== undefined && item.name !== undefined) {
          return value.name !== item.name;
        }
        if (value.fileName !== undefined) {
          return value;
        }
      });
      this.listFileProject = data;
      this.setValueToField('listFileProject', data);
      this.checkSizeFile();
    }
  }

  pageChange(event) {
    this.filePage = event;
  }

  downloadFile(item) {
    if (item.id !== undefined) {
      this.spinner.show();
      // this.fileAttachmentService.downloadFile(item.filePath).subscribe(res => {
      //   this.spinner.hide();
      //   if (res) {
      //     this.downloadService.downloadFile(res);
      //   }
      // });
    }
  }

  checkSizeFile() {
    let i = 0;
    // let sizeFileAll = 0;
    let sizeFile = 0;
    for (i = 0; i < this.listFileProject.length; i++) {
      if (this.listFileProject[i].id !== undefined) {
        sizeFile = this.listFileProject[i].size();

      } else {
        sizeFile = CommonUtils.tctGetFileSize(this.listFileProject[i]);
      }
      // sizeFileAll += Number(sizeFile);
      if (sizeFile > 5) {
        this.form.controls['listFileProject'].setErrors({maxFile: true});
      }
    }

    // if (sizeFileAll > 150) {
    //   this.form.controls['listFileProject'].setErrors({ maxFile: true });
    // }
  }

  // changePageSize(size) {
  //   this.filePerPage = size;
  //   // this.transition();
  // }

  onError(event) {
    if (event === '') {
      this.errImport = false;
      this.successImport = true;
      this.successMessage = this.translateService.instant('common.import.success.upload');
    } else {
      this.errImport = true;
      this.successImport = false;
      this.errMessage = event;
    }
  }

  setDecimal() {
    this.currencyMasksScoreEvaluation = createNumberMask({...this.currencyMasksScoreEvaluation});
  }

  viewChangeStr(history) {
    const valueNew = JSON.parse(history.valueNew);
    const valueOld = JSON.parse(history.valueOld);
    let result = "";
    if (valueOld !== null || valueNew !== null) {
      if (valueNew.customerPmName || valueOld.customerPmName) {
        result += 'PM Khách hàng: ' + valueOld.customerPmName + ' thành ' + valueNew.customerPmName;
      }
      if (valueNew.customerPmPhone || valueOld.customerPmPhone) {
        result += ((result ? ', ' : '') + 'Sđt khách hàng: ' + (valueOld.customerPmPhone ? valueOld.customerPmPhone : 'chưa có ') + ' thành ' + valueNew.customerPmPhone);
      }
      if (valueNew.customerEmail || valueOld.customerEmail) {
        result += ((result ? ', ' : '') + 'Email khách hàng: ' + (valueOld.customerEmail ? valueOld.customerEmail : 'chưa có ') + ' thành ' + valueNew.customerEmail);
      }
      if (valueNew.amName || valueOld.amName) {
        result += ((result ? ', ' : '') + 'Đầu mối liên hệ: ' + (valueOld.amName ? valueOld.amName : 'chưa có ') + ' thành ' + valueNew.amName);
      }
      if (valueNew.amPhone || valueOld.amPhone) {
        result += ((result ? ', ' : '') + 'Sđt đầu mối: ' + (valueOld.amPhone ? valueOld.amPhone : 'chưa có ') + ' thành ' + valueNew.amPhone);
      }
      if (valueNew.ba || valueOld.ba) {
        result += ((result ? ', ' : '') + 'Phạm vi yêu cầu BA ' + (valueOld.ba === 1 ? 'Có ' : 'Không') + ' thành ' + (valueNew.ba === 1 ? 'Có ' : 'Không'));
      }
      if (valueNew.dev || valueOld.dev) {
        result += ((result ? ', ' : '') + 'Phạm vi yêu cầu DEV ' + (valueOld.dev === 1 ? 'Có ' : 'Không') + ' thành ' + (valueNew.dev === 1 ? 'Có ' : 'Không'));
      }
      if (valueNew.test || valueOld.test) {
        result += ((result ? ', ' : '') + 'Phạm vi yêu cầu Test ' + (valueOld.test === 1 ? 'Có ' : 'Không') + ' thành ' + (valueNew.test === 1 ? 'Có ' : 'Không'));
      }
      if (valueNew.baMan || valueOld.baMan) {
        result += ((result ? ', ' : '') + 'Kế hoạch khảo sát ' + (valueOld.baMan === 1 ? 'BA ' : 'PM/Team Lead') + ' thành ' + (valueNew.baMan === "1" ? 'BA ' : 'PM/Team Lead'));
      }
      if (valueNew.month || valueOld.month) {
        result += ((result ? ', ' : '') + 'Tháng: ' + this.convertDate(valueOld.month) + ' thành ' + this.convertDate(valueNew.month));
      }
      if (valueNew.dateExpected || valueOld.dateExpected) {
        result += ((result ? ', ' : '') + 'Ngày KH mong muốn hoàn thành: ' + this.convertDate(valueOld.dateExpected) + ' thành ' + this.convertDate(valueNew.dateExpected));
      }
      if (valueNew.statusOverview || valueOld.statusOverview) {
        result += ((result ? ', ' : '') + 'Trạng thái tổng quan: ' + this.mapDropdown(valueOld.statusOverview, this.groupOrganizationList) + ' thành ' + this.mapDropdown(valueNew.statusOverview, this.groupOrganizationList));
      }
      if (valueNew.statusDetail || valueOld.statusDetail) {
        result += ((result ? ', ' : '') + 'Trạng thái chi tiết: ' + this.mapDropdown(valueOld.statusDetail, this.groupOrganizationList2) + ' thành ' + this.mapDropdown(valueNew.statusDetail, this.groupOrganizationList2));
      }
      if (valueNew.statusPayment || valueOld.statusPayment) {
        result += ((result ? ', ' : '') + 'Trạng thái thanh toán: ' + this.mapDropdown(valueOld.statusPayment, this.groupOrganizationList1) + ' thành ' + this.mapDropdown(valueNew.statusPayment, this.groupOrganizationList1));
      }
      if (valueNew.startDate || valueOld.startDate) {
        result += ((result ? ', ' : '') + 'Ngày dự kiến bắt đầu: ' + this.convertDate(valueOld.startDate) + ' thành ' + this.convertDate(valueNew.startDate));
      }
      if (valueNew.endDate || valueOld.endDate) {
        result += ((result ? ', ' : '') + 'Ngày dự kiến kết thúc: ' + this.convertDate(valueOld.endDate) + ' thành ' + this.convertDate(valueNew.endDate));
      }
      if (valueNew.estimatePrelimiinary || valueOld.estimatePrelimiinary) {
        result += ((result ? ', ' : '') + 'ULNL sơ bộ: ' + (valueOld.estimatePrelimiinary ? valueOld.estimatePrelimiinary : 'chưa có ') + ' thành ' + valueNew.estimatePrelimiinary);
      }
      if (valueNew.estimateInternal || valueOld.estimateInternal) {
        result += ((result ? ', ' : '') + 'ULNL nội bộ: ' + (valueOld.estimateInternal ? valueOld.estimateInternal : 'chưa có ') + ' thành ' + valueNew.estimateInternal);
      }
      if (valueNew.estimateOffer || valueOld.estimateOffer) {
        result += ((result ? ', ' : '') + 'ULNL chào giá: ' + (valueOld.estimateOffer ? valueOld.estimateOffer : 'chưa có ') + ' thành ' + valueNew.estimateOffer);
      }
      if (valueNew.estimateLatch || valueOld.estimateLatch) {
        result += ((result ? ', ' : '') + 'ULNL phê duyệt: ' + (valueOld.estimateLatch ? valueOld.estimateLatch : 'chưa có ') + ' thành ' + valueNew.estimateLatch);
      }

      return result;
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

  mapDropdown(id, list) {
    for (let i = 0; i < list.length; i++) {
      if (list[i].id === id) {
        return list[i].name;
      }
    }
  }

  getRole() {
    const role = this.formStoringService.getCurrentUserFromToken() ? this.formStoringService.getCurrentUserFromToken().role : '';
    this.userRole = role;
    return role;
  }

  //THAOLC-UPDATE
  openModalULNL(type) {
    if (this.getRole() === CODE_POPSITION.QA_MANAGER) {
      const modalRef = this.modalService.open(ConfirmPlanComponent, {
        size: 'lg',
        backdrop: 'static',
        keyboard: false,
        windowClass: 'myCustomModalClass'
      });
      modalRef.componentInstance.model = this.model;
      modalRef.componentInstance.type = type;
      if (type === 1) {
        modalRef.componentInstance.recorded.subscribe(value => {
          this.model.statusUnapproved = value.statusUnapproved;
        });
      } else if (type === 2) {
        modalRef.componentInstance.recorded.subscribe(value => {
          this.model.statusPreliinary = value.statusPreliinary;
        })
      } else if (type === 3) {
        modalRef.componentInstance.recorded.subscribe(value => {
          this.model.statusInternal = value.statusInternal;
        })
      } else if (type === 4) {
        modalRef.componentInstance.recorded.subscribe(value => {
          this.model.statusOffer = value.statusOffer;
        })
      } else if (type === 5) {
        modalRef.componentInstance.recorded.subscribe(value => {
          this.model.statusLatch = value.statusLatch;
        })
      }
      modalRef.result.then(result => {
        this.loadAll();
      }).catch(() => {
        this.loadAll();
      });
    }

  }

  loadAll() {
    const id = this.projectId;
    this.router.navigate(['system-categories/project-management/edit-project'], {queryParams: {id}});
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      code: [''],
      name: [null, Validators.compose([Validators.required])],
      customerPmName: [null, Validators.compose([Validators.required])],
      customerPmPhone: ['', Validators.compose([Validators.maxLength(11), Validators.pattern("(0)[0-9 ]{9}")])],
      customerEmail: [''],
      amName: ['', Validators.compose([Validators.maxLength(255)])],
      amEmail: [''],
      description: [''],
      partnerID: [null, Validators.compose([Validators.required])],
      estimatePrelimiinary: [''],
      estimateActual: [''],
      estimateUnapproved: [''],
      estimateOffer: [''],
      estimateLatch: [''],
      estimateInternal: [''],
      startDate: [''],
      endDate: [''],
      month: [''],
      dateExpected: [''],
      pmId: [null, Validators.compose([Validators.required])],
      baId: [null, Validators.compose([Validators.required])],
      qaId: [null, Validators.compose([Validators.required])],
      testLeadId: [null, Validators.compose([Validators.required])],
      statusOverview: [],
      ba: [],
      dev: [],
      test: [],
      statusDetail: [],
      statusPayment: [],
      amPhone: ['', Validators.compose([Validators.maxLength(11), Validators.pattern("(0)[0-9 ]{9}")])],
      BaM: [],
      listFileProject: [''],
      file: File,
      //parent: [null],
      //groupOrganization: [null]
    });
  }

  trimSpace(element) {
    const value = this.getValueOfField(element);
    if (value) {
      this.setValueToField(element, value.trim());
    }
  }

  displayCursorPointer() {
    return this.getRole() === CODE_POPSITION.QA_MANAGER || this.getRole() === CODE_POPSITION.ADMIN ? 'ulnl-img' : '';
  }

  roleCurrenUserIsQAManagerOrAdmin() {
    if (this.getRole() === CODE_POPSITION.QA_MANAGER || this.getRole() === CODE_POPSITION.ADMIN) {
      return true;
    } else {
      return false;
    }

  }
}
