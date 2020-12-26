import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {ToastService} from "app/shared/services/toast.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HeightService} from "app/shared/services/height.service";
import {ListDeviceService} from "app/core/services/list-device/list-device.service";
import {RequestFomApiService} from "app/core/services/request-form-api/request-fom-api.service";
import {SupplierServieceService} from "app/core/services/Supplier-service/supplier-serviece.service";
import {WarehouseServicesService} from "app/core/services/warehouse/warehouse-services.service";
import {DeviceServiceService} from "app/core/services/device-api/device-service.service";
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";

@Component({
  selector: 'jhi-add-request-form-return',
  templateUrl: './add-request-form-return.component.html',
  styleUrls: ['./add-request-form-return.component.scss']
})
export class AddRequestFormReturnComponent implements OnInit {
  @Input() type;
  @Input() id: any;
  @Input() partId: any;
  @Input() creatHummerId: any;
  @Input() status: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter()
  check = false;
  form: FormGroup;
  isDuplicateUserCode = false;
  partList: [];
  statusList: [];
  checkNul = [];
  listDevice = [];
  height: number;
  listGroupDiveice = [];
  listSupplier = [];
  // listGroup = [];
  wareHouseList = [];
  idOle: any;
  listDeviceReturn = [];
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
  data = {
    persons: []
  };

  constructor(public activeModal: NgbActiveModal,
              protected router: Router,
              private formBuilder: FormBuilder,
              private humanResourceService: HumanResourcesApiService,
              private toastService: ToastService,
              private spinner: NgxSpinnerService,
              private heightService: HeightService,
              private listDeviceService: ListDeviceService,
              private deviceServiceService: DeviceServiceService,
              private requestFomApiService: RequestFomApiService,
              private supplierServieceService: SupplierServieceService,
              private warehouseServicesService: WarehouseServicesService,
              private formStoringService: FormStoringService,

  ) {
  }

