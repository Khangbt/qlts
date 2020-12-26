import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ContractManagerService} from 'app/core/services/contract-management/contract-manager.service.ts';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastService} from 'app/shared/services/toast.service';
import {HeightService} from 'app/shared/services/height.service';
import {TranslateService} from '@ngx-translate/core';
import {NgxSpinnerService} from 'ngx-spinner';
import {ContractManagementService} from 'app/core/services/system-management/contract-management.service';
import {CustomerApiService} from "app/core/services/customer-api/customer-api.service.service";
import {CommonService} from "app/shared/services/common.service";
import {DatePipe} from "@angular/common";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NgSelectComponent} from "@ng-select/ng-select";
import {TIME_OUT} from "app/shared/constants/set-timeout.constants";

@Component({
  selector: 'jhi-add-partner',
  templateUrl: './add-partner.component.html',
  styleUrls: ['./add-partner.component.scss'],
  providers: [DatePipe]
})
export class AddPartnerComponent implements OnInit, AfterViewInit {

  @ViewChild('ngSelectCode', {static: false}) ngSelectCode: ElementRef;
  @ViewChild('ngSelectName', {static: false}) ngSelectName: ElementRef;

  @Input() id: any;
  type: String;
  height: number;
  form: FormGroup;
  isModalConfirmShow = false;
  routeData: any;
  page: any;
  CustomerDetail: any;
  isEdit = [];
  listStatus = [
    {id: 1, value: 'Đang hợp tác'},
    {id: 0, value: 'Không hợp tác'}
  ];
  isCooperated: number;
  status: number;
  isDuplicateUserCode = false;
  listHistory: any;
  formChange = false;

  constructor(
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private formBuilder: FormBuilder,
    private contractManagerService: ContractManagerService,
    private customerApiService: CustomerApiService,
    private commonService: CommonService,
    private service: ContractManagementService,
    private route: ActivatedRoute,
    private router: Router,
    private toastService: ToastService,
    private translateService: TranslateService,
    private heightService: HeightService,
  ) {
  }

  ngOnInit(): void {
    this.type = (this.route.snapshot.queryParamMap.get('id')) ? 'updateCustomer' : 'addCustomer';
    this.id = this.route.snapshot.queryParamMap.get('id');
    if (this.type === 'updateCustomer') {
      this.getCustomerDetail(this.id);
    }
    this.buildForm();
    this.onResize();
  }


  onchangePartner(event) {
  }

