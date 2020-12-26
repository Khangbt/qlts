import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {NgbActiveModal, NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CommonService} from "app/shared/services/common.service";
import {ToastService} from "app/shared/services/toast.service";
import {NgxSpinnerService} from "ngx-spinner";
import {JhiEventManager} from "ng-jhipster";
import {HeightService} from "app/shared/services/height.service";
import {SysUserService} from "app/core/services/system-management/sys-user.service";
import {TranslateService} from "@ngx-translate/core";
import {DatePipe} from "@angular/common";
import {RiskListService} from "app/core/services/risk-list/risk-list.service";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {ModalConfirmRiskComponent} from "app/modules/system-categories/project-management/risk-list/modal-confirm-risk/modal-confirm-risk.component";
import {NgSelectComponent} from "@ng-select/ng-select";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'jhi-add-risk',
  templateUrl: './add-risk.component.html',
  styleUrls: ['./add-risk.component.scss']
})
export class AddRiskComponent implements OnInit, AfterViewInit {
  @ViewChild('ngSelectID', {static: false}) ngSelectID: NgSelectComponent;
  @ViewChild('ngSelectName', {static: false}) ngSelectName: NgSelectComponent;
  type: string;
  data: any;
  riskId: any;
  ngbModalRef: NgbModalRef;
  form: FormGroup;
  listStatusFix = [];
  listProject = [];
  listHumanResources = [];
  listSeverity = [];
  listOverviewStatus = [];
  listRiskType = [];
  checkFixedStatus: boolean;
  checkDeadlineRequired: any;
  checkDeadLineValid: any;
  checkHistory : any;
  height: number;
  name: string;
  isError = false;
  minDate = new Date(2010, 1, 1);
  maxDate = new Date().getFullYear();
  idDev: number;
  isQM = false;
  listHistory: any[];

  constructor(
    public activeModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private commonService: CommonService,
    private toastService: ToastService,
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private heightService: HeightService,
    private sysUserService: SysUserService,
    private translateService: TranslateService,
    private datepipe: DatePipe,
    private riskListService: RiskListService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
    this.activatedRoute.queryParams.subscribe(res => {
      this.riskId = res.id
    });
    this.activatedRoute.params.subscribe(
      res => {
        this.type = res.type;
      });
    this.height = this.heightService.onResize();
  }

  ngOnInit() {
    this.getHistory();
    this.getStatusOverView();
    this.buildForm();
    this.getListProject();
    this.getSeverityList();
    this.getRiskType();
    this.getStatusList();
    this.onCheckValidDateTime();
    this.setDefaultRiskDateValue();
  }

