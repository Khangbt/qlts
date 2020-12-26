import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal, NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {JhiEventManager} from "ng-jhipster";
import {HeightService} from "app/shared/services/height.service";
import {NgxSpinnerService} from "ngx-spinner";
import {Router} from "@angular/router";
import {ToastService} from "app/shared/services/toast.service";
import {TranslateService} from "@ngx-translate/core";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {Observable, of, Subject} from "rxjs";
import {CommonService} from "app/shared/services/common.service";
import {SysUserService} from "app/core/services/system-management/sys-user.service";
import {DatePipe} from "@angular/common";
import {debounceTime} from "rxjs/operators";
import {TIME_OUT} from "app/shared/constants/set-timeout.constants";
import {ITEMS_PER_PAGE} from "app/shared/constants/pagination.constants";
import {HttpResponse} from "@angular/common/http";
import {STATUS_CODE} from "app/shared/constants/status-code.constants";
import {REGEX_PATTERN, REGEX_REPLACE_PATTERN} from "app/shared/constants/pattern.constants";
import {SupplierServieceService} from "app/core/services/Supplier-service/supplier-serviece.service";

@Component({
  selector: 'jhi-add-supplier-resource',
  templateUrl: './add-supplier-resource.component.html',
  styleUrls: ['./add-supplier-resource.component.scss']
})
export class AddSupplierResourceComponent implements OnInit {
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
  title ="";
  constructor(
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
    this.getDeparmentList();
    this.getPositionList();
    this.debounceOnSearch5();
    this.getSeverityList();
    this.loadDataHuman();

  }

  private buildForm() {
    if (this.type === 'add') {
      this.title = "Thêm danh mục nhà cung cấp";
    }else if(this.type ==="update"){
      this.title = "Sửa danh mục nhà cung cấp";
    }else
      this.title = "Xem chi tiết danh mục nhà cung cấp";

    this.form = this.formBuilder.group({
      code: ['', Validators.compose([Validators.required, Validators.maxLength(50), Validators.pattern(/^[a-zA-Z0-9]+$/)])],
      email: ['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      note: ['', Validators.maxLength(1000)],
      phone: ['', Validators.compose([Validators.required])],
      website: [null],
      address: ['', Validators.compose([Validators.required])],
      nameHummer: ['', Validators.compose([Validators.required])],
      fullName: ['', Validators.compose([Validators.required])],
      fax: [''],
      supplierId: [this.id]
    });

    if (this.id) {
      this.getUserDetail(this.id);
    }

  }

  getDefaultData() {

    console.warn("1111" + this.id + this.type)
    if (this.type && this.id) {
      this.commonService.clearDataTranfer('type');
      this.commonService.clearDataTranfer('id');
    } else {
      if (this.type !== 'add') {
        this.router.navigate(['system-categories/supplier-resources']);
      }

    }
  }

  setDataDefault() {
    this.form.patchValue(this.userDetail);


  }

  getUserDetail(id) {
    this.supplierServieceService.getInfo(id).subscribe(
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

  getDeparmentList() {
    this.departmentList = []
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
    if (this.form.value.code === "") {
      this.isDuplicateUserCode = false;
      return;
    }
    if (this.type === 'add') {
      this.supplierServieceService.checkUserCode(this.form.value.code).subscribe(res => {

        this.isDuplicateUserCode = false;

      }, err => {
        this.isDuplicateUserCode = true;
      });
    }
  }



  onCancel() {
    if (this.type === 'update') {
      if (
        this.form.value.code === this.userDetail.code&&
        this.form.value.email === this.userDetail.email&&
        this.form.value.note === this.userDetail.note&&
        this.form.value.phone === this.userDetail.phone&&
        this.form.value.website === this.userDetail.website&&
        this.form.value.address === this.userDetail.address&&
        this.form.value.nameHummer === this.userDetail.nameHummer&&
        this.form.value.fullName === this.userDetail.fullName&&
        this.form.value.fax === this.userDetail.fax

      ) {
        this.activeModal.dismiss();
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.activeModal.dismiss();
            this.router.navigate(['system-categories/supplier-resources']);
          }
        });
      }
    }
    if (this.type === 'add') {
      if (
        this.form.value.code === ''&&
        this.form.value.email ===  ''&&
        this.form.value.note ===  ''&&
        this.form.value.phone ===  ''&&
        this.form.value.website ===  null&&
        this.form.value.address ===  ''&&
        this.form.value.nameHummer ===  ''&&
        this.form.value.fullName ===  ''&&
        this.form.value.fax ===  ''
      ) {
        this.activeModal.dismiss();
        this.router.navigate(['system-categories/supplier-resources']);
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.router.navigate(['system-categories/supplier-resources']);
            this.activeModal.dismiss();
          }
        });
      }
    }
    if (this.type === 'detail') {
      this.activeModal.dismiss();
      this.router.navigate(['system-categories/supplier-resources']);
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

    this.supplierServieceService.save(this.form.value).subscribe(
      res => {

        if (this.type === 'add') {
          this.toastService.openSuccessToast('Thêm mới thành công !');
          this.activeModal.dismiss();
        }
        if (this.type === 'update') {

          this.toastService.openSuccessToast('Sửa thành công !');
          this.activeModal.dismiss();
        }
        this.router.navigate(['system-categories/supplier-resources']);
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
    this.debouncer5.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit5(value));
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

  getSeverityList() {
    this.humanResourceService.getByParType('NCC').subscribe(
      res => {
        const d: any = res;
        this.listNCC = d.data;
      },
      err => {
        this.listNCC = []
      }
    )
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

}