  private buildForm() {
    this.status = 1;
    this.isCooperated = 0;
    this.getListHistory();
    this.form = this.formBuilder.group({
      customerId: '',
      code: ['', Validators.compose([Validators.required, Validators.maxLength(20), Validators.pattern(/^[a-zA-Z0-9-_.]+$/)])],
      name: ['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      status: this.status,
      address: ['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      phone: ['', Validators.compose([Validators.maxLength(11), Validators.pattern("(0)[0-9 ]{9}")])],
      isCooperated: this.isCooperated,
      note: ['', Validators.compose([Validators.maxLength(255)])],
    });

  }

  getCustomerDetail(id) {
    this.customerApiService.getCustomer(id).subscribe(
      res => {
        this.CustomerDetail = res.data;
        this.status = this.CustomerDetail.status;
        this.isCooperated = this.CustomerDetail.isCooperated;
        console.warn(82, res.data);
        if (this.CustomerDetail !== undefined) {
          this.setDataDefault();
        }
      }, error => {
        this.CustomerDetail = null;
      }
    )
  }

  onChangePosition(event) {
    this.status = ((event) ? event.id : 1);
    console.warn(88, this.status);
  }

  onBlurUserCode() {
    if (this.type === 'addCustomer' && this.form.value.code !== '') {
      this.customerApiService.checkUserCode(this.form.value.code).subscribe(res => {
        this.isDuplicateUserCode = false;
      }, err => {
        this.isDuplicateUserCode = true;
      });
    }
  }

  convertJson(value) {
    if (value) {
      return JSON.parse(value);
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

  setDataDefault() {
    this.form.patchValue(this.CustomerDetail);
  }

  loadStatus(data, s) {
    if (s === 1) {
      if (data === 1) {
        return "Đang hợp tác";
      }
      return "Không hợp tác"
    }
    if (s === 2) {
      if (data === 1) {
        return "Có";
      }
      return "Không"
    }
  }

  checkHistory(data) {
    if (data.action === 4) {
      if ((this.convertJson(data.valueNew)).customerId === this.CustomerDetail.customerId) return true;
      return false;
    }
    return false;
  }


  loadCheckBox(data) {
    if (this.type === 'updateCustomer' && data.isCooperated === 1) {
      return true;
    }
    return false;
  }

  // private pushDataToForm(res: any, action: String) {
  //   this.form.get('customerId').setValue(res.customerId); //  id đối tác
  //   this.form.get('code').setValue(res.code); //  mã đối tác
  //   this.form.get('name').setValue(res.name);
  //   this.form.get('status').setValue(res.status);
  //   this.form.get('address').setValue(res.address);
  //   this.form.get('phone').setValue(res.phone);
  //   this.form.get('isCooperated').setValue(res.isCooperated);
  //   this.form.get('note').setValue(res.note);
  //   const dataDefault = {
  //     customerId: res.customerId, //  id đối tác
  //     code: res.code, //  mã đối tác
  //     name: res.name //  tên đối tác
  //   };
  //   if (action === 'view') {
  //     this.form.get('customerId').disable(); //  mã dự án
  //   } else {
  //     //  cho phép sửa nếu là update
  //     this.form.get('customerId').enable(); //  mã dự án
  //   }
  // }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  check(e) {
    if (e.target.checked === true) {
      this.isCooperated = 1;
    } else this.isCooperated = 0;
  }

  get formControl() {
    return this.form.controls;
  }


  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  getListHistory() {
    this.customerApiService.getListHistory().subscribe(success => {
      console.warn(success);
      this.listHistory = success;
      console.warn(681, this.listHistory);
    });

  }

  checkDirty() {
    if (this.type === 'updateCustomer') {
      if (this.form.value.code === this.CustomerDetail.code &&
        this.form.value.name === this.CustomerDetail.name &&
        this.form.value.address === this.CustomerDetail.address &&
        this.form.value.note === this.CustomerDetail.note &&
        this.form.value.phone === this.CustomerDetail.phone &&
        this.form.value.status === this.CustomerDetail.status &&
        this.form.value.isCooperated === this.CustomerDetail.isCooperated)
        this.formChange = false;
      else this.formChange = true;
    }
    return true;


  }

  onCancel() {
    console.warn(100, this.type)
    this.checkDirty()
    if (this.type === 'updateCustomer') {
      if (this.formChange === false
      ) {
        this.router.navigate(['system-categories/partner-management']);
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.router.navigate(['system-categories/partner-management']);
          }
          this.isModalConfirmShow = false;
        });
      }
    }
    if (this.type === 'addCustomer') {
      if (
        this.form.value.name === '' &&
        this.form.value.address === '' &&
        this.form.value.note === '' &&
        this.form.value.phone === '' &&
        this.form.value.status === 1 &&
        this.form.value.isCooperated === false
      ) {
        this.router.navigate(['system-categories/partner-management']);
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.router.navigate(['system-categories/partner-management']);
          }
          this.isModalConfirmShow = false;
        });
      }
    }

  }

  onSubmit() {
    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      console.warn("invalid");
      return;
    }
    this.spinner.show();

    this.customerApiService.save(
      {
        customerId: this.id,
        code: this.form.get('code').value ? this.form.get('code').value : '',
        name: this.form.get('name').value ? this.form.get('name').value : '',
        address: this.form.get('address').value ? this.form.get('address').value : '',
        isCooperated: this.isCooperated,
        status: this.status,
        phone: this.form.get('phone').value ? this.form.get('phone').value : '',
        note: this.form.get('note').value ? this.form.get('note').value : '',
      }
    ).subscribe(
      res => {
        this.toastService.openSuccessToast(this.type === 'addCustomer' ? 'Thêm mới thành công' : 'Sửa thành công');
        this.router.navigate(['system-categories/partner-management']);
      },
      error => {
        this.toastService.openErrorToast(this.type === 'addCustomer' ? 'Thêm mới không thành công' : 'Sửa không thành công');
        this.spinner.hide();
      },
      () => {
        this.spinner.hide();
      }
    );

  }

  ngAfterViewInit(): void {
    (this.type === 'addCustomer') ? this.ngSelectCode.nativeElement.focus() : this.ngSelectName.nativeElement.focus();
  }


  viewChangeStr(history) {
    const infNew = JSON.parse(history.valueNew);
    const infOld = JSON.parse(history.valueOld);
    // console.warn(70, valueNew)
    // console.warn(71, valueOld)
    // const infNew = JSON.parse(valueNew);
    // const infOld = JSON.parse(valueOld);
    let result = "";
    if (infNew.name !== infOld.name) {
      result += 'Tên khách hàng từ: [' + infOld.name + '] thành [' + infNew.name + ']';
    }
    if (infNew.status !== infOld.status) {
      result += ((result ? '; ' : '') + 'Trạng thái hợp tác từ: ' + (infOld.status === 1 ? '[Đang hợp tác]' : '[Không hợp tác]') + ' thành ' + (infOld.status === 0 ? '[Đang hợp tác]' : '[Không hợp tác]'));
    }
    if (infNew.address !== infOld.address) {
      result += ((result ? '; ' : '') + 'Địa chỉ từ: [' + infOld.address + '] thành [' + infNew.address + ']');
    }
    if (infNew.phone !== infOld.phone) {
      result += ((result ? '; ' : '') + 'Số điện thoại từ: [' + infOld.phone + '] thành [' + infNew.phone + ']');
    }
    if (infNew.isCooperated !== infOld.isCooperated) {
      result += ((result ? '; ' : '') + 'Khách hàng đã hợp tác với IIST chưa? từ: ' + (infOld.isCooperated === 1 ? '[Có]' : '[Không]') + ' thành ' + (infOld.isCooperated === 0 ? '[Có]' : '[Không]'));
    }
    if (infNew.note !== infOld.note) {
      result += ((result ? '; ' : '') + 'Ghi chú: [' + infOld.note + '] thành [' + infNew.note + ']');
    }
    return result;
    console.warn(151, result)
  }

}
