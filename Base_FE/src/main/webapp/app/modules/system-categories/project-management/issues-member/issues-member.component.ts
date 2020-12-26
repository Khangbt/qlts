import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from 'app/shared/constants/pagination.constants';
import {ActivatedRoute, Router} from '@angular/router';
import {HeightService} from 'app/shared/services/height.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {InvoiceSerialModel} from 'app/core/models/announcement-management/invoice-serial.model';
import {ConfirmModalComponent} from 'app/shared/components/confirm-modal/confirm-modal.component';
import {TranslateService} from '@ngx-translate/core';
import {NgxSpinnerService} from 'ngx-spinner';
import {HttpClient} from '@angular/common/http';
import {SHOW_HIDE_COL_HEIGHT} from 'app/shared/constants/perfect-scroll-height.constants';
import {BehaviorSubject, Observable, of, Subject, Subscription} from 'rxjs';
import {JhiEventManager} from 'ng-jhipster';
import {debounceTime} from 'rxjs/operators';
import {TIME_OUT} from 'app/shared/constants/set-timeout.constants';
import {ToastService} from 'app/shared/services/toast.service';
import {REGEX_PATTERN} from 'app/shared/constants/pattern.constants';
import {CommonService} from "app/shared/services/common.service";
import {FormStoringService} from "app/shared/services/form-storing.service";
import {IssuesMemberService} from "app/core/services/issues-member/issues-member.service";
import {IssuesMemberModel} from "app/core/models/issues-member/issues-member.model";
import {AddHumanResourcesComponent} from "app/modules/system-categories/human-resources/add-human-resources/add-human-resources.component";
import {IssuesMemberDetailComponent} from "app/modules/system-categories/project-management/issues-member/issues-member-detail/issues-member-detail.component";


@Component({
  selector: 'jhi-issues-member',
  templateUrl: './issues-member.component.html',
  styleUrls: ['./issues-member.component.scss']
})
export class IssuesMemberComponent implements OnInit {
  @Input() public selectedData: InvoiceSerialModel;
  @Input() type;
  form: FormGroup;
  token:string;
  height: number;
  itemsPerPage: any;
  maxSizePage: any;
  pageSize: any;
  routeData: any;
  page: any;
  buttonDisable;
  second: any;
  totalItems: any;
  checkSearch = false;
  message;
  previousPage: any;
  humanId:any;
  messageDate = null;
  predicate: any;
  reverse: any;
  checkDelete = false;
  listId: any[];
  columns:any[];
  listA: any[];
  listUnit$ = new Observable<any[]>();
  parentOrganizationList = new Observable<any[]>();
  groupOrganizationList: any[] = [];
  groupOrganizationList2: any[] = [];
  unitSearch;
  debouncer: Subject<string> = new Subject<string>();
  listUnit1$ = new Observable<any[]>();
  unitSearch1;
  debouncer1: Subject<string> = new Subject<string>();
  listUnit2$ = new Observable<any[]>();
  unitSearch2;
  debouncer2: Subject<string> = new Subject<string>();
  listUnit5$ = new Observable<any[]>();
  unitSearch5;
  debouncer5: Subject<string> = new Subject<string>();
  searchForm: any;
  eventSubscriber: Subscription;
  issuesMemberModel: IssuesMemberModel[];
  organizationList: any[];
  SHOW_HIDE_COL_HEIGHT = SHOW_HIDE_COL_HEIGHT;
  listErrorPro: any;
  statusList :any;
  listPriority:any[];
  listSeverity:any[];
  constructor(
    private activatedRoute: ActivatedRoute,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private translateService: TranslateService,
    private spinner: NgxSpinnerService,
    private toastService: ToastService,
    private issuesMemberService: IssuesMemberService,
    private eventManager: JhiEventManager,
    private _http: HttpClient,
    protected router: Router,
    private commonService: CommonService,
    private formStoringService: FormStoringService,
  ) {

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
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.columns = this.issuesMemberService.columns;
    this.searchForm = {};
    this.buidForm();
    this.onResize();
    this.loadAll();
    this.findAllStatus();
  }


