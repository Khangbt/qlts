import {Component, HostListener, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HeightService} from 'app/shared/services/height.service';
import {SHOW_HIDE_COL_HEIGHT} from 'app/shared/constants/perfect-scroll-height.constants';
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from 'app/shared/constants/pagination.constants';
import {ConfirmModalComponent} from 'app/shared/components/confirm-modal/confirm-modal.component';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {OrganizationCategoriesModel} from 'app/core/models/system-categories/organization-categories.model';
import {CommonService} from 'app/shared/services/common.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {ToastService} from 'app/shared/services/toast.service';
import {TranslateService} from '@ngx-translate/core';
import {Observable, forkJoin, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ListHandoverProductService} from 'app/core/services/list-handover-product/list-handover-product.service'
import {ListHandoverProduct} from "app/core/models/list-handover-product/list-handover-product.model";

@Component({
  selector: 'jhi-list-handover-product',
  templateUrl: './list-handover-product.component.html',
  styleUrls: ['./list-handover-product.component.scss'],
})
export class ListHandoverProductComponent implements OnInit {
  // @Input() public selectedData: OrganizationCategoriesModel;
  // @Input() type;
  isCheckedAdd = false;
  height: number;
  SHOW_HIDE_COL_HEIGHT = SHOW_HIDE_COL_HEIGHT;
  itemsPerPage: any;
  maxSizePage: any;
  routeData: any;
  page: any;
  second: any;
  totalItems: any;
  previousPage: any;
  predicate: any;
  reverse: any;
  checkCode = false;
  parentOrganizationList = new Observable<any[]>();
  groupOrganizationList: any[] = [];
  listUnit$ = new Observable<any[]>();
  unitSearch;
  debouncer: Subject<string> = new Subject<string>();
  listUnit1$ = new Observable<any[]>();
  unitSearch1;
  debouncer1: Subject<string> = new Subject<string>();
  isModalConfirmShow = false;
  action: String;
  fieldArray: ListHandoverProduct[] = [];statusOptions = [{statusId: 0, statusValue: "Chưa làm"},
    {statusId: 1, statusValue: "Đang làm"},
    {statusId: 2, statusValue: "Đã hoàn thành"},
    {statusId: 3, statusValue: "Đã bàn giao khách hàng"},
    {statusId: 4, statusValue: "Đang sửa theo comment khách hàng"}
  ];

  listDelete = [];
  isEdit = [];
  objClone: ListHandoverProduct[] = [];
  disableAddNew = false;
  isFieldArrayAddNew = false;
  checkProduct: ListHandoverProduct[] = [];
  projectId: any;
  valueOldString: String;
  valueNewString: String;
  lstvalueOld: any[] = [];
  lstvalueNew: any[] = [];
  lstProductClone: any[] =[];
  lstDeleteHistory: any[] = [];
  lstHistory: any[]=[];
  lstHistoryView: any[]=[];

