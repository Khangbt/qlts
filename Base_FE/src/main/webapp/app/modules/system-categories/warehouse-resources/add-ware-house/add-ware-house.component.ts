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
import {WarehouseServicesService} from "app/core/services/warehouse/warehouse-services.service";
import {PartServiceService} from "app/core/services/part-sercvice/part-service.service";
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";

@Component({
  selector: 'jhi-add-ware-house',
  templateUrl: './add-ware-house.component.html',
  styleUrls: ['./add-ware-house.component.scss']
})
export class AddWareHouseComponent implements OnInit {
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
  listProvince:any =[];
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
  checkBoll = false;
  title ="";

  constructor(
    private  partServiceService :PartServiceService,
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
    private warehouseServicesService: WarehouseServicesService,
    private formStoringService: FormStoringService,
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
    this.getPartList();
  }

  private buildForm() {
    if (this.type === 'add') {
      this.title = "Thêm  kho tài sản";
    }else if(this.type ==="update"){
      this.title = "Sửa kho tài sản ";
    }else
      this.title = "Xem chi tiết kho tài sản";
    this.form = this.formBuilder.group({
      code: ['', Validators.compose([Validators.required, Validators.maxLength(50), Validators.pattern(/^[a-zA-Z0-9]+$/)])],
      fullName: ['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      partId: [null, Validators.required],
      note: ['', Validators.maxLength(1000)],
      address: [null, Validators.required],
      iD:[this.id],

    });

    if (this.id) {
      this.getUserDetail(this.id);
      this.xetDataUer()
    }else {
      this.xetDataUer()
    }

  }

  getDefaultData() {
    console.warn("1111"+this.type+this.id)
    if (this.type && this.id) {
      this.commonService.clearDataTranfer('type');
      this.commonService.clearDataTranfer('id');
    } else {
      if (this.type !== 'add') {
        this.router.navigate(['system-categories/warehouse-resources']);
      }

    }
  }

  setDataDefault() {
    this.form.patchValue(this.userDetail);

  }

  getUserDetail(id) {
    this.warehouseServicesService.getInfo(id).subscribe(
      res => {
        this.userDetail = res.data;

        this.oldEmail = this.userDetail.email ? this.userDetail.email : '';

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



  getPositionList() {
    this.humanResourceService.getPositionList().subscribe(
      res => {
        if (res) {
          this.positionList = res.data;
        } else {
          this.positionList = [];
        }
      },
      err => {
        this.positionList = [];
      }
    );
  }



  onBlurEmail(field) {
    this.setValueToField(field, this.getValueOfField(field).trim());
    if (!REGEX_PATTERN.EMAIL.test(this.getValueOfField(field))) {
      if (this.getValueOfField(field) !== '') {
        this.form.controls[field].setErrors({invalid: true});
      }
    } else {
      if (this.getValueOfField(field) !== '') {
        // check trùng
        if (this.type === 'add' || (this.type === 'update' && this.userDetail.email !== this.form.value.email)) {
          this.humanResourceService.checkEmail(this.form.value.email).subscribe(res => {
            this.isDuplicateEmail = false;
          }, err => {
            this.isDuplicateEmail = true;
          });
        }
      }
    }
  }

  onBlurUserCode() {
    if (this.type === 'add') {
      this.warehouseServicesService.checkUserCode(this.form.value.code).subscribe(res => {

        this.isDuplicateUserCode = false;

      }, err => {
        this.isDuplicateUserCode = true;
      });
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
        this.form.value.code === this.userDetail.code &&
        this.form.value.fullName === this.userDetail.fullName &&
        this.form.value.address ===this.userDetail.address &&
        this.form.value.note === this.userDetail.note&&
        this.form.value.partId===this.userDetail.partId
      ) {
        this.activeModal.dismiss();
        this.router.navigate(['system-categories/warehouse-resources']);

      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.activeModal.dismiss();
            this.router.navigate(['system-categories/warehouse-resources']);
          }
        });
      }
    }
    if (this.type === 'add') {
      if (
        this.form.value.code === '' &&
        this.form.value.fullName === '' &&
        this.form.value.address === null &&
        this.form.value.note === ''&&
        this.checkNull()
      ) {
        this.activeModal.dismiss();

        this.router.navigate(['system-categories/warehouse-resources']);
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.activeModal.dismiss();
            this.router.navigate(['system-categories/warehouse-resources']);
          }
        });
      }
    }
    if (this.type === 'detail') {
      this.activeModal.dismiss();
      this.router.navigate(['system-categories/warehouse-resources']);
    }
  }

  onSubmitData() {
    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      return;
    }
    if(this.isDuplicateUserCode){
      return;
    }
    this.warehouseServicesService.save(this.form.value).subscribe(
      res => {

        if (this.type === 'add') {
          this.toastService.openSuccessToast('Thêm mới thành công !');
          this.activeModal.dismiss();
          this.debounceOnSearch();
        }
        if (this.type === 'update') {
          if (this.oldEmail !== this.form.value.email) {
            this.toastService.openSuccessToast('Sửa thành công');
            this.activeModal.dismiss();
            this.debounceOnSearch();
          } else {
            this.toastService.openSuccessToast('Sửa thành công !');
            this.activeModal.dismiss();
            this.debounceOnSearch();
          }
        }

        this.router.navigate(['system-categories/warehouse-resources']);
        /*this.spinner.hide();*/
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
    // || item.code.toLocaleLowerCase().indexOf(term) > -1
    // || item.email.toLocaleLowerCase().indexOf(term) > -1;
  }

  debounceOnSearch5() {
    // this.debouncer5.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit5(value));
  }


  loadDataOnSearchUnit5(term) {
    const data = {
      keySearch: term.trim().toUpperCase(),
      type: 'PARTNER'
    }
    this.humanResourceService.getHumanResourcesInfo(data).subscribe(res => {
      // this.organizationCategoriesService.getPartnerInfo(data).subscribe(res => {
      if (this.searchHuman) {
        const dataRes: any = res;

        this.listHuman = of(dataRes.sort((a, b) => a.fullName.localeCompare(b.fullName)));
        // this.listHuman = of(dataRes.sort((a, b) => a.code.localeCompare(b.code)));

      } else {
        this.listHuman = of([]);
      }
    });
  }

  onClearUnit5() {
    this.listHuman = of([]);
    this.searchHuman = '';
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
      // this.organizationCategoriesService.getPartnerInfo(data).subscribe(res => {
      const dataRes: any = res;
      this.listHuman = of(dataRes.sort((a, b) => a.fullName.localeCompare(b.fullName)));
      // this.listHuman = of(dataRes.sort((a, b) => a.code.localeCompare(b.code)));
    });
  }



  // lay du lieu bo phan
  getPartList() {
    this.humanResourceService.getPartList().subscribe(
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
  xetDataUer() {
    const userToken: any = this.formStoringService.get(STORAGE_KEYS.USER);
    if (userToken.role === "ROLE_ADMINPART") {
      this.form.get("partId").setValue(userToken.partId)
      this.checkBoll = true
    }else {
      this.checkBoll=false
    }


  }
  checkNull(){
    if(this.checkBoll){
      return true
    }
    if ( this.form.value.partId===null&&!this.checkBoll){
      return true
    }
    return false
  }
}
