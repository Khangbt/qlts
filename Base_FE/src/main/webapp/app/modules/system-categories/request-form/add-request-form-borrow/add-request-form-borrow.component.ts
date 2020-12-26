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
import {DeviceServiceService} from "app/core/services/device-api/device-service.service";
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";

@Component({
  selector: 'jhi-add-request-form-borrow',
  templateUrl: './add-request-form-borrow.component.html',
  styleUrls: ['./add-request-form-borrow.component.scss']
})
export class AddRequestFormBorrowComponent implements OnInit {
  @Input() type;
  @Input() id: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter()
  @Input() partId: any;
  @Input() status: any;
  form: FormGroup;
  isDuplicateUserCode = false;
  partList: [];
  statusList: [];
  checkNul = [];
  listDevice = [];
  listDeviceDefule = [];
  height: number;
  listGroupDiveice = [];
  listIdDevice = [];
  listDataDevice = [];
  dataIdListConvet = [];
  title ="";
  data = {
    persons: []
  };
  check = false;
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
  oldId: any;
  dataFace = [
    {id: 1, name: "ádadad", disabled: false, size: "đấ", unit: "sadad"},
    {id: 2, name: "ádasssdad", disabled: false, size: "đấ1", unit: "sad1ad"},
    {id: 3, name: "ádassssssdad", disabled: false, size: "đ2ấ", unit: "saddad"},
    {id: 4, name: "ádasssqqwdad", disabled: false, size: "đấ3", unit: "saêdad"},
    {id: 5, name: "ưqeeq", disabled: false, size: "đ4ấ", unit: "sadaeeed"},
    {id: 6, name: "ádassssdasssdad", disabled: false, size: "đ5ấ", unit: "sadad"},]

  constructor(public activeModal: NgbActiveModal,
              protected router: Router,
              private formBuilder: FormBuilder,
              private humanResourceService: HumanResourcesApiService,
              private toastService: ToastService,
              private spinner: NgxSpinnerService,
              private heightService: HeightService,
              private listDeviceService: ListDeviceService,
              private requestFomApiService: RequestFomApiService,
              private deviceServiceService: DeviceServiceService,
              private formStoringService: FormStoringService,
  ) {
    $(".ng-select.ng-select-multiple .ng-select-container.ng-value-container.ng-value").css('color', '#000000')
  }

  ngOnInit(): void {
    this.getPartList();
    this.getListDevice(this.partId);
    this.buildForm();

  }

  private buildForm() {
    if (this.type === 'add') {
      this.title = "Tạp phiếu mượn";
    }else  if(this.type ==="update"){
      this.title = "Sửa phiếu yêu cầu ";
    }else
      this.title = "Xem chi tiết phiếu yêu cầu";
    this.form = this.formBuilder.group({
      id: [this.id],
      code: [],
      creatHummerId: [],
      status: [1],
      startDateBorrow: [],
      endDateBorrow: [],
      note: [],
      partId: [null],
      creatDate: [],
      nameHummer: [],
      handlerHummerId: [],
      nameHanlerHummer: [],
      approvedDate: [],
      listDeviceR: this.formBuilder.array([]),
    });

    if (this.id) {
      this.getDetail(this.id);

    } else {
      this.form.get("creatDate").setValue(new Date())
      this.xetDateLocacl();
    }

  }

  getDetail(id) {
    //get apy
    this.requestFomApiService.getByIdDeviceRequest(id).subscribe(
      value => {
        this.data.persons = value.listDeviceR
        this.setCountries();
        this.getDateDefile(value);
        this.xetDataaDevice()
        console.warn(this.data.persons)
      },
      error => {

      }
    )
  }

  onCancel() {
    this.activeModal.dismiss();

  }

  getDateDefile(value) {
    console.warn(value)
    this.form.get("creatDate").setValue(new Date(value.creatDate));
    this.form.get("partId").setValue(value.partId);
    this.form.get("endDateBorrow").setValue(new Date(value.creatDate));
    this.form.get("note").setValue(value.note);
    this.form.get("creatHummerId").setValue(value.creatHummerId);
    this.form.get("handlerHummerId").setValue(value.handlerHummerId);
    this.form.get("status").setValue(value.status);
    this.form.get("code").setValue(value.code);
    this.form.get("nameHummer").setValue(value.nameCreat);
    this.form.get("approvedDate").setValue(new Date(value.approvedDate));
    this.form.get("nameHanlerHummer").setValue(value.nameHandler);

    console.warn(this.form.get("creatDate").value)
    this.xetDataBorrow()
  }

