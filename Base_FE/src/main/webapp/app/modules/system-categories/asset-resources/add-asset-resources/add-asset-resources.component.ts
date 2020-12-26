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
import {toNumber} from "ng-zorro-antd";
import {debounceTime} from "rxjs/operators";
import {TIME_OUT} from "app/shared/constants/set-timeout.constants";
import {ITEMS_PER_PAGE} from "app/shared/constants/pagination.constants";
import {HttpResponse} from "@angular/common/http";
import {STATUS_CODE} from "app/shared/constants/status-code.constants";
import {REGEX_PATTERN} from "app/shared/constants/pattern.constants";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {DeviceServiceService} from "app/core/services/device-api/device-service.service";
import {WarehouseServicesService} from "app/core/services/warehouse/warehouse-services.service";
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";
import {ImageApiService} from "app/core/services/image-api/image-api.service";
import { ConfimModalImgComponent } from 'app/shared/components/confim-modal-img/confim-modal-img.component';

@Component({
  selector: 'jhi-add-asset-resources',
  templateUrl: './add-asset-resources.component.html',
  styleUrls: ['./add-asset-resources.component.scss']
})
export class AddAssetResourcesComponent implements OnInit {

  oldEmail: any;
  @Input() type;
  @Input() id: any;
  @Input() code: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter()
  @Input() listGroup;
  @Input() listSupplier;
  @Input() data;
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
  isDuplicateCode = false;
  height: number;
  name: string;
  maxlength = 4;
  isError = false;
  dateRecruitmentValid = false;
  dateGraduateValid = false;
  dateMajorValid = false;
  idDev: number;
  isYear = false;
  yy: number;
  years: number[] = [];
  wareHouseList = [];
  listhummer = [];
  checkBoll = false;
  xetCheck = false;
  idOle: any;
  listImgGroup=[];
  listDeviceGroup = []
  listStatus = [{id: 1, name: "Đang Trong kho"}, {id: 2, name: "Đang sử dụng"}]
  deviceDetail: any;
  unitList = [{
    id: 1,
    name: "Lô"
  }, {
    id: 2,
    name: "Cái"
  },
    {
      id: 3,
      name: "Chiếc"
    }];
  dataStringPath: any;
  listDataImg = []
  dataIm: any = [];
  listurl = [];
  url;
  msg = "";
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
    private deviceServiceService: DeviceServiceService,
    private warehouseServicesService: WarehouseServicesService,
    private formStoringService: FormStoringService,
    private imageApiService: ImageApiService,
  ) {
    this.height = this.heightService.onResize()
  }

  get formControl() {
    return this.form.controls;
  }

  ngOnInit() {
    this.onResize();
    this.getDefaultData();
    this.getPartList();
    this.buildForm();
    this.getListHummer();

  }

  getListHummer() {
    this.humanResourceService.getCodeOrName().subscribe(value => {
        this.listhummer = value;
      },
      error => {
        this.listhummer = []
      })
  }

  private buildForm() {
    if (this.type === 'add') {
      this.title = "Thêm  tài sản";
    }else if(this.type ==="update"){
      this.title = "Sửa tài sản";
    }else
      this.title = "Xem chi tiết tài sản";

    this.form = this.formBuilder.group({
      specifications: [''],
      supplierId: [null],
      partId: [null],
      code: [''],
      name: [''],
      note: [''],
      status: [1],
      localion: [''],
      hummer: [''],
      tyle: [],
      useHummerId: [],
      idEquipmentGroup: [],
      warehouseId: [null],
      lostDevice: [100, [Validators.required,
        Validators.pattern('^\\d*$')]],
      unit: [],
      sizeUnit: [],
      size: [0],
      check: [true],
      id: [this.id]
    });
    if (this.id) {
      this.getDataDetail(this.id);
    } else {
      this.xetDataUer()
    }


  }

  onBlurUserCode() {
    this.deviceServiceService.getFindByCode(this.form.get("code").value).subscribe(
      value => {
        this.isDuplicateCode = true;
      },
      error => {
        this.isDuplicateCode = false;

      }
    )
  }

  getDefaultData() {
    if (this.type && this.id) {
      this.commonService.clearDataTranfer('type');
      this.commonService.clearDataTranfer('id');
    } else {
      if (this.type !== 'add') {
        this.router.navigate(['system-categories/asset-resources']);
      }

    }
  }

  setDataDefault() {
    this.form.patchValue(this.deviceDetail);

  }

  getDataDetail(id) {
    // console.warn("11111" + id)
    // this.deviceServiceService.getFindByCode(this.code).subscribe(
    //   value => {
    //     this.deviceDetail = value;
    //     console.warn(this.deviceDetail)
    //     this.setDataDefault();
    //   },
    //   error => {
    //
    //   }

    // )
    console.warn(this.data)
    this.idOle = this.data.partId;
    this.form.get("code").setValue(this.data.code)
    this.form.get("status").setValue(this.data.status)
    this.form.get("localion").setValue(this.data.localion)
    this.form.get("supplierId").setValue(this.data.supplierId)
    this.form.get("lostDevice").setValue(this.data.lostDevice)
    this.form.get("warehouseId").setValue(this.data.warehouseId)
    this.form.get("useHummerId").setValue(this.data.useHummerId)
    this.form.get("partId").setValue(this.data.partId)
    this.form.get("unit").setValue(this.data.unit)
    this.form.get("sizeUnit").setValue(this.data.sizeUnit)
    this.form.get("idEquipmentGroup").setValue(this.data.idEquipmentGroup)
    this.form.get("specifications").setValue(this.data.specifications)
    this.form.get("note").setValue(this.data.note)

    this.getdataDetial(this.data.partId);
    this.xetDataUer()
    this.imageApiService.getListDevice(this.id,this.data.idEquipmentGroup).subscribe(
      value => {
        this.dataStringPath=value;
        if(this.dataStringPath!==[]){
          for (const p of this.dataStringPath) {
            this.imageApiService.getFile(p.url).subscribe(
              value1 => {
                const data = value1;
                this.getFile(data,p.group)
              }
            )
          }
        }
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

  onCancel() {
    this.activeModal.dismiss();
    if (this.type === 'update') {
      if (this.xetdataa()) {
        this.activeModal.dismiss();
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.activeModal.dismiss();
          }
        });
      }
    }
    if (this.type === 'detail') {
      this.router.navigate(['system-categories/asset-resources']);
    }
  }

  xetdataa() {
    return true
  }

  onSubmitData() {

    if(this.form.get("check").value){
      if(this.isDuplicateCode){
        return;
      }
    }
    console.warn(this.form.value)
    if (this.type === "add") {
      if (this.form.get("status").value !== 2) {
        this.form.get("useHummerId").setValue(null);
        this.form.get("localion").setValue(null);
      }
      this.deviceServiceService.creart(this.form.value).subscribe(value => {
        if(this.form.controls['check'].value){
            const tyleImg=1;
          const formData = new FormData();
          for (const c of this.listDataImg) {
            formData.append("file", c);
          }
          this.imageApiService.save(value.id, tyleImg, formData).subscribe(
            res1 => {
              console.warn(res1)
            },
            error => {

            }
          )

        }
        this.toastService.openSuccessToast('Thêm mới thành công !');
        this.router.navigate(['system-categories/asset-resources']);
        this.activeModal.dismiss();
      }, error => {

        this.toastService.openErrorToast(this.type === 'add' ? 'Thêm mới không thành công' : 'Sửa thất bại');
        this.spinner.hide();
      })
    }
    if (this.type === "update") {
      if (this.form.get("status").value !== 2) {
        this.form.get("useHummerId").setValue(null);
        this.form.get("localion").setValue(null);
      }



      this.deviceServiceService.update(this.form.value, this.id).subscribe(
        value => {
          this.toastService.openSuccessToast('Chỉnh sửa thành công !');
          this.router.navigate(['system-categories/asset-resources']);
          this.activeModal.dismiss();
        }, error => {
          this.toastService.openErrorToast(this.type === 'add' ? 'Thêm mới không thành công' : 'Sửa thất bại');
          this.spinner.hide();
        }
      )

      const tyle = 1;
      const formData = new FormData();
      for (const c of this.listDataImg) {
        formData.append("file", c);
      }
      formData.append("data",this.dataIm);
      this.imageApiService.update(this.id,tyle,formData).subscribe(
        value => {
          console.warn(value)
        },
        error => {

        }
      )
    }

  }


  onResize() {
    this.height = this.heightService.onResize();
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

  getPartList() {
    this.humanResourceService.getPartList().subscribe(
      res => {
        if (res) {
          this.partList = res.data;
        } else {
          this.partList = [];
        }
      },
      err => {
        this.partList = [];
      }
    );
  }

  checkVitri() {

    if (this.form.get("status").value === 1) {
      this.xetCheck = true;
    } else {
      this.xetCheck = false
    }
    console.warn(this.form.get("status").value)
    console.warn(this.xetCheck)
  }

  xetDataWare() {
    const i = this.form.get("partId").value ? this.form.get("partId").value : -1
    if (this.idOle !== i) {
      this.form.get("warehouseId").setValue(null);
      this.warehouseServicesService.getByPart(i).subscribe(
        value => {
          this.wareHouseList = value
        },
        error => {
          this.wareHouseList = []
        }
      )
    }
  }

  getdataDetial(i) {
    this.warehouseServicesService.getByPart(i).subscribe(
      value => {
        this.wareHouseList = value
      },
      error => {
        this.wareHouseList = []
      }
    )
  }

  xetUnit() {
    console.warn(this.form.get("idEquipmentGroup").value)
    const c = this.form.get("idEquipmentGroup").value;
    let data;
    if (this.form.get("idEquipmentGroup").value !== null) {
      data = this.listGroup.filter(function (value) {
        if (value.id === c) {
          return value;
        }
      })

    }
    if (data !== undefined) {
      this.form.get("unit").setValue(data[0].unit)
      this.form.get("sizeUnit").setValue(data[0].sizeUnit)
      this.form.get("specifications").setValue(data[0].specifications)

      console.warn(data[0].unit + "------" + data[0].sizeUnit)
    } else {
      this.form.get("unit").setValue(null)
      this.form.get("sizeUnit").setValue(1)
      this.form.get("specifications").setValue(null)
    }
  }

  xetDataUer() {
    const userToken: any = this.formStoringService.get(STORAGE_KEYS.USER);
    if (userToken.role === "ROLE_ADMINPART") {
      this.form.get("partId").setValue(userToken.partId)
      this.checkBoll = true
      this.xetUnit()
      this.xetDataWare()
    } else {
      this.checkBoll = false
    }


  }

  checkNull() {
    if (this.checkBoll) {
      return true
    }
    if (this.form.value.partId === null && !this.checkBoll) {
      return true
    }
    return false
  }

  cleanAnh(i) {
    this.listDataImg.splice(i, 1)
    this.listurl.splice(i, 1)
  }

  getFile(file, check) {
    if (!file || file.length === 0) {
      this.msg = 'You must select an image';
      return;
    }

    const mimeType = file.type;
    if (mimeType.match(/image\/*/) == null) {
      this.msg = "Only images are supported";
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = (_event) => {
      this.msg = "";
      this.url = reader.result;
      if(check){
        this.listImgGroup.push(reader.result);
      }else {
        this.listurl.push({chech: true, data: reader.result, url: null})
        this.listDataImg.push(file)

      }

    }
  }

  selectFile(event) {
    for (const p of event.target.files) {
      this.getFile(p, false)
    }

  }

    deleteImg(i){
      const modalRef = this.modalService.open(ConfimModalImgComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'delete';
      modalRef.componentInstance.param = 'ảnh đính kèm';
      modalRef.componentInstance.status = this.type;
      modalRef.componentInstance.role = true;
      modalRef.componentInstance.imgData=this.listurl[i].data
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
            this.cleanAnh(i)
          }
      });
      
    }
    showImg(i){
      const modalRef = this.modalService.open(ConfimModalImgComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'delete';
      modalRef.componentInstance.param = 'ảnh đính kèm';
      modalRef.componentInstance.status = this.type;
      modalRef.componentInstance.role = false;
      modalRef.componentInstance.imgData=this.listImgGroup[i]
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
            this.cleanAnh(i)
          }
      });
    }
}