  toggleColumns(col) {
    col.isShow = !col.isShow;
    this.issuesMemberService.columns = this.columns;
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

  onSearchUnit1(event) {
    this.unitSearch1 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer1.next(term);
    } else {
      this.listUnit1$ = of([]);
    }
  }

  onClearUnit1() {
    this.listUnit1$ = of([]);
    this.unitSearch1 = '';
  }

  customSearchFn1(term: string, item: any): any {
    term = term.toLocaleLowerCase().trim();
    return item.username.toLocaleLowerCase().indexOf(term) > -1 ||
      item.email.toLocaleLowerCase().indexOf(term) > -1;
  }

  onSearchUnitClose1() {
    if (!this.form.value.firstName) {
      this.listUnit1$ = of([]);
      this.unitSearch1 = '';
    }
  }

  onSearchUnit2(event) {
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
    // debugger;
    const data = {
      keySearch: term,
      type: 'STATUSD'
    }
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

  onSearchUnit5(event) {
    this.unitSearch5 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer5.next(term);
    } else {
      this.listUnit5$ = of([]);
    }
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
  customSearchFn2(term: string, item: any): any {
    term = term.toLocaleLowerCase().trim();
    return item.name.toLocaleLowerCase().indexOf(term) > -1 ||
      item.code.toLocaleLowerCase().indexOf(term) > -1;
  }

  convertDate(str) {
    const date = new Date(str),
      mnth = ('0' + (date.getMonth() + 1)).slice(-2),
      day = ('0' + date.getDate()).slice(-2);
    return [ day,mnth,date.getFullYear()].join('/');
  }

  customSearchFn(term: string, item: any) {
    const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    const newRegEx = new RegExp(replacedKey, 'gi');
    const purgedPosition = item.name.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    return newRegEx.test(purgedPosition);
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

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }


  //dangnp
  changePageSize(size) {
    this.itemsPerPage = size;
     this.loadAll();
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.loadAll();
    }
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      codeProject: [],
      status:[],
      author:[],
      assignee:[]
    });
  }

  //Hàm đồng bộ
  synchronizeIssuesMember(){
    this.issuesMemberService.synchronizedIssue().subscribe(success =>{
      this.toastService.openSuccessToast("đồng bộ thành công");
      this.loadAll();
    },error => {
      this.toastService.openErrorToast("Đồng bộ thất bại");
    })
  }

  onSearchData() {
    this.loadAll();
  }

  loadAll(){
    this.spinner.show();
    console.warn(343,this.form.value);
    this.issuesMemberService.findAll({
      page: this.page - 1,
      pageSize: this.itemsPerPage,
      // sort: this.sort(),
      codeProject:this.form.get('codeProject').value ? this.form.get('codeProject').value.trim() : '',
      status:this.form.get('status').value && this.form.get('status').value !== '--Tất cả--' ? this.form.get('status').value.trim() : '',
      author: this.form.get('author').value ? this.form.get('author').value.trim()  : '',
      assignee: this.form.get('assignee').value ? this.form.get('assignee').value.trim()  : ''
    }).subscribe(
      res => {
        console.warn(319,res);
        this.spinner.hide();
        this.paginateUserList(res);
      },
      err => {
        this.spinner.hide();
        this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
      }
    )
  }

  private paginateUserList(res) {
    this.totalItems = res.dataCount;
    this.issuesMemberModel = res.data;
  }

  findAllStatus(){
    this.issuesMemberService.findAllStatus().subscribe(res=>{
      this.statusList=res.listStatus;
      this.listPriority=res.listPriority;
      this.listSeverity=res.listSeverity;
    });
  }

  //open model
  openModal(item, statusList){
    const modalRef = this.modalService.open(IssuesMemberDetailComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.data = item;
    modalRef.componentInstance.statusList= statusList;
    modalRef.componentInstance.listPriority= this.listPriority;
    modalRef.componentInstance.listPriority = this.listPriority;
  }
}
