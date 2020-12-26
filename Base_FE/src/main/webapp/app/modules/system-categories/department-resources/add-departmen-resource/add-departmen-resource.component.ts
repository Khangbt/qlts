import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal, NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable, of, Subject} from "rxjs";
import {HeightService} from "app/shared/services/height.service";
import {CommonService} from "app/shared/services/common.service";
import {ToastService} from "app/shared/services/toast.service";
import {NgxSpinnerService} from "ngx-spinner";
import {JhiEventManager} from "ng-jhipster";
import {SysUserService} from "app/core/services/system-management/sys-user.service";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {TranslateService} from "@ngx-translate/core";
import {DatePipe} from "@angular/common";
import {Router} from "@angular/router";
import {SupplierServieceService} from "app/core/services/Supplier-service/supplier-serviece.service";
import {toNumber} from "ng-zorro-antd";
import {debounceTime} from "rxjs/operators";
import {TIME_OUT} from "app/shared/constants/set-timeout.constants";
import {ITEMS_PER_PAGE} from "app/shared/constants/pagination.constants";
import {HttpResponse} from "@angular/common/http";
import {STATUS_CODE} from "app/shared/constants/status-code.constants";
import {REGEX_PATTERN, REGEX_REPLACE_PATTERN} from "app/shared/constants/pattern.constants";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {PartServiceService} from "app/core/services/part-sercvice/part-service.service";

@Component({
  selector: 'jhi-add-departmen-resource',
  templateUrl: './add-departmen-resource.component.html',
  styleUrls: ['./add-departmen-resource.component.scss']
})
export class AddDepartmenResourceComponent implements OnInit {
  oldEmail: any;
  @Input() type;
  @Input() id: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter()
  ngbModalRef: NgbModalRef;
  form: FormGroup;
  listUnit$ = new Observable<any[]>();
  unitSearch;
  debouncer: Subject<string> = new Subject<string>();
  departmentList: any[] = [];
  partList: any[] = [];
  majorList: any[] = [];
  positionList: any[] = [];
  historyList: any[] = [];
  majorRequired = false;
  isDuplicateEmail = false;
  isDuplicateUserCode = false;
  height: number;
  name: string;
  maxlength = 4;
  isError = false;
  userDetail: any;
  dateRecruitmentValid = false;
  dateGraduateValid = false;
  dateMajorValid = false;
  idDev: number;
  isYear = false;
  yy: number;
  years: number[] = [];
  debouncer5: Subject<string> = new Subject<string>();
  listHuman = new Observable<any[]>();
  searchHuman;
  listNCC = [];
  listProvince: any = [];
  listStatus: any = [
    {name: 'Hoạt động', value: 'ACTIVE'}, {
      name: 'Không hoạt động', value: 'INACTIVE'
    }]

  constructor(
    private  partServiceService: PartServiceService,
    public activeModal: NgbActiveModal,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private commonService: CommonService,
    private toastService: ToastService,
    private spinner: NgxSpinnerService,
    private modalService: NgbModal,
    private eventManager: JhiEventManager,
    private sysUserService: SysUserService,
    private humanResourceService: HumanResourcesApiService,
    private translateService: TranslateService,
    private datepipe: DatePipe,
    protected router: Router,
    private supplierServieceService: SupplierServieceService,
  ) {
    this.height = this.heightService.setMenuHeight()
  }

  get formControl() {
    return this.form.controls;
  }

  ngOnInit() {
    this.getDefaultData();

    this.buildForm();
    this.debounceOnSearch();
    this.debounceOnSearch5();
    this.getSeverityList();
    this.loadDataHuman();
    this.getProvince();
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      note: ['', Validators.maxLength(1000)],
      status: ['ACTIVE'],
      partName: ['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      partnerID: [this.id],
      code: ['', Validators.compose([Validators.required, Validators.maxLength(255)])]
    });

