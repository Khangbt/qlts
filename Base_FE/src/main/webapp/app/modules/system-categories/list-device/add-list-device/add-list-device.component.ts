import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {ListDeviceService} from "app/core/services/list-device/list-device.service";
import {ToastService} from "app/shared/services/toast.service";
import {NgxSpinnerService} from "ngx-spinner";
import {SupplierServieceService} from "app/core/services/Supplier-service/supplier-serviece.service";
import {Observable} from "rxjs";
import {HeightService} from "app/shared/services/height.service";
import {WarehouseServicesService} from "app/core/services/warehouse/warehouse-services.service";
import {ImageApiService} from "app/core/services/image-api/image-api.service";
import { ConfirmModalComponent } from 'app/shared/components/confirm-modal/confirm-modal.component';
import { ConfimModalImgComponent } from 'app/shared/components/confim-modal-img/confim-modal-img.component';

@Component({
  selector: 'jhi-add-list-device',
  templateUrl: './add-list-device.component.html',
  styleUrls: ['./add-list-device.component.scss']
})
export class AddListDeviceComponent implements OnInit {
  @Input() type;
  @Input() id: any;
  @Input() data: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter()
  height: any;
  formList: FormGroup;
  isDuplicateUserCode = false;
  partList: [];
  statusList: [];
  supplierList: [];
  warehouseList: [];
  name: String;
  dataStringPath: any;
  listDataImg = []
  dataIm: any = [];
  listurl = [];
  url;
  msg = "";
  title = "";
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
  selectedFiles: FileList;
  progressInfos = [];
  message = '';
  idOle: any;
  fileInfos: Observable<any>;

  constructor(public activeModal: NgbActiveModal,
               private modalService: NgbModal,

              protected router: Router,
              private formBuilder: FormBuilder,
              private humanResourceService: HumanResourcesApiService,
              private listDeviceService: ListDeviceService,
              private toastService: ToastService,
              private spinner: NgxSpinnerService,
              private supplierServieceService: SupplierServieceService,
              private heightService: HeightService,
              private warehouseServicesService: WarehouseServicesService,
              private imageApiService: ImageApiService,
  ) {
  }

  ngOnInit(): void {
    this.onResize();
    this.buildForm();
    this.getPartList();
    this.getSupperPartList();
    this.getWarehousePartList();
    this.getFileImg()
    console.warn(this.partList)
  }

  private buildForm() {

    if (this.type === 'add') {
      this.title = "Thêm danh mục tài sản";
    } else if (this.type === "update") {
      this.title = "Sửa danh mục tài sản";
    } else
      this.title = "Xem chi tiết danh mục tài sản";

    this.formList = this.formBuilder.group({
      id: [this.id],
      code: [''],
      name: [''],
      tyle: [''],
      size: [0],
      sizeId: [0],
      specifications: [''],
      note: [''],
      supplierId: [''],
      partId: [null],
      location: [''],
      sizeUnit: [1],
      unit: [1],
      lostDevice: [],
      warehouseId: [null],
      quantityAdded: [0, [Validators.required,
        Validators.pattern('^\\d*$')]],
      partName: [],
      warehouseName: [],
      supplierName: [],
      sizeWareHouse: [],
    });

    if (this.id) {
      this.getUserDetail(this.id);
    }

  }

  getFileImg() {
    console.warn(this.dataStringPath)
    if (this.dataStringPath === undefined || this.dataStringPath === []) {
      return
    }
    for (const p of this.dataStringPath) {
      this.imageApiService.getFile(p).subscribe(
        value => {
          const data = value;
          this.getFile(data, true)
        }
      )
    }
  }

  getUserDetail(id) {
    //get apy
    this.imageApiService.getListDeviceGroup(id).subscribe(
      value => {
        this.dataStringPath = value;
        if (this.dataStringPath !== []) {
          for (const p of this.dataStringPath) {
            this.imageApiService.getFile(p).subscribe(
              value1 => {
                const data = value1;
                this.getFile(data, true)
              }
            )
          }
        }
      }, error => {

      }
    )


    if (this.data !== null) {
      this.formList.get("code").setValue(this.data.code);
      this.formList.get("name").setValue(this.data.name);
      this.formList.get("size").setValue(this.data.size);
      this.formList.get("unit").setValue(this.data.unit);
      this.formList.get("sizeId").setValue(this.data.sizeId);
      this.formList.get("note").setValue(this.data.note);
      this.formList.get("specifications").setValue(this.data.specifications);
      this.formList.get("supplierName").setValue(this.data.supperName);
      this.formList.get("sizeWareHouse").setValue(this.data.sizeWareHouse);
      this.formList.get("warehouseName").setValue(this.data.warehouseName);

    }
  }

  onCancel() {
    this.activeModal.dismiss();

  }