  constructor(
    private http: HttpClient,
    private activatedRoute: ActivatedRoute,
    private heightService: HeightService,
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private commonService: CommonService,
    private toastService: ToastService,
    private translateService: TranslateService,
    // public activeModal: NgbActiveModal,
    private listHandoverProductService: ListHandoverProductService,
    protected router: Router,
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.maxSizePage = MAX_SIZE_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      if (data && data.pagingParams) {
        this.page = data.pagingParams.page;
        this.previousPage = data.pagingParams.page;
        this.reverse = data.pagingParams.ascending;
        this.predicate = data.pagingParams.predicate;
      }
    });
  }

  permissionCheck(permission?: string) {
    return this.commonService.havePermission(permission);
  }

  buildForm() {
    this.activatedRoute.queryParams.subscribe(param => {
      this.projectId = param.id;
      this.loadHistory(this.projectId);
      this.listHandoverProductService.getProductList(param.id).subscribe(
        res => {
          this.checkProduct = JSON.parse(JSON.stringify(res));
          if (res) {
            if (res.length === 0) {
              this.isFieldArrayAddNew = true;
            }
            for (let i = 0; i < res.length; i++) {
              this.isEdit[i] = true;
              this.fieldArray.push(res[i]);
            }
          } else {
            this.fieldArray = [];
          }
        },
        err => {
          this.fieldArray = [];
        }
      );
    });
  }

  loadHistory(i){
    this.listHandoverProductService.getHistoryProductList(i).subscribe(
      res => {
        this.lstHistoryView = res;
      }
    );
  }

  ngOnInit() {
    this.buildForm();
    this.onResize();
  }

  addFieldValue() {
    const newRecord = {
      projectId: this.projectId,
      productName: '',
      statusHandover: 0,
      description: ''
    }
    this.disableAddNew = true;
    this.isEdit.unshift(false);
    this.fieldArray.unshift(newRecord);
    this.checkProduct.unshift(newRecord);
  }

  updateRow($event: MouseEvent, i: number) {
    this.objClone[i] = JSON.parse(JSON.stringify(this.fieldArray[i]));
    this.isEdit[i] = !this.isEdit[i];
  }

  onNotUpdate(index) {
    if (this.objClone[index] != null) {
      this.fieldArray[index] = this.objClone[index];
      this.isEdit[index] = !this.isEdit[index];
    } else {
      this.fieldArray.splice(index, 1);
      this.checkProduct.splice(index,1);
      this.isEdit.splice(index,1);
    }
    this.disableAddNew = false;
  }

  onUpdate(index) {
    if (this.checkSP(index)) {
      this.isEdit[index] = !this.isEdit[index];
        this.disableAddNew = false;

    }
  }

  checkSP(i){
    let checkValid = true;
    let checkSP = 0;
    if(this.fieldArray[i].productName.length === 0){
      this.toastService.openErrorToast('Sản phẩm không được để trống.');
      return false;
    }
    for (let index = 0; index< this.fieldArray.length; index++){
      if(this.fieldArray[i].productName.trim() === this.fieldArray[index].productName.trim()){
        checkSP++;
      }
    }
    if(checkSP>1){
      this.toastService.openErrorToast('Sản phẩm này đã tồn tại.');
      return checkValid = false;
    }
    return checkValid;
  }

  deleteFieldValue(index) {
    this.isModalConfirmShow = true;
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'delete';
    modalRef.componentInstance.param = this.fieldArray[index].productName;
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.listDelete.push(this.fieldArray[index].productHandoverId);
        this.lstDeleteHistory.push(this.fieldArray[index]);
        this.fieldArray.splice(index, 1);
        this.isEdit.splice(index, 1);
        this.checkProduct.splice(index, 1);
      }
      this.isModalConfirmShow = false;
    });
  }

  onCloseAddModal() {
    this.isModalConfirmShow = true;
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'confirm';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.router.navigate(['system-categories/project-management']);
      }
      this.isModalConfirmShow = false;
    });
  }

  onSubmitData() {
    if (this.validData()) {
      // this.activeModal.dismiss();
      this.listChange();
      const saveReq = this.listHandoverProductService.save(this.fieldArray);
      const saveHistoryReq = this.listHandoverProductService.saveHistory(this.lstHistory);
      const deleteReq = this.listHandoverProductService.delete(this.listDelete);
      forkJoin([saveReq, saveHistoryReq, deleteReq]).subscribe();

      if (this.isFieldArrayAddNew === true) {
        this.toastService.openSuccessToast('Thêm danh sách sản phẩm bàn giao thành công');
      } else if (this.fieldArray.length === 0) {
        this.toastService.openSuccessToast('Xóa danh sách sản phẩm bàn giao thành công');
      } else {
        this.toastService.openSuccessToast('Sửa danh sách sản phẩm bàn giao thành công');
      }
      this.router.navigate(['system-categories/project-management']);
      this.loadHistory(this.projectId);
      // this.router.navigate(['project-management/list-handover-product'], {queryParams:{id: this.projectId}});
    }
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  validData() {
    let checkValid = true;
    for (let i = 0; i < this.fieldArray.length; i++) {
      if (this.fieldArray[i].productName.length < 1) {
        this.toastService.openErrorToast('Trường Sản phẩm tại ' + (i + 1) + ' không được để trống!');
        checkValid = false;
      }
    }
    return checkValid;
  }

  export() {
    this.listHandoverProductService.exportData(this.projectId).subscribe(res => {
      if (res.status === 200) {
        this.downloadFile(res);
        this.toastService.openSuccessToast('Export thành công!');
      }
    },error1 => {
      this.toastService.openErrorToast('Export gặp lỗi hệ thống!');
    });
  }
  downloadFile(data) {
    if (!data || !data.body) {
      this.toastService.openWarningToast(this.translateService.instant('common.toastr.messages.info.noDataToExport'));
      return;
    }
    const fileName = data.headers.get('File');
    const link = document.createElement('a');
    const url = URL.createObjectURL(data.body);
    link.setAttribute('href', url);
    link.setAttribute('download', fileName);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  listChange() {

    let isChanged = false;
    this.lstProductClone = JSON.parse(JSON.stringify(this.fieldArray));
    for (let i = 0; i < this.lstProductClone.length; i++) {
      const itemNew: any = {};
      const itemOld: any = {};
      const history : any ={};
      if (this.lstProductClone[i].productHandoverId === undefined) {
        itemNew.projectId = this.projectId;
        itemNew.productName = this.lstProductClone[i].productName;
        itemNew.statusHandover = this.lstProductClone[i].statusHandover;
        itemNew.description = this.lstProductClone[i].description;
        history.valueOld = "0";
        history.valueNew = JSON.stringify(itemNew);
        history.valueId = this.projectId;
        history.userCreate = JSON.parse(localStorage.getItem('user')).humanResourceId;
        this.lstHistory.push(history);
      }
    }
    for (let i = 0; i < this.lstProductClone.length; i++) {
      const itemNew: any = {};
      const itemOld: any = {};
      const history : any ={};
      if (this.lstProductClone[i].productHandoverId !== undefined) {
        if (this.lstProductClone[i].productName !== this.checkProduct[i].productName) {
          itemOld.productName = this.checkProduct[i].productName;
          itemNew.productName = this.lstProductClone[i].productName;
          isChanged = true;
        }
        if (this.lstProductClone[i].statusHandover !== this.checkProduct[i].statusHandover) {
          itemNew.statusHandover = this.lstProductClone[i].statusHandover;
          itemOld.statusHandover = this.checkProduct[i].statusHandover;
          isChanged = true;
        }
        if (this.lstProductClone[i].description !== this.checkProduct[i].description) {
          itemNew.description = this.lstProductClone[i].description;
          itemOld.description = this.checkProduct[i].description;
          isChanged = true;
        }
        if (isChanged) {
          itemNew.line = this.lstProductClone[i].productName;
          history.valueOld = JSON.stringify(itemOld);
          history.valueNew = JSON.stringify(itemNew);
          history.valueId = this.projectId;
          history.userCreate = JSON.parse(localStorage.getItem('user')).humanResourceId;
          this.lstHistory.push(history);
        }
      }
      isChanged = false;
    }
    if (this.lstDeleteHistory.length > 0) {
      for (let i = 0; i < this.lstDeleteHistory.length; i++) {
        const itemOld: any = {};
        const history : any ={};
        itemOld.projectId = this.projectId;
        itemOld.productName = this.lstDeleteHistory[i].productName;
        itemOld.statusHandover = this.lstDeleteHistory[i].statusHandover;
        itemOld.description = this.lstDeleteHistory[i].description;
        history.valueOld = JSON.stringify(itemOld);
        history.valueNew = "0";
        history.valueId = this.projectId;
        history.userCreate = JSON.parse(localStorage.getItem('user')).humanResourceId;
        this.lstHistory.push(history);
      }
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

  viewChangeStr(history) {
    const valueNew = JSON.parse(history.valueNew);
    const valueOld = JSON.parse(history.valueOld);
    let result = "";
    if (valueOld === 0) {
      return 'Thêm mới sản phẩm bàn giao khách hàng của sản phẩm: ' + valueNew.productName;
    } else if (valueNew === 0) {
      return 'Xóa sản phẩm bàn giao khách hàng của sản phẩm: ' + valueOld.productName;
    } else if (valueOld !== null || valueNew !== null) {
      if (valueNew.line || valueOld.line) {
        result += 'tại sản phẩm: ' + valueNew.line + ' sửa ';
      }
      if (valueNew.productName || valueOld.productName) {
        result += 'Sản phẩm: ' + valueOld.productName + ' thành ' + valueNew.productName;
      }
      if (valueNew.statusHandover || valueOld.statusHandover) {
        result += ((result ? ', ' : '') +'Trạng thái: ' + this.convertStatus(valueOld.statusHandover) + ' thành ' + this.convertStatus(valueNew.statusHandover));
      }
      if (valueNew.description || valueOld.description) {
        result += ((result ? ', ' : '') + 'Ghi chú: ' + (valueOld.description ? valueOld.description : 'không có') + ' thành ' + valueNew.description);
      }
      return result;
    }
  }
  convertStatus(i){
    if(i === 0){
      return 'Chưa làm';
    }
    if(i === 1){
      return 'Đang làm';
    }
    if(i === 2){
      return 'Đã hoàn thành';
    }
    if(i === 3){
      return 'Đã bàn giao khách hàng';
    }
    if(i === 4){
      return 'Đang sửa theo comment khách hàng';
    }
  }


}
