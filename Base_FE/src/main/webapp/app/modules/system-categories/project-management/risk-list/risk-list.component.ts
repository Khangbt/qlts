import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Subscription} from "rxjs";
import {HeightService} from "app/shared/services/height.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {JhiEventManager} from "ng-jhipster";
import {ToastService} from "app/shared/services/toast.service";
import {TranslateService} from "@ngx-translate/core";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CommonService} from "app/shared/services/common.service";
import {SHOW_HIDE_COL_HEIGHT} from 'app/shared/constants/perfect-scroll-height.constants';
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {RiskListService} from "app/core/services/risk-list/risk-list.service"
import {RiskModel} from "app/core/models/risk-list/risk.model"
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from "app/shared/constants/pagination.constants";
import {AddRiskComponent} from "app/modules/system-categories/project-management/risk-list/add-risk/add-risk.component";
@Component({
  selector: 'jhi-risk-list',
  templateUrl: './risk-list.component.html',
  styleUrls: ['./risk-list.component.scss']
})
export class RiskListComponent implements OnInit, OnDestroy {
  token = '';
  form: FormGroup;
  listStatusFix = [];
  listRiskType = [];
  routeData: any;
  height: any;
  itemsPerPage: any;
  totalItems: any;
  maxSizePage: any;
  page: any;
  pagingParams;
  columns:any[];
  previousPage: any;
  predicate: any;
  reverse: any;
  eventSubscriber: Subscription;
  SHOW_HIDE_COL_HEIGHT = SHOW_HIDE_COL_HEIGHT;
  width: number;
  risks : RiskModel[];
  listHistory :any[];
  constructor(
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
    private riskListService: RiskListService,
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
    this.columns = this.riskListService.columns;
    this.loadAll();
    this.getStatusList();
    this.getRiskType();
    this.onResize();
    this.getHistory();
  }

  getRiskType(){
    this.riskListService.getListStatusFixOnSearch().subscribe(
      res => {
        const d: any = res;
        const all = {code: '', name: '--Tất cả--'}
        this.listStatusFix = d.data;
        this.listStatusFix = [all,...this.listStatusFix];
        console.warn(this.listStatusFix);
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  getStatusList(){
    this.riskListService.getRiskType().subscribe(
      res => {
        const d: any = res;
        const all = {code: '', name: '--Tất cả--'}
        this.listRiskType = d.data;
        this.listRiskType = [all,...this.listRiskType];
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  ngOnDestroy(): void {
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  onSearchData() {
    this.transition();
  }

  onDeleteRisk(data) {
    if(data.fixedStatus !== 'Chưa khắc phục'){
      this.toastService.openErrorToast("Trạng thái rủi ro/vấn đề khác 'Chưa khắc phục', không được phép xóa!");
    }else {
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'delete';
      modalRef.componentInstance.param = 'rủi ro';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.deleteRisk(data.id);
        }
      });
    }
  }


  deleteRisk(riskId) {
    this.riskListService.deleteRisk(riskId).subscribe(res => {
      this.spinner.show();
      if (res) {
        this.spinner.hide();
        this.toastService.openSuccessToast("Xóa rủi ro thành công!");
        this.loadAll();
      }
    }, error => {
      this.spinner.hide();
      this.toastService.openErrorToast("Trạng thái rủi ro/vấn đề khác 'Chưa khắc phục', không được phép xóa!");
    });
  }

  transition() {
    this.router.navigate(['/system-categories/risk-list'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
        project:this.form.get('project').value ? this.form.get('project').value : '',
        status:this.form.get('status').value && this.form.get('status').value !== '--Tất cả--' ? this.form.get('status').value : '',
        humanResources: this.form.get('humanResources').value ? this.form.get('humanResources').value : '',
        fixedBy: this.form.get('fixedBy').value ? this.form.get('fixedBy').value : '',
        classify: this.form.get('classify').value && this.form.get('classify').value !== '--Tất cả--' ? this.form.get('classify').value : '',
      }
    });
    this.loadAll();
  }

  openActionAddRiskList(type?: string, selectedData?: any) {
    this.router.navigate(['/system-categories/project-management/add-risk/' + type], {
      queryParams: {
        id: selectedData ? selectedData.id : null
      }
    });
  }

  loadAll(){
    this.spinner.show();
    this.riskListService.findAll({
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
      project:this.form.get('project').value ? this.form.get('project').value.trim() : '',
      status:this.form.get('status').value && this.form.get('status').value !== '--Tất cả--' ? this.form.get('status').value.trim() : '',
      humanResources: this.form.get('humanResources').value ? this.form.get('humanResources').value.trim() : '',
      fixedBy: this.form.get('fixedBy').value ? this.form.get('fixedBy').value.trim() : '',
      classify: this.form.get('classify').value && this.form.get('classify').value !== '--Tất cả--' ? this.form.get('classify').value.trim() : '',
    }).subscribe(
      res => {
        console.warn("asdfsdfasdfsd");
        this.spinner.hide();
        const d: any = res;
        this.paginateRiskList(d.data);
      },
      err => {
        this.spinner.hide();
        this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
      }
    )
  }

  private paginateRiskList(data) {
    this.totalItems = data.totalItems;
    this.risks = data.data;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
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

  private buidForm() {
    this.form = this.formBuilder.group({
      project: [''],
      status: ['--Tất cả--'],
      humanResources: [''],
      fixedBy: [''],
      classify: ['--Tất cả--'],
    });
  }

  openAddRisk(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddRiskComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.data = selectedData ? selectedData : null;
    modalRef.result.then(result => {
      if(result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });
  }

  toggleColumns(col) {
    col.isShow = !col.isShow;
    this.riskListService.columns = this.columns;
  }
  getHistory() {
    this.riskListService.getHistory().subscribe(success => {
      this.listHistory = success;
    });
  }
  convertJson(value){
    return JSON.parse(value);
  }
}