    if (this.id) {
      this.getUserDetail(this.id);
    }

  }

  getDefaultData() {
    // this.type = this.commonService.getDataTranfer('type');
    // this.id = this.commonService.getDataTranfer('id');
    console.warn("data" + this.type + this.id)
    if (this.type && this.id) {
      this.commonService.clearDataTranfer('type');
      this.commonService.clearDataTranfer('id');
    } else {
      if (this.type !== 'add') {
        this.router.navigate(['system-categories/department-resources']);
      }

    }
  }

  setDataDefault() {
    this.form.patchValue(this.userDetail);
    if (this.userDetail.dateGraduate) {
      this.form.get('experience').setValue(new Date().getFullYear() - toNumber(this.userDetail.dateGraduate));
    }
    if (this.userDetail.dateMajor) {
      this.form.get('majorExperience').setValue(new Date().getFullYear() - toNumber(this.userDetail.dateMajor));
    }


  }

  getUserDetail(id) {
    this.partServiceService.getInfo(id).subscribe(
      res => {
        this.userDetail = res.data;


        this.setDataDefault();
      }, err => {
        this.userDetail = null;
      }
    )
  }

  debounceOnSearch() {
    this.debouncer.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit(value));
  }

  loadDataOnSearchUnit(term) {
    this.sysUserService
      .getUnit({
        name: term,
        limit: ITEMS_PER_PAGE,
        page: 0
      })
      .subscribe((res: HttpResponse<any[]>) => {
        if (res && res.status === STATUS_CODE.SUCCESS && this.unitSearch) {
          this.listUnit$ = of(res.body['content'].sort((a, b) => a.name.localeCompare(b.name)));
        } else {
          this.listUnit$ = of([]);
        }
      });
  }


  dateGraduate() {
    if (this.form.value.dateGraduate) {

      if (this.form.value.dateGraduate > new Date().getFullYear()) {
        this.dateGraduateValid = true;
        this.form.get("dateGraduate").reset();
        this.form.get("experience").reset();
      } else {
        const experience = new Date().getFullYear() - this.form.value.dateGraduate;
        this.form.get('experience').setValue(experience);
        this.dateGraduateValid = false;
      }

    } else {
      return;
    }
  }

  onBlurUserCode() {
    if (this.type === 'add') {
      this.partServiceService.checkUserCode(this.form.value.code).subscribe(res => {

        this.isDuplicateUserCode = false;

      }, err => {
        this.isDuplicateUserCode = true;
      });
    }
  }

  dateMajor() {

    if (this.form.value.dateMajor) {

      if (this.form.value.dateMajor > new Date().getFullYear()) {
        this.dateMajorValid = true;
        this.form.get("dateMajor").reset();
        this.form.get("majorExperience").reset();
      } else {
        const majorExperience = new Date().getFullYear() - this.form.value.dateMajor;
        this.form.get('majorExperience').setValue(majorExperience);
        this.dateMajorValid = false;
      }
    } else {
      return;
    }
  }


  clearMajor() {
    if (this.form.value.departmentId !== this.idDev) {
      return this.form.get('majorId').reset();
    }
  }

  onCancel() {
    if (this.type === 'update') {
      if (
        this.userDetail.code === this.form.value.code &&
        this.userDetail.partName === this.form.value.partName &&
        this.userDetail.note === this.form.value.note &&
        this.userDetail.status === this.form.value.status

      ) {
        this.activeModal.dismiss();
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.activeModal.dismiss();
            this.router.navigate(['system-categories/department-resources']);
          }
        });
      }
    }
    if (this.type === 'add') {
      if (
        this.form.value.code === '' &&
        this.form.value.partName === '' &&
        this.form.value.note === '' &&
        this.form.value.status === 'ACTIVE'
      ) {
        this.activeModal.dismiss();
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.activeModal.dismiss();
            this.router.navigate(['system-categories/department-resources']);
          }
        });
      }
    }
    if (this.type === 'detail') {
      this.activeModal.dismiss();
      this.router.navigate(['system-categories/department-resources']);
    }
  }

  onSubmitData() {


    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      return;
    }

    if (this.isDuplicateUserCode){
      return;
    }
    this.partServiceService.save(this.form.value).subscribe(
      res => {
        if (this.type === 'add') {
          this.toastService.openSuccessToast('Thêm mới thành công !');
          console.warn("vao day 12345")

        }
        if (this.type === 'update') {
          this.toastService.openSuccessToast('Sửa thành công !');
          console.warn("vao day 123")

        }
        console.warn("vao day 123")
        this.router.navigate(['system-categories/department-resources']);
        this.activeModal.dismiss();
      },
      err => {
        this.toastService.openErrorToast(this.type === 'add' ? 'Thêm mới không thành công' : 'Sửa thất bại');
        this.spinner.hide();
      },
      () => {
        this.spinner.hide();
      }
    );
  }

  onResize() {
    this.height = this.heightService.setMenuHeight();
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }

  trimSpace(element) {
    const value = this.getValueOfField(element);
    if (value) {
      this.setValueToField(element, value.trim());

    }
  }

  getValueOfField(item) {
    return this.form.get(item).value;
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
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

  }

  debounceOnSearch5() {
    this.debouncer5.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit5(value));
  }


  loadDataOnSearchUnit5(term) {
    const data = {
      keySearch: term.trim().toUpperCase(),
      type: 'PARTNER'
    }
    this.humanResourceService.getHumanResourcesInfo(data).subscribe(res => {
      if (this.searchHuman) {
        const dataRes: any = res;

        this.listHuman = of(dataRes.sort((a, b) => a.fullName.localeCompare(b.fullName)));

      } else {
        this.listHuman = of([]);
      }
    });
  }

  onClearUnit5() {
    this.listHuman = of([]);
    this.searchHuman = '';
  }

  getSeverityList() {

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

  loadDataHuman() {
    const data = {
      keySearch: "",
      type: 'PARTNER'
    }
    this.humanResourceService.getHumanResourcesInfo(data).subscribe(res => {
      const dataRes: any = res;
      this.listHuman = of(dataRes.sort((a, b) => a.fullName.localeCompare(b.fullName)));
    });
  }

// lay du lieu tinh thanh
  getProvince() {

  }
}