  heightResize() {
    this.height = this.type === 'add' ? this.heightService.onResize() : this.heightService.onResize() - 200;
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      id: [null],
      fixedStatus: [null],
      riskContent: [null],
      riskExplain: [null],
      riskNo: [null],

      reason: [null],
      modifiedDate: [null],
      createdDate: [null],
      createdBy: [null],
      modifiedBy: [null],
      statusConfirm: [null],

      actualDate: [null],
      noteAction: [null],
      lessonLearn: [null],
      riskDate: [null],
      deadline: [null],
      fixer: [null],
      fixedBy: [null],
      leader: [null],
      overViewStatus: [null],
      riskType: [null],
      severity: [null],
      projectId: [null],
      note: ['', Validators.maxLength(500)],
    });
    if (this.riskId) {
      this.getDataRiskList(this.riskId);
    }
  }

  getDataRiskList(riskId) {
    this.riskListService.findOne(riskId).subscribe(
      next => {
        const d: any = next;
        this.data = d.data;
        this.form.patchValue(this.data);
        this.getParName(this.data);
        this.getLeaderFromProject(this.data);
        this.getListHumanResources(this.data);
        if (this.data.fixedStatus === 'Xác nhận đã được khắc phục') {
          this.checkFixedStatus = true;
        }
        if (null != this.data.actualDate) {
          this.setValueToField('actualDate', this.formatDateShowView(this.data.actualDate));
        }
      }
    );
  }

  get formControl() {
    return this.form.controls;
}

  ngAfterViewInit(): void {
    if (this.type === 'add') {
      this.ngSelectID.focus();
    } else if (this.type === 'update') {
      this.ngSelectName.focus();
    }
  }

  closeModal() {
    this.activeModal.dismiss(false);
  }

  formatDateString(value) {
    const date = new Date(value);
    return this.datepipe.transform(date, 'yyyy-MM-dd');
  }

  formatDateShowView(value) {
    const date = new Date(value);
    return this.datepipe.transform(date, 'dd/MM/yyyy')
  }

  formatStringDate(value) {
    return new Date(value);
  }

  onChangeDeadlineValid() {
    const deadLine = (this.formatDateString(this.form.value.deadline));
    const deadLineDate = this.formatStringDate(deadLine);
    if (this.form.value.deadline != null) {
      if (this.form.value.riskDate != null) {
        const riskDate = (this.formatDateString(this.form.value.riskDate));
        const riskDateDate = this.formatStringDate(riskDate);
        this.checkDeadlineRequired = deadLineDate.getTime() >= riskDateDate.getTime();
      }
    } else {
      this.checkDeadLineValid = true;
    }
  }

  onCheckValidDateTime() {
    this.onChangeDeadlineValid();
  }

  trimSpaceValue(value) {
    this.setValueToField(value, this.getValueOfField(value).trim());
  }

  onSubmitForm() {
    if (this.type === 'update') {
      if (this.data.fixedStatus === 'Xác nhận đã được khắc phục') {
        this.toastService.openErrorToast('Không được phép sửa bản ghi có trạng thái Xác nhận đã xử lý');
        this.router.navigate(['/system-categories/project-management/risk-list']);
        return;
      }
      // if (this.data.fixedStatus === 'Từ chối xác nhận') {
      //   this.toastService.openErrorToast('Không được phép sửa bản ghi có trạng thái Từ chối xác nhận');
      //   this.router.navigate(['/system-categories/project-management/risk-list']);
      //   return;
      // }
    }
    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      return;
    }
    if (this.checkDeadlineRequired === false) {
      return;
    }
    this.spinner.show();
    if (this.type === 'update') {
      this.form.value.id = this.data.id;
      this.form.value.reason = this.data.reason;
      this.form.value.modifiedDate = this.data.modifiedDate;
      this.form.value.createdDate = this.data.createdDate;
      this.form.value.createdBy = this.data.createdBy;
      this.form.value.modifiedBy = this.data.modifiedBy;
      this.form.value.statusConfirm = this.data.statusConfirm;
    }
    this.trimSpace('riskContent');
    this.trimSpace('riskExplain');
    this.trimSpace('noteAction');
    this.trimSpace('lessonLearn');
    this.trimSpace('note');
    this.form.value.deadline = this.datepipe.transform(this.form.value.deadline, 'yyyy-MM-dd');
    this.form.value.riskDate = this.datepipe.transform(this.form.value.riskDate, 'yyyy-MM-dd');
    if (this.form.value.actualDate != null) {
      this.form.value.actualDate = this.formatStringDate(this.form.value.actualDate);
    }
    this.riskListService.addRiskList(this.form.value).subscribe(
      res => {
        this.toastService.openSuccessToast(this.type === 'add' ? 'Thêm mới thành công' : 'Sửa thành công');
        this.router.navigate(['/system-categories/project-management/risk-list']);
        this.spinner.hide();
      },
      error => {
        this.toastService.openErrorToast(this.type === 'add' ? 'Thêm mới không thành công' : 'Sửa thất bại');
        this.router.navigate(['/system-categories/project-management/risk-list']);
        this.spinner.hide();
      },
      () => {
        this.router.navigate(['/system-categories/project-management/risk-list']);
        this.spinner.hide();
      }
    );
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  getValueOfField(item) {
    return this.form.get(item).value;
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }

  onResize() {
    this.height = this.heightService.onResize();
  }

  trimSpace(element) {
    const value = this.getValueOfField(element);
    if (value) {
      this.setValueToField(element, value.trim());
    }
  }

  onCancel() {
    if (this.type === 'update') {
      if (
        this.form.value.projectId === this.data.projectId &&
        this.form.value.fixedBy === this.data.fixedBy &&
        this.form.value.riskDate === this.data.riskDate &&
        this.form.value.deadline === this.data.deadline &&
        this.form.value.severity === this.data.severity &&
        this.form.value.riskType === this.data.riskType &&
        this.form.value.fixedStatus === this.data.fixedStatus &&
        this.form.value.riskContent === this.data.riskContent &&
        this.form.value.riskExplain === this.data.riskExplain &&
        this.form.value.noteAction === this.data.noteAction &&
        this.form.value.lessonLearn === this.data.lessonLearn &&
        this.form.value.note === this.data.note
      ) {
        this.router.navigate(['/system-categories/project-management/risk-list']);
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.router.navigate(['/system-categories/project-management/risk-list']);
          }
        });
      }
    }
    if (this.type === 'add') {
      if (
        this.form.value.projectId === null &&
        this.form.value.fixedBy === null &&
        this.formatDateString(this.form.value.riskDate) === this.formatDateString(new Date()) &&
        this.form.value.deadline === null &&
        this.form.value.severity === null &&
        this.form.value.riskType === null &&
        this.form.value.fixedStatus === null &&
        this.form.value.riskContent === null &&
        this.form.value.riskExplain === null &&
        this.form.value.noteAction === null &&
        this.form.value.lessonLearn === null &&
        this.form.value.note === ''
      ) {
        this.router.navigate(['/system-categories/project-management/risk-list']);
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.router.navigate(['/system-categories/project-management/risk-list']);
          }
        });
      }
    }
    if (this.type === 'detail') {
      this.router.navigate(['/system-categories/project-management/risk-list']);
    }
  }

  getStatusList() {
    this.riskListService.getListStatusFix().subscribe(
      res => {
        const d: any = res;
        this.listStatusFix = d.data;
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  getRiskType() {
    this.riskListService.getRiskType().subscribe(
      res => {
        const d: any = res;
        this.listRiskType = d.data;
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  getSeverityList() {
    this.riskListService.getByParType('SEVERITY').subscribe(
      res => {
        const d: any = res;
        this.listSeverity = d.data;
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  getStatusOverView() {
    this.riskListService.getByParType('StatusO').subscribe(
      res => {
        const d: any = res;
        this.listOverviewStatus = d.data;
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  getListProject() {
    this.riskListService.getListProject().subscribe(
      res => {
        const d: any = res;
        this.listProject = d.data;
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  getDataOnSelectProject(event) {
    this.getListHumanResources(event);
    this.getParName(event);
    this.getLeaderFromProject(event);
  }

  getListHumanResources(event) {
    this.riskListService.getHumanResources(event.projectId).subscribe(
      res => {
        const d: any = res;
        this.listHumanResources = d.data;
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  getParName(event) {
    this.riskListService.getParName(event.projectId).subscribe(
      res => {
        const d: any = res;
        this.setValueToField("overViewStatus", d.data);
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  confirm(status) {
    const modalRef = this.modalService.open(ModalConfirmRiskComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false,
      centered: true
    });
    modalRef.componentInstance.status = status;
    modalRef.componentInstance.data = this.data;
  }

  setDefaultRiskDateValue() {
    if (this.type === 'add') {
      this.setValueToField("riskDate", new Date());
    }
  }

  getLeaderFromProject(event) {
    this.riskListService.getLeader(event.projectId).subscribe(
      res => {
        const d: any = res;
        this.setValueToField("leader", d.data);
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  getHistory() {
    if (this.type !== 'add' && this.riskId) {
      this.riskListService.getHistoryByID(this.riskId).subscribe(success => {
        this.listHistory = success;
      });
    }
  }
  viewChangeStr(item){
    const newValue = JSON.parse(item.valueNew);
    const oldValue = JSON.parse(item.valueOld);
    let result='';
    if(!newValue||!oldValue){
      return;
    }
    if(item.action === 10){
      this.checkHistory = 1;
      result = "["+newValue.riskNo+"]";
      if(newValue.reason != ""){
        result += " với lý do "+"["+newValue.reason+"]";
      }
      return result;
    }else if(item.action === 11){
      this.checkHistory = 2;
      result = "["+newValue.riskNo+"]"+" với lý do "+"["+newValue.reason+"]";
      return result;
    }else{
      this.checkHistory = 3;
    }

    if(newValue.fixedBy!==oldValue.fixedBy){
      if(this.listHumanResources&&this.listHumanResources.length>0) {
        const newFixBy = this.listHumanResources.find(value => {
          return value.humanResourceId === newValue.fixedBy;
        });
        const oldFixBy = this.listHumanResources.find(value => {
          return value.humanResourceId === oldValue.fixedBy;
        });
        result += (result ? ' ; ' : '') +'Người khắc phục từ [' + (oldFixBy?oldFixBy.code:'') + '] thành [' + (newFixBy?newFixBy.code:'')+' ]';
      }
    }

    if(newValue.riskDate!==oldValue.riskDate){
      const newRiskDate = (newValue.riskDate) ? new Date(newValue.riskDate).toLocaleDateString() :'';
      const oldRiskDate = (oldValue.riskDate) ? new Date(oldValue.riskDate).toLocaleDateString() :'';
      if(newRiskDate!==oldRiskDate){
        result += (result ? ' ; ' : '') + 'Thời gian phát hiện rủi ro từ ['+oldRiskDate+ '] thành ['+ newRiskDate+' ]';
      }
    }
    if(newValue.deadline!==oldValue.deadline){
      const newDeadline = (newValue.deadline) ? new Date(newValue.deadline).toLocaleDateString() :'';
      const oldDeadline = (oldValue.deadline) ? new Date(oldValue.deadline).toLocaleDateString() :'';
      if(newDeadline!==oldDeadline){
        result += (result ? ' ; ' : '') + 'Thời gian xử lý từ ['+oldDeadline+ '] thành ['+ newDeadline+' ]';
      }
    }
    if(newValue.severity!==oldValue.severity){
      result += (result ? ' ; ' : '') + 'Mức độ nghiêm trọng từ ['+oldValue.severity+ '] thành ['+ newValue.severity+' ]';
    }
    if(newValue.riskType!==oldValue.riskType){
      result += (result ? ' ; ' : '') +'Phân loại vấn đề từ ['+ oldValue.riskType+ '] thành ['+ newValue.riskType+' ]';
    }
    newValue.riskContent = (newValue.riskContent)?newValue.riskContent.trim():'';
    oldValue.riskContent = (oldValue.riskContent)?oldValue.riskContent.trim():'';
    if(newValue.riskContent!==oldValue.riskContent){
      result += (result ? ' ; ' : '') +'Rủi ro vấn đề từ ['+ oldValue.riskContent+ '] thành ['+ newValue.riskContent+' ]';
    }
    newValue.riskExplain=newValue.riskExplain ?newValue.riskExplain.trim() :'';
    oldValue.riskExplain=oldValue.riskExplain ?oldValue.riskExplain.trim() :'';
    if(newValue.riskExplain!==oldValue.riskExplain){
      result += (result ? ' ; ' : '') +'Giải trình rủi ro/ Vấn đề từ ['+ oldValue.riskExplain+ '] thành ['+ newValue.riskExplain+' ]';
    }
    newValue.noteAction=newValue.noteAction ?newValue.noteAction.trim() :'';
    oldValue.noteAction=oldValue.noteAction ?oldValue.noteAction.trim() :'';
    if(newValue.noteAction!==oldValue.noteAction){
      result += (result ? ' ; ' : '') +'Hành động khắc phục từ ['+ oldValue.noteAction+ '] thành ['+ newValue.noteAction+' ]';
    }
    newValue.lessonLearn=newValue.lessonLearn ?newValue.lessonLearn.trim() :'';
    oldValue.lessonLearn=oldValue.lessonLearn ?oldValue.lessonLearn.trim() :'';
    if(newValue.lessonLearn!==oldValue.lessonLearn){
      result += (result ? ' ; ' : '') +'Bài học kinh nghiệm từ ['+ oldValue.lessonLearn+ '] thành ['+ newValue.lessonLearn+' ]';
    }
    newValue.note=newValue.note ?newValue.note.trim() :'';
    oldValue.note=oldValue.note ?oldValue.note.trim() :'';
    if(newValue.note!==oldValue.note){
      result += (result ? ' ; ' : '') +'Ghi chú từ ['+ oldValue.note+ '] thành ['+ newValue.note+' ]';
    }
    return result;
  }



}