  ngOnInit(): void {
    this.getPartList();
    this.getSupperlerList();
    this.buildForm();
    // this.getDevide()
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      id: [this.id],
      code: [],
      creatHummerId: [],
      status: [1],
      note: [],
      partId: [null],
      creatDate: [],
      nameHummer: [],
      listDeviceR: this.formBuilder.array([]),
      nameHanlerHummer:[],
      handlerHummerId: [],
      approvedDate:[],
    });
    if (this.id) {
      this.getDetail(this.id);
      this.xetDataWare(false);
      this.xetDataDetalil()
      console.warn("aaaaa")
    } else {
      this.form.get("creatDate").setValue(new Date())
      this.xetDateLocacl();

    }

  }

  getDetail(id) {
    //get apy
    this.requestFomApiService.getByIdDeviceRequestRetu(id).subscribe(
      value => {
        this.data.persons = value.listDeviceR
        this.form.get("creatDate").setValue(new Date(value.creatDate));
        this.form.get("partId").setValue(value.partId);
        this.form.get("creatHummerId").setValue(value.creatHummerId);
        this.form.get("note").setValue(value.note);
        if (value.status === 2 || value.status === 3) {
          this.form.get("nameHanlerHummer").setValue(value.nameHandler);
          this.form.get("approvedDate").setValue(new Date(value.approvedDate));
        }
        this.form.get("status").setValue(value.status);
        this.form.get("code").setValue(value.code);
        this.form.get("nameHummer").setValue(value.nameCreat);

        this.setCountries();
        this.xetDataBorrow()

      },
      error => {

      }
    )
  }

  onCancel() {
    this.activeModal.dismiss();

  }

  onSubmitData() {
    if (this.type === "add") {
      this.requestFomApiService.saveDeviceRequestretu(this.form.value).subscribe(
        value => {
          this.router.navigate(['system-categories/request-form']);
          this.activeModal.dismiss();
        }, error => {

        }
      )
    }
    if (this.type === "update") {
      this.requestFomApiService.updateDeviceRequestetu(this.form.value, this.id).subscribe(
        value => {
          console.warn("ok")
          this.router.navigate(['system-categories/request-form']);
          this.activeModal.dismiss();
        }, error => {

        }
      )
    }

  }

  onBlurUserCode() {
    ///check code
    this.isDuplicateUserCode = false;
  }

  get formControl() {
    return this.form.controls;
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

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  getValueOfField(item) {
    return this.form.get(item).value;
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

  addPerson(value) {
    for (const t of this.checkNul) {
      if (!t) {
        return
      }
    }
    console.warn(this.listDevice)
    this.checkNul.push(value);
    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    (<FormArray>this.form.get("listDeviceR")).push(
      this.formBuilder.group({
        deviceId: [],
        unit: [],
        warehouseId: [],
        lostDevice: [],
        dateBorrow: [],
      })
    );
    this.listDevice = this.form.get("listDeviceR").value;
  }

  setCountries() {
    console.warn(this.data.persons)
    console.warn(this.listDeviceReturn)
    this.onResize();
    for (const c of this.data.persons) {
      this.addPerson(true);
    }
    for (const p of this.listDeviceReturn) {
      for (const c of this.data.persons) {
        if (p.id === c.id) {
          p.disabled = true
        }
      }
    }
    this.form.controls["listDeviceR"].setValue(this.data.persons)
    this.listDevice = this.form.get("listDeviceR").value;
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  deleteDevice(i) {
    this.checkNul.splice(i, 1);
    console.warn(i);

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    (<FormArray>this.form.get("listDeviceR")).removeAt(i);
    this.listDevice = this.form.get("listDeviceR").value;
    this.xetTrueFal(this.form.get("listDeviceR").value);
  }

  saveDevice(i) {
    this.checkNul[i] = !this.checkNul[i];
    this.xetTrueFal(this.form.get("listDeviceR").value);
  }

  // getListDevice(id) {
  //   this.listDeviceService.getListDeviceGroup(id).subscribe(
  //     value => {
  //       this.listDevice = value;
  //     },
  //     error => {
  //       this.listDevice = []
  //     }
  //   )
  // }

  xetDataDefile(event, i) {
    if (event !== undefined) {
      this.listDevice[i].unit = event.unit ? event.unit : null;
      this.listDevice[i].deviceId = event.id
      this.listDevice[i].lostDevice = event.lostDevice

      this.form.controls["listDeviceR"].setValue(this.listDevice)
      this.xetTrueFal(this.form.controls["listDeviceR"].value)
    } else {
      this.listDevice[i].unit = null
      this.listDevice[i].id = null

      this.form.controls["listDeviceR"].setValue(this.listDevice)
    }
  }

  xetTrueFal(data1) {
    console.warn(data1)
    if (data1.length === 0) {
      for (const p of this.listDeviceReturn) {
        p.disabled = false
      }
    } else {
      for (const p of this.listDeviceReturn) {
        p.disabled = false
      }
      for (const c of data1) {
        for (const p of this.listDeviceReturn) {
          if (p.id === c.deviceId) {
            p.disabled = true
          }
        }
      }
    }
  }

  getSupperlerList() {
    this.supplierServieceService.getListSuppler().subscribe(
      value => {
        this.listSupplier = value;
      },
      error => {
        this.listSupplier = []
      }
    )
  }

  // getDevide() {
  //   this.listDeviceService.getListDeviceGroupNotId().subscribe(
  //     value => {
  //       this.listGroup = value;
  //       for (const p of this.listGroup) {
  //         p.disabled = false;
  //       }
  //       console.warn(this.listGroup)
  //
  //     },
  //     error => {
  //       this.listGroup = []
  //     }
  //   )
  //   console.warn(this.listGroup)
  //
  // }

  xetDataWare(a) {
    let i;
    if (a) {
      i = this.form.get("partId").value ? this.form.get("partId").value : -1

    } else {
      i = this.partId;
    }
    if (this.idOle !== i) {
      this.checkNul = [];
      //eslint-disable-next-line @typescript-eslint/consistent-type-assertions
      while ((<FormArray>this.form.get("listDeviceR")).value.length) {
        // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
        (<FormArray>this.form.get("listDeviceR")).removeAt(0);
      }

      this.listDevice = [];

      this.warehouseServicesService.getByPart(i).subscribe(
        value => {
          this.wareHouseList = value
          this.idOle = i;
        },
        error => {
          this.wareHouseList = []
        }
      )
    }
  }

  xetDateWarHouse() {

    this.xetDataWare(true);
    this.xetListDeviceReturn(true)
  }

  xetListDeviceReturn(a) {
    let i, z;
    if (a) {
      i = this.form.get("partId").value ? this.form.get("partId").value : -1
      z = this.form.get("creatHummerId").value ? this.form.get("creatHummerId").value : -1
    } else {
      i = this.partId;
      z = this.creatHummerId

    }
    if (this.idOle !== i) {
      this.deviceServiceService.getDeviceRetu(i, z).subscribe(
        value => {
          this.listDeviceReturn = value;
          this.idOle = i;

        },
        error => {

        }
      )
    }
  }

  xetDataDetalil() {
    if (this.status === 2) {

        this.deviceServiceService.getListyReturmByIdStus(this.id).subscribe(value => {
            this.listDeviceReturn = value;
            this.xetTrueFal(this.form.get("listDeviceR").value);

          },
          error => {

          })

    } else {
      if (this.type === "detail" || this.type === "update"||this.type==="browser") {
        this.deviceServiceService.getListyReturmById(this.id, this.partId, this.creatHummerId).subscribe(
          value => {
            this.listDeviceReturn = value;
            this.xetTrueFal(this.form.get("listDeviceR").value);

          },
          error => {

          }
        )
      }
    }
  }

  xetDateLocacl() {
    const x = JSON.parse(localStorage.getItem('user'));

    this.form.get("creatHummerId").setValue(x.humanResourceId)
    this.form.get("nameHummer").setValue(x.fullName)

  }
  onGood() {
    this.requestFomApiService.goodRequestRetu(this.form.value,this.id).subscribe(
      value => {
        this.router.navigate(['system-categories/request-form']);
        this.activeModal.dismiss();
      },
      error => {

      }
    )
  }

  onCancelModal(content) {

    this.requestFomApiService.cannRequestRetu(this.form.value,this.id).subscribe(
      value => {
        this.router.navigate(['system-categories/request-form']);
        this.activeModal.dismiss();
      },
      error => {

      }
    )
  }

  xetDataBorrow() {
    if (this.type === 'browser') {
      const userToken: any = this.formStoringService.get(STORAGE_KEYS.USER);
      this.form.get("handlerHummerId").setValue(userToken.humanResourceId)
      console.warn(userToken)
    }

  }
}
