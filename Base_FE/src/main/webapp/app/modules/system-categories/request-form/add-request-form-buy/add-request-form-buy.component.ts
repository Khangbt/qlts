import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {ModalDismissReasons, NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {ToastService} from "app/shared/services/toast.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HeightService} from "app/shared/services/height.service";
import {ListDeviceService} from "app/core/services/list-device/list-device.service";
import {RequestFomApiService} from "app/core/services/request-form-api/request-fom-api.service";
import {SupplierServieceService} from "app/core/services/Supplier-service/supplier-serviece.service";
import {WarehouseServicesService} from "app/core/services/warehouse/warehouse-services.service";
import {STORAGE_KEYS} from "app/shared/constants/storage-keys.constants";
import {FormStoringService} from "app/shared/services/form-storing.service";

@Component({
  selector: 'jhi-add-request-form-buy',
  templateUrl: './add-request-form-buy.component.html',
  styleUrls: ['./add-request-form-buy.component.scss']
})
export class AddRequestFormBuyComponent implements OnInit {
  @Input() type;
  @Input() id: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter()
  @Input() status:any;
  closeResult: string;
  form: FormGroup;
  isDuplicateUserCode = false;
  partList: [];
  statusList: [];
  checkNul = [];
  listDevice = [];
  height: number;
  listGroupDiveice = [];
  listSupplier = [];
  listGroup = [];
  wareHouseList=[];
  idOle:any;
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
              private supplierServieceService: SupplierServieceService,
              private modalService: NgbModal,
              private warehouseServicesService: WarehouseServicesService,
              private formStoringService:FormStoringService,


  ) {
  }

  ngOnInit(): void {
    this.getPartList();
    this.getSupperlerList();
    this.getDevide()
    this.buildForm();

  }

  private buildForm() {
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
    this.requestFomApiService.getByIdDeviceRequestAdd(id).subscribe(
      value => {
        console.warn(value)
        this.data.persons = value.listDeviceR
        this.getDateDefile(value)
        this.setCountries();
        this.xetDataHoust(false)
        this.xetDataBorrow()

      },
      error => {

      }
    )
  }

  getDateDefile(value) {
    this.form.get("creatDate").setValue(new Date(value.creatDate));
    this.form.get("partId").setValue(value.partId);
    this.form.get("creatHummerId").setValue(value.creatHummerId);
    this.form.get("note").setValue(value.note);
    this.form.get("handlerHummerId").setValue(value.handlerHummerId);
    this.form.get("status").setValue(value.status);
    this.form.get("code").setValue(value.code);
    this.form.get("nameHummer").setValue(value.nameCreat);
    this.form.get("nameHanlerHummer").setValue(value.nameHandler);


  }

  onCancel() {
    this.activeModal.dismiss();

  }

  onSubmitData() {
    console.warn(this.form.value)
    if (this.type === "add") {
      this.requestFomApiService.saveDeviceRequestAdd(this.form.value).subscribe(
        value => {
          console.warn("ok")
          this.router.navigate(['system-categories/request-form']);
          this.activeModal.dismiss();
        }, error => {

        }
      )
    }
    if (this.type === "update") {
      this.requestFomApiService.updateDeviceRequestAdd(this.form.value, this.id).subscribe(
        value => {
          console.warn("ok")
          this.router.navigate(['system-categories/request-form']);
          this.activeModal.dismiss();
        }, error => {

        }
      )
    }
    console.warn(this.form.value)


  }

  onBlurUserCode() {
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
    console.warn("11111111111111")
    this.checkNul.push(value);
    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    (<FormArray>this.form.get("listDeviceR")).push(
      this.formBuilder.group({
        id: [],
        size: [],
        unit: [],
        supplierId: [],
        price: [],
        idGroup: [],
        sizeUnit: [],
        warehouseId:[],
      })
    );
    this.listDevice = this.form.get("listDeviceR").value;
  }

  setCountries() {
    console.warn(this.data.persons)
    this.onResize();
    for (const c of this.data.persons) {
      this.addPerson(true);
    }
    for (const p of this.listGroup) {
      for (const c of this.data.persons) {
        if (p.id === c.idGroup) {
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
    this.listDeviceService.getListDeviceGroup(id).subscribe(
      value => {
        this.listDevice = value;
      },
      error => {
        this.listDevice = []
      }
    )
  }

  xetDataDefile(event, i) {
    if (event !== undefined) {
      this.listDevice[i].unit = event.unit ? event.unit : null;
      this.listDevice[i].idGroup = event.id
      this.listDevice[i].size = event.size ? event.size : null
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
      for (const p of this.listGroup) {
        p.disabled = false
      }
    } else {
      for (const p of this.listGroup) {
        p.disabled = false
      }
      for (const c of data1) {
        for (const p of this.listGroup) {
          if (p.id === c.idGroup) {
            p.disabled = true
          }
        }
      }
      console.warn(this.listGroup)
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

  getDevide() {
    this.listDeviceService.getListDeviceGroupNotId().subscribe(
      value => {
        this.listGroup = value;
        for (const p of this.listGroup) {
          p.disabled = false;
        }
        console.warn(this.listGroup)

      },
      error => {
        this.listGroup = []
      }
    )
    console.warn(this.listGroup)

  }

  xetDateLocacl() {
    const x = JSON.parse(localStorage.getItem('user'));

    this.form.get("creatHummerId").setValue(x.humanResourceId)
    this.form.get("nameHummer").setValue(x.fullName)
    this.form.get("partId").setValue(x.partId)

  }

  onGood() {
    this.requestFomApiService.goodRequestAdd(this.form.value,this.id).subscribe(
      value => {


        this.router.navigate(['system-categories/request-form']);
        this.activeModal.dismiss();
      },
      error => {

      }
    )
  }

  onCancelModal(content) {
    // this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
    //   this.closeResult = `Closed with: ${result}`;
    // }, (reason) => {
    //   this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    // });
      this.requestFomApiService.cannRequestAdd(this.form.value,this.id).subscribe(
        value => {


          this.router.navigate(['system-categories/request-form']);
          this.activeModal.dismiss();
        },
        error => {

        }
      )
  }
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return  `with: ${reason}`;
    }
  }

  xetDataBorrow() {
    if (this.type === 'browser') {
      const userToken: any = this.formStoringService.get(STORAGE_KEYS.USER);
      this.form.get("handlerHummerId").setValue(userToken.humanResourceId)
      console.warn(userToken)
    }

  }
  xetDataHoust(value1){
    const i = this.form.get("partId").value ? this.form.get("partId").value : -1
    console.warn(i+"-----"+this.idOle)
    if (this.idOle !== i) {
      if(value1){
        this.checkNul = [];
        //eslint-disable-next-line @typescript-eslint/consistent-type-assertions
        while ((<FormArray>this.form.get("listDeviceR")).value.length) {
          // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
          (<FormArray>this.form.get("listDeviceR")).removeAt(0);
        }

        this.listDevice = [];
      }
      this.warehouseServicesService.getByPart(i).subscribe(
        value => {

          this.wareHouseList = value
          this.idOle=i
        },
        error => {
          this.wareHouseList = []
        }
      )
    }
  }
}