  onSubmitData() {
    if (this.formList.get("unit").value !== 1) {
      this.formList.get("sizeUnit").setValue(1);
    }
    if (this.isDuplicateUserCode){
      return
    }
    if (this.type === "add") {
      this.formList.get('sizeId').setValue(this.formList.get('size').value)
      this.listDeviceService.save(this.formList.value).subscribe(
        res => {
          if (this.type === 'add') {
            this.toastService.openSuccessToast('Thêm mới thành công !');
            this.router.navigate(['system-categories/list-device']);
            this.activeModal.dismiss();

            const tyle = 2;
            const formData = new FormData();
            for (const c of this.listDataImg) {
              formData.append("file", c);
            }
            this.imageApiService.save(res.id, tyle, formData).subscribe(
              res1 => {
                console.warn(res1)
              },
              error => {

              }
            )
          }
        },
        error => {
          this.toastService.openErrorToast(this.type === 'add' ? 'Thêm mới không thành công' : 'Sửa thất bại');
          this.spinner.hide();
        }
      )

    }
    if (this.type === "update") {
      const z: number = this.formList.get('sizeId').value
      const y: number = this.formList.get("quantityAdded").value
      this.formList.get('sizeId').setValue(z + y)
      console.warn(this.formList.value)

      this.listDeviceService.update(this.formList.value, this.id).subscribe(
        value => {
          this.toastService.openSuccessToast('Thêm mới số lượng thành công !');
          this.router.navigate(['system-categories/list-device']);
          this.activeModal.dismiss();
        },
        error => {
          this.toastService.openErrorToast(this.type === 'add' ? 'Thêm mới không thành công' : 'Sửa thất bại');
          this.spinner.hide();
        }
      )
      const tyle = 2;
      const formData = new FormData();
      for (const c of this.listDataImg) {
        formData.append("file", c);
      }
      formData.append("data", this.dataIm);
      this.imageApiService.update(this.id, tyle, formData).subscribe(
        value => {
          console.warn(value)
        },
        error => {

        }
      )

    }
    // this.router.navigate(['system-categories/list-device']);
    // this.activeModal.dismiss();

  }

  onBlurUserCode() {
    this.listDeviceService.getFindByCodeCustom(this.formList.get("code").value).subscribe(
      value => {
        this.isDuplicateUserCode = false;
      }, error => {
        this.isDuplicateUserCode = true;

      }
    )
  }

  get formControl() {
    return this.formList.controls;
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  isFieldValid(field: string) {
    return !this.formList.get(field).valid && this.formList.get(field).touched;
  }

  trimSpace(element) {
    const value = this.getValueOfField(element);
    if (value) {
      this.setValueToField(element, value.trim());

    }
  }

  setValueToField(item, data) {
    this.formList.get(item).setValue(data);
  }

  getValueOfField(item) {
    return this.formList.get(item).value;
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


  onSelectFile(event) {
    const file = event.target.files
  }

  selectFiles(event) {
    this.selectedFiles = event.target.files;
  }

  upload(idx, file) {
    this.progressInfos.push(file);
    console.warn(this.progressInfos)
  }

  uploadFiles() {
    this.message = '';
    for (let i = 0; i < this.selectedFiles.length; i++) {
      this.upload(i, this.selectedFiles[i]);
    }
  }

  getFileReder(file) {
    const reader = new FileReader();
    const url = "";
    reader.readAsDataURL(file); // read file as data url

    reader.onload = (event) => {
      // url = event.target;
      return url;
    }
    return url;
  }

  deleteImg(file) {
    console.warn("111" + file)
    this.progressInfos.splice(file, 1);
  }

  deleteImgCustom(i){
    
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
  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  getSupperPartList() {
    const id = this.formList.get("partId").value ? this.formList.get("partId").value : -1;
    this.supplierServieceService.getListSuppler().subscribe(
      value => {
        this.supplierList = value
      }, error => {
        this.supplierList = []
      }
    )
  }

  getWarehousePartList() {
    const id = this.formList.get("partId").value ? this.formList.get("partId").value : -1;
    this.idOle = id;
    this.warehouseServicesService.getByPart(id).subscribe(
      value => {
        this.warehouseList = value
      }, error => {
        this.supplierList = []
      }
    )
  }

  xetWareHouse() {
    const id = this.formList.get("partId").value ? this.formList.get("partId").value : -1;
    console.warn("this" + id)
    if (this.idOle !== id) {
      this.formList.get("warehouseId").setValue(null);
      this.getWarehousePartList();
    }
  }

  selectFile(event) {
    for (const p of event.target.files) {
      this.getFile(p, true)
    }

  }

  getFile(file, check) {
    if (!file || file.length === 0) {
      this.msg = 'You must select an image';
      return;
    }

    const mimeType = file.type;
    this.listDataImg.push(file)
    if (mimeType.match(/image\/*/) == null) {
      this.msg = "Only images are supported";
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = (_event) => {
      this.msg = "";
      this.url = reader.result;
      this.listurl.push({chech: check, data: reader.result, url: null})

    }
  }

  cleanAnh(i) {
    this.listDataImg.splice(i, 1)
    this.listurl.splice(i, 1)
  }
}