  onSubmitData() {
    console.warn(this.form.value)
    if (this.type === "add") {
      this.requestFomApiService.saveDeviceRequest(this.form.value).subscribe(
        value => {
          console.warn("ádad")
          this.router.navigate(['system-categories/request-form']);
          this.activeModal.dismiss();
        }
      )
    }
    if (this.type === "update") {
      this.requestFomApiService.updateDeviceRequest(this.form.value, this.id).subscribe(
        value => {
          console.warn("ádad")
          this.router.navigate(['system-categories/request-form']);
          this.activeModal.dismiss();
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
      this.onResize();
    }

    this.checkNul.push(value);
    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    (<FormArray>this.form.get("listDeviceR")).push(
      this.formBuilder.group({
        idGroup: [],
        size: [],
        unit: [],
        listCode: [],
        quantity: [0],
        id: []
      })
    );
    this.listDevice = this.form.get("listDeviceR").value;
  }

  setCountries() {
    this.onResize();
    for (const c of this.data.persons) {
      this.addPerson(true);
    }

    for (const p of this.listDeviceDefule) {
      for (const c of this.data.persons) {
        if (p.id === c.idGroup) {
          p.disabled = true
          c.size = p.sizeWarhous;
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


    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    (<FormArray>this.form.get("listDeviceR")).removeAt(i);
    this.listDevice = this.form.get("listDeviceR").value;
    this.xetTrueFal(this.form.get("listDeviceR").value);
  }

  saveDevice(i) {
    this.checkNul[i] = !this.checkNul[i];
    this.xetTrueFal(this.form.get("listDeviceR").value);
  }

  getListDevice(id) {
    if (id !== null) {
      this.listDeviceService.getListDeviceGroup(id).subscribe(
        value => {
          this.listDeviceDefule = value;
          console.warn(this.listDeviceDefule)
          this.oldId = id
        },
        error => {
          this.listDeviceDefule = []
        }
      )
    }
  }

  xetDataDefile(event, i) {
    if (event !== undefined) {
      this.listDevice[i].unit = event.unit ? this.xetUntit(event.unit) : null;
      this.listDevice[i].idGroup = event.id
      this.listDevice[i].size = event.sizeWarhous ? event.sizeWarhous : null
      this.listDevice[i].unit = event.unit ? event.unit : null

      this.form.controls["listDeviceR"].setValue(this.listDevice)
      this.xetTrueFal(this.form.controls["listDeviceR"].value)
    } else {
      this.listDevice[i].unit = null
      this.listDevice[i].idGroup = null
      this.listDevice[i].size = null

      this.form.controls["listDeviceR"].setValue(this.listDevice)
    }
  }

  xetTrueFal(data1) {
    console.warn(data1)
    if (data1.length === 0) {
      for (const p of this.listDeviceDefule) {
        p.disabled = false
      }
    } else {
      for (const p of this.listDeviceDefule) {
        p.disabled = false
      }
      for (const c of data1) {
        for (const p of this.listDeviceDefule) {
          if (p.id === c.idGroup) {
            p.disabled = true
          }
        }
      }
      console.warn(this.listDeviceDefule)
    }
  }

  xetDataPart() {
    const c = this.form.get("partId").value;
    if (c !== null && c !== undefined) {
      this.getListDevice(c);
    }
    if (c !== this.oldId) {
      // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
      while ((<FormArray>this.form.get("listDeviceR")).value.length) {
        // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
        (<FormArray>this.form.get("listDeviceR")).removeAt(0);
      }
      this.checkNul = [];

      this.listDevice = [];
    }

  }

  xetUntit(unit) {
    const c = this.unitList.filter(function (value) {
      return value.id === unit
    });
    console.warn(c);
    console.warn(unit)
    return c[0].name;
  }

  xetDateLocacl() {
    const x = JSON.parse(localStorage.getItem('user'));

    this.form.get("creatHummerId").setValue(x.humanResourceId)
    this.form.get("nameHummer").setValue(x.fullName)
  }

  xetDataaDevice() {
    console.warn(this.form.get("status").value)
    if (this.form.get("status").value === 1) {
      const c = this.form.get("partId").value
      if (c !== null) {
        this.deviceServiceService.getDataParTId(this.id, c).subscribe(
          value => {
            this.listDataDevice = value
            this.xetDataaDevite();

          }, error => {
            this.listDataDevice = []
          }
        )

      }
    } else {
      this.deviceServiceService.getDataParRetu(this.id).subscribe(
        value => {
          this.listDataDevice = value
          this.xetDataaDevite();

        }, error => {
          this.listDataDevice = []
        }
      )


    }

  }

  xetDataaDevite() {
    console.warn(this.listDataDevice)
    for (const p of this.listDevice) {
      for (const c1 of this.listDataDevice) {
        if (c1.id === p.idGroup) {
          this.dataIdListConvet.push(c1.listXet)
        }
      }
    }
    console.warn(this.dataIdListConvet)
  }

  onGood() {
    this.requestFomApiService.goodRequest(this.form.value, this.id).subscribe(
      value => {


        this.router.navigate(['system-categories/request-form']);
        this.activeModal.dismiss();
      },
      error => {

      }
    )
  }

  onCancelModal(content) {
    this.requestFomApiService.cannRequest(this.form.value, this.id).subscribe(
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
